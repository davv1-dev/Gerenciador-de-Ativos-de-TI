package br.com.reservasti.infra;

import br.com.reservasti.domain.helpdesk.ChamadoService;
import br.com.reservasti.infra.exceptions.ConcorrenciaException;
import br.com.reservasti.infra.exceptions.IdNaoEncontradoException;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TratadorDeErrosTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChamadoService chamadoService;

    @Test
    @DisplayName("Deve retornar HTTP 404 e JSON padronizado ao lançar IdNaoEncontradoException")
    void testTratadorIdNaoEncontrado() throws Exception {

        Long idInexistente = 999L;
        Mockito.when(chamadoService.resolverChamado(idInexistente))
                .thenThrow(new IdNaoEncontradoException("Chamado com ID " + idInexistente + " não existe."));

        mockMvc.perform(patch("/chamados/{id}/resolver", idInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.mensagem").value("Chamado com ID 999 não existe."))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.path").value("/chamados/999/resolver"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 400 e JSON padronizado ao lançar ValidacaoException")
    void testTratadorValidacaoRegraNegocio() throws Exception {
        Long idChamado = 1L;

        Mockito.when(chamadoService.cancelarChamado(idChamado))
                .thenThrow(new ValidacaoException("Apenas o solicitante pode cancelar este chamado."));

        mockMvc.perform(delete("/chamados/{id}", idChamado))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Violação de Regra de Negócio"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 409 e JSON padronizado ao lançar ConcorrenciaException")
    void testTratadorConcorrencia() throws Exception {
        Long idChamado = 1L;
        Long idTecnico = 2L;

        Mockito.when(chamadoService.assumirChamado(idChamado, idTecnico))
                .thenThrow(new ConcorrenciaException("Este chamado já foi assumido por outro técnico."));

        mockMvc.perform(patch("/chamados/{idChamado}/assumir", idChamado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(idTecnico)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.erro").value("Conflito de Estado/Concorrência"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 500, ocultar o stacktrace e devolver o protocolo ao lançar Exception Genérica")
    void testTratadorErroInterno500() throws Exception {

        Mockito.when(chamadoService.listarFilaGlobal())
                .thenThrow(new RuntimeException("Banco de dados fora do ar / NullPointer inesperado"));

        mockMvc.perform(get("/chamados/fila-global"))
                .andExpect(status().isInternalServerError()) // Verifica o HTTP 500
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.erro").value("Erro Interno do Servidor"))
                .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("informando o protocolo")));
    }
}
