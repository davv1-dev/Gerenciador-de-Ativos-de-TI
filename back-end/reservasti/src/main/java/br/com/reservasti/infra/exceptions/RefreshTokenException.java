package br.com.reservasti.infra.exceptions;

public class RefreshTokenNaoExisteException extends RuntimeException {
  public RefreshTokenNaoExisteException(String message) {
    super(message);
  }
}
