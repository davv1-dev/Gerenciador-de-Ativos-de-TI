package br.com.reservasti.infra;

public class IdNaoEncontradoExeception extends RuntimeException {
    public IdNaoEncontradoExeception(String message) {
        super(message);
    }
}
