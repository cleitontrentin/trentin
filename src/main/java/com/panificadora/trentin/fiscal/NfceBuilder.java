package com.panificadora.trentin.fiscal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

    private static final String CSC = "SEU_CSC_AQUI";
    private static final String ID_CSC = "000001";

    private String formatarDecimal(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String formatarQuantidade(BigDecimal valor) {
        return valor.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

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

        // 🔑 CHAVE
        String data = ide.getDhEmi().substring(2, 4) + ide.getDhEmi().substring(5, 7);

        String chave = gerarChaveNfe(
                ide.getCUF(),
                data,
                emit.getCNPJ(),
                ide.getMod(),
                ide.getSerie(),
                ide.getNNF(),
                ide.getTpEmis(),
                ide.getCNF()
        );

        ide.setCDV(chave.substring(chave.length() - 1));
        inf.setId("NFe" + chave);

        nfe.setInfNFe(inf);

        // 🔥 SUPLEMENTO (QR CODE)
        TNFe.InfNFeSupl supl = new TNFe.InfNFeSupl();

        String qrCode = gerarQrCode(chave, config.getAmbiente().getCodigo());
        supl.setQrCode(qrCode);

        supl.setUrlChave("http://www.fazenda.pr.gov.br/nfce/qrcode");

        nfe.setInfNFeSupl(supl);

        return nfe;
    }

    private String gerarQrCode(String chave, String tpAmb) {
        try {

            String url = "http://www.fazenda.pr.gov.br/nfce/qrcode";

            String dados = "chNFe=" + chave +
                    "&nVersao=100" +
                    "&tpAmb=" + tpAmb +
                    "&cIdToken=" + ID_CSC;

            String hash = sha1(dados + CSC);

            return url + "?" + dados + "&cHashQRCode=" + hash;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar QRCode", e);
        }
    }

    private String sha1(String value) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] bytes = md.digest(value.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private TNFe.InfNFe.Ide montarIde(ConfiguracoesNfe config) {
        TNFe.InfNFe.Ide ide = new TNFe.InfNFe.Ide();

        ide.setCUF("41");
        ide.setNatOp("VENDA");
        ide.setMod("65");
        ide.setSerie("1");

        ide.setNNF(numeroService.gerarProximoNumero());
        ide.setCNF(gerarCodigoNumerico());

        ide.setDhEmi(OffsetDateTime.now(ZoneOffset.of("-03:00")).withNano(0).toString());

        ide.setTpNF("1");
        ide.setIdDest("1");
        ide.setCMunFG("4108304");

        ide.setTpImp("4");
        ide.setTpEmis("1");

        ide.setTpAmb(config.getAmbiente().getCodigo());
        ide.setFinNFe("1");
        ide.setIndFinal("1");
        ide.setIndPres("1");
        ide.setProcEmi("0");
        ide.setVerProc("1.0");

        return ide;
    }

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

    // (resto da sua classe continua exatamente igual)

    private void montarProdutos(Venda venda, TNFe.InfNFe inf) {

        int item = 1;

        for (VendaItem vendaItem : venda.getItens()) {

            TNFe.InfNFe.Det det = new TNFe.InfNFe.Det();
            det.setNItem(String.valueOf(item++));

            TNFe.InfNFe.Det.Prod prod = new TNFe.InfNFe.Det.Prod();

            prod.setCProd(vendaItem.getProduto().getCode() != null ?
                    vendaItem.getProduto().getCode() : "1");

            prod.setCEAN("SEM GTIN");
            prod.setXProd(vendaItem.getProduto().getName());
            prod.setNCM("19059090");
            prod.setCFOP("5102");

            prod.setUCom("UN");
            prod.setQCom(formatarQuantidade(vendaItem.getQuantidade()));
            prod.setVUnCom(formatarDecimal(vendaItem.getPrecoUnitario()));
            prod.setVProd(formatarDecimal(vendaItem.getSubtotal()));

            prod.setCEANTrib("SEM GTIN");
            prod.setUTrib("UN");
            prod.setQTrib(formatarQuantidade(vendaItem.getQuantidade()));
            prod.setVUnTrib(formatarDecimal(vendaItem.getPrecoUnitario()));

            prod.setIndTot("1");

            det.setProd(prod);

            TNFe.InfNFe.Det.Imposto imposto = new TNFe.InfNFe.Det.Imposto();

            // ICMS
            TNFe.InfNFe.Det.Imposto.ICMS icms = new TNFe.InfNFe.Det.Imposto.ICMS();
            TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 icms102 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();

            icms102.setOrig("0");
            icms102.setCSOSN("102");

            icms.setICMSSN102(icms102);

            imposto.getContent().add(new JAXBElement<>(
                    new QName("http://www.portalfiscal.inf.br/nfe", "ICMS"),
                    TNFe.InfNFe.Det.Imposto.ICMS.class, icms));

            // PIS
            TNFe.InfNFe.Det.Imposto.PIS pis = new TNFe.InfNFe.Det.Imposto.PIS();
            TNFe.InfNFe.Det.Imposto.PIS.PISNT pisnt = new TNFe.InfNFe.Det.Imposto.PIS.PISNT();

            pisnt.setCST("07");
            pis.setPISNT(pisnt);

            imposto.getContent().add(new JAXBElement<>(
                    new QName("http://www.portalfiscal.inf.br/nfe", "PIS"),
                    TNFe.InfNFe.Det.Imposto.PIS.class, pis));

            // COFINS
            TNFe.InfNFe.Det.Imposto.COFINS cofins = new TNFe.InfNFe.Det.Imposto.COFINS();
            TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT cofinsnt = new TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT();

            cofinsnt.setCST("07");
            cofins.setCOFINSNT(cofinsnt);

            imposto.getContent().add(new JAXBElement<>(
                    new QName("http://www.portalfiscal.inf.br/nfe", "COFINS"),
                    TNFe.InfNFe.Det.Imposto.COFINS.class, cofins));

            det.setImposto(imposto);
            inf.getDet().add(det);
        }
    }

    private TNFe.InfNFe.Total montarTotal(Venda venda) {

        TNFe.InfNFe.Total total = new TNFe.InfNFe.Total();
        TNFe.InfNFe.Total.ICMSTot icms = new TNFe.InfNFe.Total.ICMSTot();

        icms.setVBC("0.00");
        icms.setVICMS("0.00");

        // 🔥 ESSENCIAL pro PR (mesmo zerado)
        icms.setVICMSDeson("0.00");

        // pode colocar esses zerados pra evitar rejeição futura
        icms.setVFCP("0.00");
        icms.setVBCST("0.00");
        icms.setVST("0.00");
        icms.setVFCPST("0.00");
        icms.setVFCPSTRet("0.00");

        // agora sim valores principais
        icms.setVProd(formatarDecimal(venda.getTotal()));

        icms.setVFrete("0.00");
        icms.setVSeg("0.00");
        icms.setVDesc("0.00");
        icms.setVII("0.00");
        icms.setVIPI("0.00");
        icms.setVIPIDevol("0.00");

        icms.setVPIS("0.00");
        icms.setVCOFINS("0.00");

        icms.setVOutro("0.00");

        icms.setVNF(formatarDecimal(venda.getTotal()));

        // 🔥 obrigatório em muitos estados
        icms.setVTotTrib("0.00");

        total.setICMSTot(icms);

        return total;
    }

    private TNFe.InfNFe.Transp montarTransporte() {
        TNFe.InfNFe.Transp t = new TNFe.InfNFe.Transp();
        t.setModFrete("9");
        return t;
    }

    private TNFe.InfNFe.Pag montarPagamento(Venda venda) {

        TNFe.InfNFe.Pag pag = new TNFe.InfNFe.Pag();
        TNFe.InfNFe.Pag.DetPag det = new TNFe.InfNFe.Pag.DetPag();

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

    private String gerarCodigoNumerico() {
        return String.valueOf(10000000 + new Random().nextInt(90000000));
    }

    private String gerarChaveNfe(String cUF, String data, String cnpj, String mod,
                                String serie, String numero, String tpEmis, String cNF) {

        String chave43 = String.format("%s%s%s%s%03d%09d%s%s",
                cUF, data, cnpj, mod,
                Integer.parseInt(serie),
                Integer.parseInt(numero),
                tpEmis, cNF);

        int peso = 2, soma = 0;

        for (int i = chave43.length() - 1; i >= 0; i--) {
            soma += Character.getNumericValue(chave43.charAt(i)) * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }

        int resto = soma % 11;
        int dv = (resto <= 1) ? 0 : 11 - resto;

        return chave43 + dv;
    }
}