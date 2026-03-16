package com.panificadora.trentin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panificadora.trentin.entities.Venda;
import com.panificadora.trentin.fiscal.NfceBuilder;

import br.com.swconsultoria.nfe.Nfe;

import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.DocumentoEnum;

import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe;

@Service
public class NfceService {

    @Autowired
    private NfeConfigService configService;

    @Autowired
    private NfceBuilder nfceBuilder;

    public void emitirNfceHomologacao(Venda venda) throws Exception {

        ConfiguracoesNfe config = configService.getConfiguracoesNfe();

        TNFe nfe = nfceBuilder.montarNfce(venda, config);

        TEnviNFe lote = new TEnviNFe();
        lote.setVersao("4.00");
        lote.setIdLote("1");
        lote.setIndSinc("1");

        lote.getNFe().add(nfe);

        TRetEnviNFe retorno = Nfe.enviarNfe(
                config,
                lote,
                DocumentoEnum.NFE
        );

        System.out.println("STATUS: " + retorno.getCStat());
        System.out.println("MOTIVO: " + retorno.getXMotivo());
    }
}