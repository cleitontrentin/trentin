package com.panificadora.trentin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.AmbienteEnum;
import br.com.swconsultoria.nfe.dom.enuns.EstadosEnum;

@Service
public class NfeConfigService {

    @Value("${nfe.estado}")
    private String estado;

    @Value("${nfe.ambiente}")
    private String ambiente;

    @Value("${nfe.certificado.caminho}")
    private String caminhoCertificado;

    @Value("${nfe.certificado.senha}")
    private String senhaCertificado;

    public ConfiguracoesNfe getConfiguracoesNfe() throws Exception {

        Certificado certificado = CertificadoService.certificadoPfx(
                caminhoCertificado,
                senhaCertificado
        );

        // ASSINATURA CORRETA DA 4.00.48
        return ConfiguracoesNfe.criarConfiguracoes(
                EstadosEnum.valueOf(estado),
                AmbienteEnum.valueOf(ambiente),
                certificado,
                "America/Sao_Paulo"
        );
    }
}
