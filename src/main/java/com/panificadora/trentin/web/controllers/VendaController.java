package com.panificadora.trentin.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.panificadora.trentin.entities.MetodoPagamento;
import com.panificadora.trentin.entities.Produto;
import com.panificadora.trentin.entities.Venda;
import com.panificadora.trentin.service.ProdutoService;
import com.panificadora.trentin.service.VendaService;

@Controller
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private ProdutoService produtoService;

    // ===============================
    // === ABRIR TELA DO PDV ========
    // ===============================
    @GetMapping("/pdv")
    public String abrirPdv(ModelMap model) {
        Venda venda = vendaService.criarVenda();
        model.addAttribute("venda", venda);
        return "/venda/pdv";
    }

    // ===============================
    // === FINALIZAR VENDA ==========
    // ===============================
    @PostMapping("/{vendaId}/finalizar")
    public String finalizarVenda(@PathVariable Long vendaId,
                                 @RequestParam MetodoPagamento metodoPagamento,
                                 @RequestParam String cliente,
                                 RedirectAttributes attr) {

        try {

            vendaService.finalizarVenda(vendaId, metodoPagamento, cliente);

            attr.addFlashAttribute("success", "Venda finalizada com sucesso!");

            // 🔹 Redireciona para impressão do DANFE
            return "redirect:/vendas/" + vendaId + "/imprimir";

        } catch (RuntimeException e) {

            attr.addFlashAttribute("error", "Erro ao finalizar venda: " + e.getMessage());

            return "redirect:/vendas/pdv";
        }
    }

    // ===============================
    // === TELA DE IMPRESSÃO ========
    // ===============================
    @GetMapping("/{id}/imprimir")
    public String imprimir(@PathVariable Long id, ModelMap model) {

        Venda venda = vendaService.buscarPorId(id);

        model.addAttribute("venda", venda);

        return "/venda/imprimir";
    }

    // ===============================
    // === GERAR DANFE (PDF) ========
    // ===============================
    @GetMapping("/{id}/danfe")
    public ResponseEntity<byte[]> danfe(@PathVariable Long id) {

        Venda venda = vendaService.buscarPorId(id);

        byte[] pdf = vendaService.gerarDanfe(venda);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }

    // ===============================
    // === LISTAR VENDAS ============
    // ===============================
    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("vendas", vendaService.buscarTodos());
        return "/venda/lista";
    }

    // ===============================
    // === DETALHAR VENDA ===========
    // ===============================
    @GetMapping("/detalhar/{id}")
    public String detalhar(@PathVariable("id") Long id, ModelMap model) {
        Venda venda = vendaService.buscarPorId(id);
        model.addAttribute("venda", venda);
        return "/venda/detalhe";
    }

    // ===============================
    // === LOOKUP PRODUTO ===========
    // ===============================
    @GetMapping("/produto/lookup")
    @ResponseBody
    public ResponseEntity<?> lookupProduto(@RequestParam("codigo") String codigo) {

        Produto produto = produtoService.findByCode(codigo);

        if (produto != null) {
            return ResponseEntity.ok(produto);
        }

        return ResponseEntity.notFound().build();
    }

    // ===============================
    // === ADICIONAR ITEM ===========
    // ===============================
    @PostMapping("/{vendaId}/itens")
    @ResponseBody
    public ResponseEntity<?> adicionarItemAjax(@PathVariable Long vendaId,
                                               @RequestParam String codigo) {

        try {

            Venda venda = vendaService.adicionarItemPorCodigo(vendaId, codigo);

            return ResponseEntity.ok(venda);

        } catch (RuntimeException ex) {

            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ===============================
    // === REMOVER ITEM ============
    // ===============================
    @DeleteMapping("/{vendaId}/itens/{itemId}")
    @ResponseBody
    public ResponseEntity<?> removerItemAjax(@PathVariable Long vendaId,
                                             @PathVariable Long itemId) {

        try {

            Venda venda = vendaService.removerItem(vendaId, itemId);

            return ResponseEntity.ok(venda);

        } catch (RuntimeException ex) {

            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ===============================
    // === EXCLUIR VENDA ============
    // ===============================
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {

        try {

            vendaService.excluir(id);

            attr.addFlashAttribute("success", "Venda excluída com sucesso!");

        } catch (RuntimeException ex) {

            attr.addFlashAttribute("error", "Erro ao excluir venda: " + ex.getMessage());
        }

        return "redirect:/vendas/listar";
    }

    // ===============================
    // === VISUALIZAR VENDA =========
    // ===============================
    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable("id") Long id, ModelMap model) {

        Venda venda = vendaService.buscarPorIdComItens(id);

        if (venda == null) {
            model.addAttribute("error", "Venda não encontrada!");
            return "/venda/lista";
        }

        model.addAttribute("venda", venda);

        return "/venda/pdv";
    }
}