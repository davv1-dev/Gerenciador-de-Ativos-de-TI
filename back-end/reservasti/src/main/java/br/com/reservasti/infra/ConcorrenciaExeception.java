package br.com.reservasti.infra;

public class ConcorrenciaExeception extends RuntimeException {
    public ConcorrenciaExeception(String message) {
        super(message);
    }
}
