package com.example.demo.exception;

public class ArchivoNoValidoException extends RuntimeException {
    public ArchivoNoValidoException(String mensaje) {
        super(mensaje);
    }
}