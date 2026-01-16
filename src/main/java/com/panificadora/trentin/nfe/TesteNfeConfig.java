package com.panificadora.trentin.nfe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.panificadora.trentin.service.NfeConfigService;

@Component
public class TesteNfeConfig implements CommandLineRunner {

    @Autowired
    private NfeConfigService nfeConfigService;

    @Override
    public void run(String... args) throws Exception {
        nfeConfigService.getConfiguracoesNfe();
        System.out.println("✅ Configuração da NF-e carregada com sucesso!");
    }
}
