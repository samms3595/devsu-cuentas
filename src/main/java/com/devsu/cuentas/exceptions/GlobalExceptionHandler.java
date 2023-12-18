package com.devsu.cuentas.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    private static final Map<String, String> SQL_STATE_MESSAGES = Map.of(
        "23000", "Violación de restricción de integridad. Unique Key",
        "23505", "Error: Violación de restricción única.",
        "23502", "Error: Violación de campo obligatorio no nulo.",
        "23503", "Error: Violación de restricción de clave foránea.",
        "22001", "Error: Datos demasiado largos para una columna.",
        "42000", "Error de sintaxis o violación de regla de acceso.",
        "08003", "La conexión con la base de datos no existe.",
        "08004", "La conexión con la base de datos ha sido rechazada.",
        "08007", "Estado desconocido de la transacción.",
        "40001","Error de serialización en la transacción.");

    record ErrorResponse(String mensaje, LocalDateTime timestamp) {}

    @ExceptionHandler(CustomHandlerException.class)
    public ResponseEntity<ErrorResponse> handlerCustomExceptions(CustomHandlerException e) {
        var response = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var mensaje = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Error de validación");
        var response = new ErrorResponse(mensaje, LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        var mensaje = "Error en la base de datos.";
        if (e.getRootCause() instanceof SQLException sqlEx) {
            mensaje = SQL_STATE_MESSAGES.getOrDefault(sqlEx.getSQLState(), mensaje);
        }
        var response = new ErrorResponse(mensaje, LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
