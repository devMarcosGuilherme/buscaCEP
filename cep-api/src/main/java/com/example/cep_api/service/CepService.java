package com.example.cep_api.service;

import com.example.cep_api.logging.LogConsultaService;
import com.example.cep_api.model.Cep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class CepService {
    private final RestTemplate restTemplate;
    private final LogConsultaService logConsultaService;

    private static final Logger log = LoggerFactory.getLogger(CepService.class);

    @Autowired
    public CepService(RestTemplate restTemplate, LogConsultaService logConsultaService) {
        this.restTemplate = restTemplate;
        this.logConsultaService = logConsultaService;
    }

    public Cep consultarCep(String cep) {
        final String url = "https://viacep.com.br/ws/" + cep + "/json/";
        try {
            log.debug("Realizando requisição para o CEP: {}", cep);
            ResponseEntity<Cep> response = restTemplate.getForEntity(url, Cep.class);
            log.debug("Resposta do serviço: {}", response.getBody());

            Cep cepResponse = response.getBody();
            if (cepResponse != null) {
                logConsultaService.salvarLog(cepResponse.getCep(), cepResponse.getLogradouro(), cepResponse.getBairro(), cepResponse.getlocalidade(), cepResponse.getEstado());
            } else {
                throw new IllegalArgumentException("Não foi possível encontrar esse CEP.");
            }

            return cepResponse;
        } catch (IllegalArgumentException e) {
            log.error("Erro de validação: {}", e.getMessage());
            throw e;
        } catch (RestClientException e) {
            log.error("Erro ao consultar CEP: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao consultar CEP: " + e.getMessage());
        }
    }
}