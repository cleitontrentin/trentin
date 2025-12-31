package com.panificadora.trentin.dao;

import org.springframework.stereotype.Repository;

import com.panificadora.trentin.entities.Venda;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;



@Repository
public class VendaDaoImpl extends AbstractDao<Venda, Long> implements VendaDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Venda buscarPorIdComItens(Long id) {
        try {
            return em.createQuery(
                "SELECT v FROM Venda v " +
                "LEFT JOIN FETCH v.itens i " +
                "LEFT JOIN FETCH i.produto " +
                "WHERE v.id = :id", Venda.class)
                .setParameter("id", id)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}