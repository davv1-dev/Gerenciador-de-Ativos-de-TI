package br.com.reservasti.infra.exceptions;

public class ConcorrenciaException extends RuntimeException {
    public ConcorrenciaException(String message) {
        super(message);
    }
}
