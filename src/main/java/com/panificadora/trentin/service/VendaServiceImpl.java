package com.panificadora.trentin.service;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panificadora.trentin.dao.VendaDao;
import com.panificadora.trentin.domain.Produto;
import com.panificadora.trentin.domain.Venda;
import com.panificadora.trentin.domain.VendaItem;
import com.panificadora.trentin.domain.VendaStatus;

@Service
@Transactional(readOnly = false)
public class VendaServiceImpl implements VendaService {

	@Autowired
    private VendaDao dao;

    @Autowired
    private ProdutoService produtoService;

    @Override
    public void salvar(Venda venda) {
        if (venda.getStatus() == null) {
            venda.setStatus(VendaStatus.ABERTA);
        }
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
        Venda venda = dao.findById(id);
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
        dao.save(venda);
        return venda;
    }
    

    @Transactional
    public Venda adicionarItemPorCodigo(Long vendaId, String codigoProduto, int quantidade) {
        Venda venda = buscarPorId(vendaId);
        Produto produto = produtoService.findByCode(codigoProduto);

        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }

        if (produto.getStock() != null && produto.getStock() < quantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }

        VendaItem item = new VendaItem();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPrice());

        venda.adicionarItem(item);
        dao.update(venda);

        return venda;
    }

    @Transactional
    public Venda finalizarVenda(Long id, String metodoPagamento, String nomeCliente) {
        Venda venda = buscarPorId(id);

        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new RuntimeException("Venda sem itens");
        }

        venda.recalcularTotal();
        venda.setStatus(VendaStatus.FINALIZADA);
        venda.setCliente(nomeCliente);
        // venda.setMetodoPagamento(metodoPagamento);

        dao.update(venda);

        // baixa o estoque de cada item
        for (VendaItem item : venda.getItens()) {
            produtoService.removeStock(item.getProduto().getId(), item.getQuantidade());
        }

        return venda;
    }
}