package com.panificadora.trentin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panificadora.trentin.dao.NfceNumeroDao;
import com.panificadora.trentin.entities.NfceNumero;

@Service
public class NfceNumeroService {

    @Autowired
    private NfceNumeroDao nfceNumeroDao;

    @Transactional
    public synchronized String gerarProximoNumero() {

        NfceNumero controle = nfceNumeroDao.buscarControle();

        Long proximo = controle.getUltimoNumero() + 1;

        controle.setUltimoNumero(proximo);

        nfceNumeroDao.update(controle);

        return String.valueOf(proximo);
    }
}