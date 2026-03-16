package com.panificadora.trentin.fiscal;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panificadora.trentin.entities.Venda;
import com.panificadora.trentin.entities.VendaItem;
import com.panificadora.trentin.service.NfceNumeroService;

import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnderEmi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TUfEmi;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

@Service
public class NfceBuilder {

	@Autowired
	private NfceNumeroService numeroService;

	public TNFe montarNfce(Venda venda, ConfiguracoesNfe config) {

		TNFe nfe = new TNFe();
		TNFe.InfNFe inf = new TNFe.InfNFe();

		inf.setVersao("4.00");

		TNFe.InfNFe.Ide ide = montarIde(venda, config);
		TNFe.InfNFe.Emit emit = montarEmitente();

		inf.setIde(ide);
		inf.setEmit(emit);
		inf.setDest(montarDestinatario());

		montarProdutos(venda, inf);

		inf.setTotal(montarTotal(venda));
		inf.setTransp(montarTransporte());
		inf.setPag(montarPagamento(venda));

		String data = ide.getDhEmi().substring(2, 4) + ide.getDhEmi().substring(5, 7);

		String chave = gerarChaveNfe(ide.getCUF(), data, emit.getCNPJ(), ide.getMod(), ide.getSerie(), ide.getNNF(),
				ide.getTpEmis(), ide.getCNF());

		inf.setId("NFe" + chave);

		venda.setChaveNfce(chave);
		venda.setNumeroNfce(ide.getNNF());
		venda.setDataEmissaoNfce(LocalDateTime.now());

		nfe.setInfNFe(inf);

		return nfe;
	}

	private TNFe.InfNFe.Ide montarIde(Venda venda, ConfiguracoesNfe config) {

		TNFe.InfNFe.Ide ide = new TNFe.InfNFe.Ide();

		ide.setCUF("41");
		ide.setNatOp("VENDA");
		ide.setMod("65");
		ide.setSerie("1");

		String numero = numeroService.gerarProximoNumero();
		ide.setNNF(numero);

		ide.setCNF(gerarCodigoNumerico());

		ide.setDhEmi(OffsetDateTime.now(ZoneOffset.of("-03:00")).withNano(0).toString());

		ide.setTpNF("1");
		ide.setIdDest("1");
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
		emit.setIE("9047633904");
		emit.setCRT("1");

		TEnderEmi ender = new TEnderEmi();

		ender.setXLgr("RUA EXEMPLO");
		ender.setNro("123");
		ender.setXBairro("CENTRO");
		ender.setCMun("4108304");
		ender.setXMun("FOZ DO IGUACU");
		ender.setUF(TUfEmi.PR);
		ender.setCEP("85800000");
		ender.setCPais("1058");
		ender.setXPais("BRASIL");

		emit.setEnderEmit(ender);

		return emit;
	}

	private TNFe.InfNFe.Dest montarDestinatario() {

		TNFe.InfNFe.Dest dest = new TNFe.InfNFe.Dest();

		dest.setXNome("CONSUMIDOR NAO IDENTIFICADO");
		dest.setIndIEDest("9");

		return dest;
	}

	private void montarProdutos(Venda venda, TNFe.InfNFe inf) {

		int item = 1;

		for (VendaItem vendaItem : venda.getItens()) {

			TNFe.InfNFe.Det det = new TNFe.InfNFe.Det();
			det.setNItem(String.valueOf(item++));

			TNFe.InfNFe.Det.Prod prod = new TNFe.InfNFe.Det.Prod();

			prod.setCProd(vendaItem.getProduto().getCode() != null ? vendaItem.getProduto().getCode() : "1");

			prod.setCEAN("SEM GTIN");
			prod.setXProd(vendaItem.getProduto().getName());
			prod.setNCM("19059090");
			prod.setCFOP("5102");

			prod.setUCom("UN");
			prod.setQCom(vendaItem.getQuantidade().toString());
			prod.setVUnCom(vendaItem.getPrecoUnitario().toString());
			prod.setVProd(vendaItem.getSubtotal().toString());

			prod.setCEANTrib("SEM GTIN");
			prod.setUTrib("UN");
			prod.setQTrib(vendaItem.getQuantidade().toString());
			prod.setVUnTrib(vendaItem.getPrecoUnitario().toString());

			prod.setIndTot("1");

			det.setProd(prod);

			// =========================
			// IMPOSTOS
			// =========================

			TNFe.InfNFe.Det.Imposto imposto = new TNFe.InfNFe.Det.Imposto();

			// ICMS
			TNFe.InfNFe.Det.Imposto.ICMS icms = new TNFe.InfNFe.Det.Imposto.ICMS();

			TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 icmssn102 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();

			icmssn102.setOrig("0");
			icmssn102.setCSOSN("102");

			icms.setICMSSN102(icmssn102);

			imposto.getContent().add(new JAXBElement<>(new QName("http://www.portalfiscal.inf.br/nfe", "ICMS"),
					TNFe.InfNFe.Det.Imposto.ICMS.class, icms));

			// PIS
			TNFe.InfNFe.Det.Imposto.PIS pis = new TNFe.InfNFe.Det.Imposto.PIS();

			TNFe.InfNFe.Det.Imposto.PIS.PISNT pisnt = new TNFe.InfNFe.Det.Imposto.PIS.PISNT();

			pisnt.setCST("07");

			pis.setPISNT(pisnt);

			imposto.getContent().add(new JAXBElement<>(new QName("http://www.portalfiscal.inf.br/nfe", "PIS"),
					TNFe.InfNFe.Det.Imposto.PIS.class, pis));

			// COFINS
			TNFe.InfNFe.Det.Imposto.COFINS cofins = new TNFe.InfNFe.Det.Imposto.COFINS();

			TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT cofinsnt = new TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT();

			cofinsnt.setCST("07");

			cofins.setCOFINSNT(cofinsnt);

			imposto.getContent().add(new JAXBElement<>(new QName("http://www.portalfiscal.inf.br/nfe", "COFINS"),
					TNFe.InfNFe.Det.Imposto.COFINS.class, cofins));

			det.setImposto(imposto);
			inf.getDet().add(det);
		}
	}

	private TNFe.InfNFe.Total montarTotal(Venda venda) {

		TNFe.InfNFe.Total total = new TNFe.InfNFe.Total();
		TNFe.InfNFe.Total.ICMSTot icmsTot = new TNFe.InfNFe.Total.ICMSTot();

		icmsTot.setVProd(venda.getTotal().toString());
		icmsTot.setVNF(venda.getTotal().toString());

		icmsTot.setVBC("0.00");
		icmsTot.setVICMS("0.00");
		icmsTot.setVICMSDeson("0.00");
		icmsTot.setVFCP("0.00");

		icmsTot.setVBCST("0.00");
		icmsTot.setVST("0.00");
		icmsTot.setVFCPST("0.00");
		icmsTot.setVFCPSTRet("0.00");

		icmsTot.setVFrete("0.00");
		icmsTot.setVSeg("0.00");
		icmsTot.setVDesc("0.00");

		icmsTot.setVII("0.00");
		icmsTot.setVIPI("0.00");
		icmsTot.setVIPIDevol("0.00");

		icmsTot.setVPIS("0.00");
		icmsTot.setVCOFINS("0.00");
		icmsTot.setVOutro("0.00");

		total.setICMSTot(icmsTot);

		return total;
	}

	private TNFe.InfNFe.Transp montarTransporte() {

		TNFe.InfNFe.Transp transp = new TNFe.InfNFe.Transp();
		transp.setModFrete("9");

		return transp;
	}

	private TNFe.InfNFe.Pag montarPagamento(Venda venda) {

		TNFe.InfNFe.Pag pag = new TNFe.InfNFe.Pag();
		TNFe.InfNFe.Pag.DetPag det = new TNFe.InfNFe.Pag.DetPag();

		det.setTPag("01");
		det.setVPag(venda.getTotal().toString());

		pag.getDetPag().add(det);

		return pag;
	}

	private String gerarCodigoNumerico() {
		Random random = new Random();
		int numero = 10000000 + random.nextInt(90000000);
		return String.valueOf(numero);
	}

	private String gerarChaveNfe(String cUF, String data, String cnpj, String mod, String serie, String numero,
			String tpEmis, String cNF) {

		String chave43 = String.format("%s%s%s%s%03d%09d%s%s", cUF, data, cnpj, mod, Integer.parseInt(serie),
				Integer.parseInt(numero), tpEmis, cNF);

		int peso = 2;
		int soma = 0;

		for (int i = chave43.length() - 1; i >= 0; i--) {
			soma += Character.getNumericValue(chave43.charAt(i)) * peso;
			peso++;
			if (peso > 9)
				peso = 2;
		}

		int resto = soma % 11;
		int dv = (resto == 0 || resto == 1) ? 0 : 11 - resto;

		return chave43 + dv;
	}
}