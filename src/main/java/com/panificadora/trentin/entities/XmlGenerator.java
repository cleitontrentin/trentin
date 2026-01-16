package com.panificadora.trentin.entities;

import java.io.StringWriter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;

public class XmlGenerator {

    public static String gerarXml(TNFe nfe) throws Exception {
        JAXBContext context = JAXBContext.newInstance(TNFe.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        StringWriter writer = new StringWriter();
        marshaller.marshal(nfe, writer);
        return writer.toString();
    }
}