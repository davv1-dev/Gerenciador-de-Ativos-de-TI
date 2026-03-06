package br.com.reservasti.infra;

public class ValidationExeception extends RuntimeException {
    public ValidationExeception(String message) {
        super(message);
    }
}
