package com.panificadora.trentin.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.panificadora.trentin.domain.Produto;
import com.panificadora.trentin.domain.Venda;
import com.panificadora.trentin.domain.VendaStatus;
import com.panificadora.trentin.service.ProdutoService;
import com.panificadora.trentin.service.VendaService;

@Controller
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;
    private final ProdutoService produtoService;

    public VendaController(VendaService vendaService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.produtoService = produtoService;
    }

    // Abre a tela PDV — cria/usa uma venda em aberto
    @GetMapping("/pdv")
    public String abrirPdv(Model model, @RequestParam(value = "vendaId", required = false) Long vendaId) {
        Venda venda;

        if (vendaId != null) {
            venda = vendaService.buscarPorId(vendaId);
            if (venda == null) { // se não encontrar, cria nova venda
                venda = new Venda();
                venda.setStatus(VendaStatus.ABERTA);
                vendaService.salvar(venda);
            }
        } else {
            venda = new Venda();
            venda.setStatus(VendaStatus.ABERTA);
            vendaService.salvar(venda);
        }

        model.addAttribute("venda", venda);
        return "venda/pdv"; // Thymeleaf vai buscar src/main/resources/templates/venda/pdv.html
    }

    // Lookup produto por código — usado pelo front via fetch/axios
    @GetMapping("/produto/lookup")
    @ResponseBody
    public ResponseEntity<?> lookupProduto(@RequestParam("codigo") String codigo) {
        Produto produto = produtoService.findByCode(codigo);
        if (produto != null) {
            return ResponseEntity.ok(produto); // retorna JSON
        } else {
            return ResponseEntity.notFound().build(); // retorna 404
        }
    }

    // Adicionar item via AJAX
    @PostMapping("/{vendaId}/itens")
    @ResponseBody
    public ResponseEntity<?> adicionarItemAjax(@PathVariable Long vendaId,
                                               @RequestParam String codigo,
                                               @RequestParam int quantidade) {
        try {
            Venda venda = vendaService.adicionarItemPorCodigo(vendaId, codigo, quantidade);
            return ResponseEntity.ok(venda);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Finalizar venda
    @PostMapping("/{vendaId}/finalizar")
    public String finalizarVenda(@PathVariable Long vendaId,
                                 @RequestParam(required = false) String cliente,
                                 @RequestParam(required = false) String metodoPagamento,
                                 Model model) {
        try {
            vendaService.finalizarVenda(vendaId, metodoPagamento, cliente);
            model.addAttribute("success", "Venda finalizada com sucesso.");
            return "redirect:/vendas/pdv"; // volta para PDV com nova venda
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            Venda venda = vendaService.buscarPorId(vendaId);
            model.addAttribute("venda", venda != null ? venda : new Venda());
            return "venda/pdv";
        }
    }
}