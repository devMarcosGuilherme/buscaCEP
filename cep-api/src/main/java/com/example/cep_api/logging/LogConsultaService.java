package com.example.cep_api.logging;

import com.example.cep_api.repository.LogConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogConsultaService {
    private final LogConsultaRepository logConsultaRepository;

    @Autowired
    public LogConsultaService(LogConsultaRepository logConsultaRepository) {
        this.logConsultaRepository = logConsultaRepository;
    }

    public void salvarLog(final String cep, final String dadosRetornados) {
        LogConsulta log = new LogConsulta();
        log.setCep(cep);
        log.setDadosRetornados(dadosRetornados);
        log.setHorarioConsulta(LocalDateTime.now());
        logConsultaRepository.save(log);
    }
}
