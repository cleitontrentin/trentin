package com.panificadora.trentin.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panificadora.trentin.dao.VendaDao;
import com.panificadora.trentin.entities.CodigoBalancaDTO;
import com.panificadora.trentin.entities.CodigoBalancaParser;
import com.panificadora.trentin.entities.MetodoPagamento;
import com.panificadora.trentin.entities.Produto;
import com.panificadora.trentin.entities.Venda;
import com.panificadora.trentin.entities.VendaItem;
import com.panificadora.trentin.entities.VendaStatus;

@Service
@Transactional(readOnly = false)
public class VendaServiceImpl implements VendaService {

	@Autowired
    private VendaDao dao;

    @Autowired
    private ProdutoService produtoService;
    

    @Override
    public void salvar(Venda venda) {
        dao.save(venda);
    }

    @Override
    public void editar(Venda venda) {
        dao.update(venda);
    }

    @Override
    public void excluir(Long id) {
        dao.delete(id);
    }

    @Override
    public Venda buscarPorId(Long id) {
        Venda venda = dao.buscarPorIdComItens(id);
        if (venda == null) {
            throw new RuntimeException("Venda não encontrada");
        }
        return venda;
    }

    @Override
    public List<Venda> buscarTodos() {
        return dao.findAll();
    }

    // Cria nova venda em aberto
    @Transactional
    public Venda criarVenda() {
        Venda venda = new Venda();
        venda.setStatus(VendaStatus.ABERTA);
        venda.setCriadoEm(LocalDateTime.now());
        venda.setTotal(BigDecimal.ZERO);
        dao.save(venda);
        return venda;
    }
    
    private void adicionarItem(Long vendaId, Produto produto, BigDecimal quantidade) {

        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }

        Venda venda = buscarPorId(vendaId);

        // Verifica se o item já existe na venda
        VendaItem itemExistente = venda.getItens().stream()
            .filter(i -> i.getProduto().getId().equals(produto.getId()))
            .findFirst()
            .orElse(null);

        if (itemExistente != null) {
            itemExistente.setQuantidade(
                itemExistente.getQuantidade().add(quantidade)
            );
        } else {
            VendaItem item = new VendaItem();
            item.setVenda(venda);
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setPrecoUnitario(produto.getPrice());
            venda.getItens().add(item);
        }

        venda.recalcularTotal();
        dao.update(venda);
    }

    

    @Override
    @Transactional
    public Venda adicionarItemPorCodigo(Long vendaId, String codigo) {

        CodigoBalancaDTO dto = CodigoBalancaParser.parse(codigo);
        Produto produto = produtoService.findByCode(dto.getCodigoProduto());

        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }

        Venda venda = buscarPorId(vendaId);

        VendaItem item = new VendaItem();
        item.setVenda(venda);
        item.setProduto(produto);

        if (dto.isPorValor()) {
            item.setQuantidade(BigDecimal.ONE);       // apenas informativo
            item.setPrecoUnitario(dto.getValor());    // valor FINAL
            item.setSubtotal(dto.getValor());         // valor direto
        } else {
            item.setQuantidade(BigDecimal.ONE);
            item.setPrecoUnitario(produto.getPrice());
        }

        venda.getItens().add(item);
        venda.recalcularTotal();
        dao.update(venda);

        return venda;
    }






    @Override
    public Venda finalizarVenda(Long vendaId, MetodoPagamento metodoPagamento, String cliente) {
        Venda venda = buscarPorId(vendaId);

        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new RuntimeException("Venda sem itens");
        }

        venda.recalcularTotal();
        venda.setStatus(VendaStatus.FINALIZADA);
        venda.setCliente(cliente);
        venda.setMetodoPagamento(metodoPagamento);
        dao.update(venda);

        // baixa o estoque
        for (VendaItem item : venda.getItens()) {
            produtoService.removeStock(item.getProduto().getId(), item.getQuantidade());
        }

        return venda;
    }
    
    public Venda buscarPorIdComItens(Long id) {
        return dao.buscarPorIdComItens(id);
    }
    
    
}