package com.panificadora.trentin.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panificadora.trentin.entities.XmlGenerator;

import br.com.swconsultoria.nfe.Assinar;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.AssinaturaEnum;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;

@Service
public class NfceService {

    @Autowired
    private NfeConfigService configService;

    public void emitirNfceHomologacao() throws Exception {

        ConfiguracoesNfe config = configService.getConfiguracoesNfe();

        // ===============================
        // 1️⃣ NFC-e (enviNFe)
        // ===============================
        TNFe nfe = new TNFe();
        TNFe.InfNFe infNFe = new TNFe.InfNFe();

        infNFe.setId("NFe" + "CHAVE_AQUI"); // depois geramos
        infNFe.setVersao("4.00");

        // -------------------------------
        // IDE
        // -------------------------------
        TNFe.InfNFe.Ide ide = new TNFe.InfNFe.Ide();
        ide.setCUF("41"); // Paraná
        ide.setNatOp("VENDA");
        ide.setMod("65"); // NFC-e
        ide.setSerie("1");
        ide.setNNF("1");

        ide.setDhEmi(
            OffsetDateTime
                .now(ZoneOffset.of("-03:00"))
                .toString()
        );

        ide.setTpNF("1");        // saída
        ide.setIdDest("1");      // consumidor final
        ide.setTpEmis("1");      // normal
        ide.setTpAmb(config.getAmbiente().getCodigo());
        ide.setFinNFe("1");      // normal
        ide.setIndFinal("1");    // consumidor final
        ide.setIndPres("1");     // presencial
        ide.setProcEmi("0");     // app do contribuinte
        ide.setVerProc("1.0");

        infNFe.setIde(ide);
        nfe.setInfNFe(infNFe);

        // ===============================
        // 2️⃣ XML
        // ===============================
        String xml = XmlGenerator.gerarXml(nfe);

        // ===============================
        // 3️⃣ Assinatura
        // ===============================
        String xmlAssinado = Assinar.assinaNfe(
                config,
                xml,
                AssinaturaEnum.NFE
        );

        System.out.println("XML ASSINADO:\n" + xmlAssinado);
    }
}
