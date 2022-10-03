package com.company.exceptions;

public class CsvFormatException extends RuntimeException {
  public CsvFormatException(String message) {
    super(message);
  }
}
