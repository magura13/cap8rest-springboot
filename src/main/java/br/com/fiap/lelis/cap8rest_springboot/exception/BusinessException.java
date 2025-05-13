package br.com.fiap.lelis.cap8rest_springboot.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) { super(message); }
}