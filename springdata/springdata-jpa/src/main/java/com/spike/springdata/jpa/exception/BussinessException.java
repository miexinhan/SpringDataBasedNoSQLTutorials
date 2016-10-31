package com.spike.springdata.jpa.exception;

public class BussinessException extends Exception {
  private static final long serialVersionUID = 9043782295716360818L;

  public BussinessException(String message) {
    super(message);
  }

  public BussinessException(String message, Throwable cause) {
    super(message, cause);
  }
}
