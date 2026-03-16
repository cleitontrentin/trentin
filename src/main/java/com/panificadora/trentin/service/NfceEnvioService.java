package com.panificadora.trentin.service;

import org.springframework.stereotype.Service;

import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.DocumentoEnum;

import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.com.swconsultoria.nfe.schema_4.consReciNFe.TRetConsReciNFe;

@Service
public class NfceEnvioService {

	public TRetConsReciNFe enviarNfce(TNFe nfeAssinada, ConfiguracoesNfe config) throws Exception {

        TEnviNFe enviNFe = new TEnviNFe();

        enviNFe.setVersao("4.00");
        enviNFe.setIdLote("1");
        enviNFe.setIndSinc("0");

        enviNFe.getNFe().add(nfeAssinada);

        TRetEnviNFe retornoEnvio = Nfe.enviarNfe(
                config,
                enviNFe,
                DocumentoEnum.NFCE
        );

        System.out.println("STATUS ENVIO: " + retornoEnvio.getCStat());
        System.out.println("MOTIVO: " + retornoEnvio.getXMotivo());

        if ("103".equals(retornoEnvio.getCStat())) {

            String recibo = retornoEnvio.getInfRec().getNRec();

            System.out.println("RECIBO: " + recibo);
            
            Thread.sleep(3000);
            
            TRetConsReciNFe retornoRecibo =
                    Nfe.consultaRecibo(
                            config,
                            recibo,
                            DocumentoEnum.NFCE
                    );

            System.out.println("STATUS NOTA: " + retornoRecibo.getCStat());
            System.out.println("MOTIVO: " + retornoRecibo.getXMotivo());

            return retornoRecibo;
        }

        throw new Exception("Falha no envio: " + retornoEnvio.getXMotivo());
    }
}