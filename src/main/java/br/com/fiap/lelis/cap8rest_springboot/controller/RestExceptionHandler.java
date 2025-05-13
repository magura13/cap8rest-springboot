package br.com.fiap.lelis.cap8rest_springboot.controller;

import br.com.fiap.lelis.cap8rest_springboot.exception.BusinessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class RestExceptionHandler {

    private record ErrorDTO(String message) {}

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusiness(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("Please review your bodyRequest"));
    }
}