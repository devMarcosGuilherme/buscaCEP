package com.example.cep_api.logging;

import com.example.cep_api.repository.LogConsultaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogConsultaService {
    private final LogConsultaRepository logConsultaRepository;

    private static final Logger logger = LoggerFactory.getLogger(LogConsultaService.class);

    @Autowired
    public LogConsultaService(LogConsultaRepository logConsultaRepository) {
        this.logConsultaRepository = logConsultaRepository;
    }

    public void salvarLog(final String cep, final String logradouro, final String bairro, final String cidade, final String estado) {
        if (logradouro == null && bairro == null && cidade == null && estado == null) {
            throw new IllegalArgumentException("N�o foi poss�vel encontrar esse CEP.");
        }

        LogConsulta log = new LogConsulta();
        log.setCep(cep);
        log.setLogradouro(logradouro);
        log.setBairro(bairro);
        log.setCidade(cidade);
        log.setEstado(estado);
        log.setHorarioConsulta(LocalDateTime.now());
        logConsultaRepository.save(log);
        logger.info("Log salvo com sucesso para o CEP: {}", cep);
    }
}
