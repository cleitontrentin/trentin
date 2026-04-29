package com.panificadora.trentin.fiscal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panificadora.trentin.entities.MetodoPagamento;
import com.panificadora.trentin.entities.Venda;
import com.panificadora.trentin.entities.VendaItem;
import com.panificadora.trentin.service.NfceNumeroService;

import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnderEmi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TUfEmi;

@Service
public class NfceBuilder {

    @Autowired
    private NfceNumeroService numeroService;

    private final DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    // ---------------- UTIL ----------------

    private String formatarDecimal(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String formatarQuantidade(BigDecimal valor) {
        return valor.setScale(4, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString();
    }

    // ---------------- PRINCIPAL ----------------

    public TNFe montarNfce(Venda venda, ConfiguracoesNfe config) {

        TNFe nfe = new TNFe();
        TNFe.InfNFe inf = new TNFe.InfNFe();

        inf.setVersao("4.00");

        TNFe.InfNFe.Ide ide = montarIde(config);
        TNFe.InfNFe.Emit emit = montarEmitente();

        inf.setIde(ide);
        inf.setEmit(emit);

        montarProdutos(venda, inf);

        inf.setTotal(montarTotal(venda));
        inf.setTransp(montarTransporte());
        inf.setPag(montarPagamento(venda));

        nfe.setInfNFe(inf);

        return nfe;
    }

    // ---------------- IDE ----------------

    private TNFe.InfNFe.Ide montarIde(ConfiguracoesNfe config) {

        TNFe.InfNFe.Ide ide = new TNFe.InfNFe.Ide();

        ide.setCUF("41");
        ide.setNatOp("VENDA");
        ide.setMod("65");
        ide.setSerie("1");

        // Numeração (controle seu OK)
        ide.setNNF(numeroService.gerarProximoNumero());

        // Código aleatório da NFC-e (OK)
        ide.setCNF(gerarCodigoNumerico());

        // Data/hora emissão (SEFAZ exige ISO com timezone)
        String dhEmi = OffsetDateTime
                .now(ZoneOffset.of("-03:00"))
                .withSecond(0)
                .withNano(0)
                .format(fmt);

        ide.setDhEmi(dhEmi);

        ide.setTpNF("1");          // saída
        ide.setIdDest("1");        // interno
        ide.setCMunFG("4108304");

        ide.setTpImp("4");         // DANFE NFC-e
        ide.setTpEmis("1");        // emissão normal

        ide.setTpAmb(config.getAmbiente().getCodigo());
        ide.setFinNFe("1");        // normal
        ide.setIndFinal("1");      // consumidor final
        ide.setIndPres("1");       // presencial
        ide.setIndIntermed("0");

        ide.setProcEmi("0");
        ide.setVerProc("1.0");

        return ide;
    }
    // ---------------- EMITENTE ----------------

    private TNFe.InfNFe.Emit montarEmitente() {

        TNFe.InfNFe.Emit emit = new TNFe.InfNFe.Emit();

        emit.setCNPJ("10755198000177");
        emit.setXNome("PANIFICADORA TRENTIN LTDA");
        emit.setXFant("PANIFICADORA TRENTIN");

        TEnderEmi ender = new TEnderEmi();
        ender.setXLgr("AVENIDA POR DO SOL");
        ender.setNro("1068");
        ender.setXBairro("JARDIM SÃO PAULO");
        ender.setCMun("4108304");
        ender.setXMun("FOZ DO IGUACU");
        ender.setUF(TUfEmi.PR);
        ender.setCEP("85856430");
        ender.setCPais("1058");
        ender.setXPais("BRASIL");

        emit.setEnderEmit(ender);

        emit.setIE("9047633904");
        emit.setCRT("1");

        return emit;
    }

    // ---------------- PRODUTOS ----------------

    private void montarProdutos(Venda venda, TNFe.InfNFe inf) {

        int item = 1;

        for (VendaItem v : venda.getItens()) {

            TNFe.InfNFe.Det det = new TNFe.InfNFe.Det();
            det.setNItem(String.valueOf(item++));

            TNFe.InfNFe.Det.Prod prod = new TNFe.InfNFe.Det.Prod();

            prod.setCProd(v.getProduto().getCode() != null
                    ? v.getProduto().getCode()
                    : "1");

            prod.setCEAN("SEM GTIN");
            prod.setXProd(v.getProduto().getName());
            prod.setNCM("19059090");
            prod.setCFOP("5102");

            prod.setUCom("UN");
            prod.setQCom(formatarQuantidade(v.getQuantidade()));
            prod.setVUnCom(formatarDecimal(v.getPrecoUnitario()));
            prod.setVProd(formatarDecimal(v.getSubtotal()));

            prod.setCEANTrib("SEM GTIN");
            prod.setUTrib("UN");
            prod.setQTrib(formatarQuantidade(v.getQuantidade()));
            prod.setVUnTrib(formatarDecimal(v.getPrecoUnitario()));

            prod.setIndTot("1");

            det.setProd(prod);

            TNFe.InfNFe.Det.Imposto imposto =
                    new TNFe.InfNFe.Det.Imposto();

            TNFe.InfNFe.Det.Imposto.ICMS icms =
                    new TNFe.InfNFe.Det.Imposto.ICMS();

            TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 icms102 =
                    new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();

            icms102.setOrig("0");
            icms102.setCSOSN("102");

            icms.setICMSSN102(icms102);

            imposto.getContent().add(new JAXBElement<>(
                    new QName("http://www.portalfiscal.inf.br/nfe", "ICMS"),
                    TNFe.InfNFe.Det.Imposto.ICMS.class,
                    icms));

            det.setImposto(imposto);
            inf.getDet().add(det);
        }
    }

    // ---------------- TOTAL ----------------

    private TNFe.InfNFe.Total montarTotal(Venda venda) {

        TNFe.InfNFe.Total total = new TNFe.InfNFe.Total();
        TNFe.InfNFe.Total.ICMSTot icms =
                new TNFe.InfNFe.Total.ICMSTot();

        icms.setVBC("0.00");
        icms.setVICMS("0.00");
        icms.setVICMSDeson("0.00");

        icms.setVProd(formatarDecimal(venda.getTotal()));

        icms.setVNF(formatarDecimal(venda.getTotal()));

        icms.setVTotTrib("0.00");

        total.setICMSTot(icms);

        return total;
    }

    // ---------------- TRANSPORTE ----------------

    private TNFe.InfNFe.Transp montarTransporte() {
        TNFe.InfNFe.Transp t = new TNFe.InfNFe.Transp();
        t.setModFrete("9");
        return t;
    }

    // ---------------- PAGAMENTO ----------------

    private TNFe.InfNFe.Pag montarPagamento(Venda venda) {

        TNFe.InfNFe.Pag pag = new TNFe.InfNFe.Pag();
        TNFe.InfNFe.Pag.DetPag det =
                new TNFe.InfNFe.Pag.DetPag();

        det.setTPag(mapearPagamento(venda.getMetodoPagamento()));
        det.setVPag(formatarDecimal(venda.getTotal()));

        pag.getDetPag().add(det);

        return pag;
    }

    private String mapearPagamento(MetodoPagamento metodo) {

        if (metodo == null) return "01";

        switch (metodo) {
            case DINHEIRO: return "01";
            case CARTAO_CREDITO: return "03";
            case CARTAO_DEBITO: return "04";
            case PIX: return "17";
            default: return "99";
        }
    }

    // ---------------- UTIL ----------------

    private String gerarCodigoNumerico() {
        return String.valueOf(10000000 +
                new Random().nextInt(90000000));
    }
}