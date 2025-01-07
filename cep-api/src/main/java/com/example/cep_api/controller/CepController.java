package com.example.cep_api.controller;

import com.example.cep_api.exception.CepNaoEncontradoException;
import com.example.cep_api.exception.ServicoIndisponivelException;
import com.example.cep_api.model.Cep;
import com.example.cep_api.service.CepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
public class CepController {

    private final CepService cepService;
    private static final Logger log = LoggerFactory.getLogger(CepController.class);

    @Autowired
    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    @GetMapping("/{cep}")
    public ResponseEntity<Object> getCep(@PathVariable final String cep) {
        try {
            if (!isCepValido(cep)) {
                return ResponseEntity.badRequest().body("CEP inválido");
            }

            log.info("Recebida requisição para o CEP: {}", cep);
            Cep cepResponse = cepService.consultarCep(cep);
            log.info("CEP encontrado com sucesso: {}", cepResponse);
            return ResponseEntity.ok(cepResponse);
        } catch (CepNaoEncontradoException e) {
            log.info("CEP não encontrado: {}", cep);
            return ResponseEntity.notFound().build();
        } catch (ServicoIndisponivelException e) {
            log.error("Serviço de CEP indisponível: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Serviço de CEP temporariamente indisponível");
        } catch (Exception e) {
            log.error("Erro inesperado ao consultar CEP: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno no servidor");
        }
    }

    private boolean isCepValido(String cep) {
        return cep.matches("\\d{8}");
    }
}
