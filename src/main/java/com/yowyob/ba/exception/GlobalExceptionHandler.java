package com.yowyob.ba.exception;

import com.yowyob.ba.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


     // Gère les exceptions de type RuntimeException (erreurs génériques).
    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleRuntimeException(RuntimeException ex, ServerWebExchange exchange) {
        log.error("Erreur non gérée : ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne du serveur", exchange);
    }


     //Gère les exceptions de statut Spring (ex: 404 Not Found via Mono.error).
    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleResponseStatusException(ResponseStatusException ex, ServerWebExchange exchange) {
        return buildErrorResponse((HttpStatus) ex.getStatusCode(), ex.getReason(), exchange);
    }


    //  Gère les erreurs de validation des @RequestBody.
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(WebExchangeBindException ex, ServerWebExchange exchange) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation échouée : " + details, exchange);
    }

    private Mono<ResponseEntity<ErrorResponse>> buildErrorResponse(HttpStatus status, String message, ServerWebExchange exchange) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(exchange.getRequest().getPath().value())
                .build();

        return Mono.just(ResponseEntity.status(status).body(response));
    }
}