package br.com.reservasti.infra.exceptions;

public class AlteracaoDeSenhaException extends RuntimeException {
  public AlteracaoDeSenhaException(String message) {
    super(message);
  }
}
