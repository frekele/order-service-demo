package br.com.demo.domain.exception;

public class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super(message);
    }
}