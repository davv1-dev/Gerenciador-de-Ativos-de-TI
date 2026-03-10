package br.com.reservasti.infra;

import br.com.reservasti.infra.exceptions.ConcorrenciaException;
import br.com.reservasti.infra.exceptions.IdNaoEncontradoException;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class TratadorDeErros{

    @ExceptionHandler(IdNaoEncontradoException.class)
    public ResponseEntity<ErroPadraoDTO> tratarErro404(IdNaoEncontradoException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ErroPadraoDTO> tratarErroDeNegocio(ValidacaoException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Violação de Regra de Negócio",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(ConcorrenciaException.class)
    public ResponseEntity<ErroPadraoDTO> tratarErroDeConcorrencia(ConcorrenciaException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflito de Estado/Concorrência",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroValidacaoCamposDTO>> tratarErro400Anotacoes(MethodArgumentNotValidException ex) {
        List<ErroValidacaoCamposDTO> erros = ex.getFieldErrors().stream()
                .map(ErroValidacaoCamposDTO::new)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroPadraoDTO> tratarErro500(Exception ex, HttpServletRequest request) {

        String protocoloErro = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        System.err.println("\n=== [ERRO INTERNO - PROTOCOLO: " + protocoloErro + "] ===");
        System.err.println("Motivo real: " + ex.getMessage());
        ex.printStackTrace();
        System.err.println("-------------------------------------------------\n");

        ErroPadraoDTO erro = new ErroPadraoDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno do Servidor",
                "Poxa, algo deu errado do nosso lado! Por favor, contate o suporte informando o protocolo: " + protocoloErro,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> tratarErroDeConcorrencia(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Conflito de concorrência: Outro técnico já assumiu ou alterou este chamado. Por favor, atualize a página.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> tratarErroDeChaveUnica(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Erro: Já existe um registro cadastrado com esta informação única (ex: e-mail, CPF ou nome).");
    }

    private record ErroPadraoDTO(
            LocalDateTime timestamp,
            Integer status,
            String erro,
            String mensagem,
            String path
    ) {}

    public record ErroValidacaoCamposDTO(String campo, String mensagem) {
        public ErroValidacaoCamposDTO(org.springframework.validation.FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}