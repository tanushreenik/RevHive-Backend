package com.project.revhive.demo.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
		Map<String, Object> body = new HashMap<>();
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		return ResponseEntity.status(status).body(body);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.orElse("Validation failed");
		return buildError(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
		String message = ex.getMessage() == null ? "Unexpected error" : ex.getMessage();

		if (message.contains("already exists") || message.contains("already present")) {
			return buildError(HttpStatus.CONFLICT, message);
		}
		if (message.contains("not found")) {
			return buildError(HttpStatus.NOT_FOUND, message);
		}
		if (message.contains("Invalid password") || message.contains("Unauthorized")) {
			return buildError(HttpStatus.UNAUTHORIZED, message);
		}
		if (message.contains("Token generation failed")) {
			return buildError(HttpStatus.INTERNAL_SERVER_ERROR, message);
		}

		return buildError(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
		return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
				ex.getMessage() == null ? "Something went wrong" : ex.getMessage());
	}
}

