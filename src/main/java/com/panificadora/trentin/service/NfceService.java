package com.panificadora.trentin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panificadora.trentin.dao.VendaDao;
import com.panificadora.trentin.entities.Venda;
import com.panificadora.trentin.fiscal.NfceBuilder;

import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.DocumentoEnum;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe;

@Service
public class NfceService {

    @Autowired
    private NfeConfigService configService;

    @Autowired
    private NfceBuilder builder;

    @Autowired
    private VendaDao vendaDao;

    public void emitir(Venda venda) throws Exception {

        ConfiguracoesNfe config = configService.getConfiguracoesNfe();

        // 1. Monta
        TNFe nfe = builder.montarNfce(venda, config);

        // 2. Monta lote
        TEnviNFe lote = new TEnviNFe();
        lote.setVersao("4.00");
        lote.setIdLote(String.valueOf(System.currentTimeMillis()));
        lote.setIndSinc("1");

        lote.getNFe().add(nfe);
        String xmlGerado = br.com.swconsultoria.nfe.util.XmlNfeUtil.objectToXml(lote);
        System.out.println("XML GERADO:\n" + xmlGerado);

        // 3. Envia (já assina automaticamente)
        TRetEnviNFe retorno = Nfe.enviarNfe(
                config,
                lote,
                DocumentoEnum.NFCE
        );

        System.out.println("STATUS: " + retorno.getCStat());
        System.out.println("MOTIVO: " + retorno.getXMotivo());

        // 4. Validação
        if (!"104".equals(retorno.getCStat())) {
            throw new RuntimeException("Erro no envio: " + retorno.getXMotivo());
        }

        var prot = retorno.getProtNFe().getInfProt();

        if (!"100".equals(prot.getCStat())) {
            throw new RuntimeException("Nota rejeitada: " + prot.getXMotivo());
        }

        // 5. XML (por enquanto simples)
        String xmlAutorizado = br.com.swconsultoria.nfe.util.XmlNfeUtil.objectToXml(nfe);

        // 6. Salva
        venda.setChaveNfce(prot.getChNFe());
        venda.setNumeroNfce(nfe.getInfNFe().getIde().getNNF());
        venda.setStatusNfce("AUTORIZADA");
        venda.setProtocoloNfce(prot.getNProt());
        venda.setXmlNfce(xmlAutorizado);
        venda.setDataEmissaoNfce(java.time.LocalDateTime.now());

        vendaDao.update(venda);
    }
}