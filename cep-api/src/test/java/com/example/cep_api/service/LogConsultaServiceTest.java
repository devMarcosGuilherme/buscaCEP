package com.example.cep_api.service;

import com.example.cep_api.logging.LogConsulta;
import com.example.cep_api.repository.LogConsultaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class LogConsultaServiceTest {

    @Mock
    private LogConsultaRepository logConsultaRepository;

    private LogConsultaService logConsultaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logConsultaService = new LogConsultaService(logConsultaRepository);
    }

    @Test
    void deveSalvarLogComDadosValidos() {
        // Arrange
        String cep = "12345-678";
        String logradouro = "Rua Exemplo";
        String bairro = "Bairro Teste";
        String localidade = "Cidade Exemplo";
        String estado = "SP";

        // Act
        logConsultaService.salvarLog(cep, logradouro, bairro, localidade, estado);

        // Assert
        ArgumentCaptor<LogConsulta> logCaptor = ArgumentCaptor.forClass(LogConsulta.class);
        verify(logConsultaRepository, times(1)).save(logCaptor.capture());

        LogConsulta logSalvo = logCaptor.getValue();
        assertEquals(cep, logSalvo.getCep());
        assertEquals(logradouro, logSalvo.getLogradouro());
        assertEquals(bairro, logSalvo.getBairro());
        assertEquals(localidade, logSalvo.getlocalidade());
        assertEquals(estado, logSalvo.getEstado());
        assertNotNull(logSalvo.getHorarioConsulta());
    }

    @Test
    void deveLancarExcecaoParaDadosInvalidos() {
        // Arrange
        String cep = "12345-678";
        String logradouro = null;
        String bairro = null;
        String localidade = null;
        String estado = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                logConsultaService.salvarLog(cep, logradouro, bairro, localidade, estado)
        );
        assertEquals("Não foi possível encontrar esse CEP.", exception.getMessage());
        verify(logConsultaRepository, never()).save(any(LogConsulta.class));
    }
}
