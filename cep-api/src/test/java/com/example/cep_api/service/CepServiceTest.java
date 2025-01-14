package com.example.cep_api.service;

import com.example.cep_api.model.Cep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CepServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private LogConsultaService logConsultaService;

    @InjectMocks
    private CepService cepService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarCepQuandoSucesso() {
        String cep = "01001000";
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        Cep mockCep = new Cep();
        mockCep.setCep("01001-000");
        mockCep.setLogradouro("Praça da Sé");
        mockCep.setBairro("Sé");
        mockCep.setLocalidade("São Paulo");
        mockCep.setEstado("SP");

        when(restTemplate.getForEntity(url, Cep.class))
                .thenReturn(new ResponseEntity<>(mockCep, HttpStatus.OK));

        Cep result = cepService.consultarCep(cep);

        assertNotNull(result);
        assertEquals(mockCep.getCep(), result.getCep());
        verify(logConsultaService, times(1)).salvarLog(
                mockCep.getCep(),
                mockCep.getLogradouro(),
                mockCep.getBairro(),
                mockCep.getLocalidade(),
                mockCep.getEstado()
        );
    }

    @Test
    void deveLancarExcecaoQuandoCepInvalido() {
        String cep = "00000000";
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        when(restTemplate.getForEntity(url, Cep.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> cepService.consultarCep(cep));

        assertEquals("Não foi possível encontrar esse CEP.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoErroDeRestClient() {
        String cep = "01001000";
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        when(restTemplate.getForEntity(url, Cep.class))
                .thenThrow(new RestClientException("Erro de conexão"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cepService.consultarCep(cep));

        assertTrue(exception.getMessage().contains("Erro ao consultar CEP"));
    }
}
