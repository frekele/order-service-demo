package br.com.demo.infrastructure.config.handler;

import br.com.demo.domain.exception.DomainException;
import br.com.demo.domain.exception.NotFoundException;
import br.com.demo.infrastructure.openapi.order.model.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    private static final String REQUEST_URI = "/test/path";
    private static final String REQUEST_URI_WITH_PREFIX = "uri=" + REQUEST_URI;

    @BeforeEach
    void setUp() {
        when(webRequest.getDescription(false)).thenReturn(REQUEST_URI_WITH_PREFIX);
    }

    @Test
    void handleNotFoundException_shouldReturnNotFound() {
        // Arrange
        String errorMessage = "Resource not found";
        var ex = new NotFoundException(errorMessage);

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleNotFoundException(ex, webRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorResponse.getError());
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(REQUEST_URI, errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleDomainException_shouldReturnConflict() {
        String errorMessage = "Domain rule violation";
        var ex = new DomainException(errorMessage);

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleDomainException(ex, webRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.CONFLICT.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.CONFLICT.getReasonPhrase(), errorResponse.getError());
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(REQUEST_URI, errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleGlobalException_shouldReturnInternalServerError() {
        var ex = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleGlobalException(ex, webRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponse.getError());
        assertEquals("An unexpected internal server error has occurred.", errorResponse.getMessage());
        assertEquals(REQUEST_URI, errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequest() {
        var fieldError1 = new FieldError("objectName", "field1", "Error message 1");
        var fieldError2 = new FieldError("objectName", "field2", "Error message 2");
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        String expectedErrorMessage = "Error message 1, Error message 2";

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleValidationExceptions(methodArgumentNotValidException, webRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponse.getError());
        assertEquals(expectedErrorMessage, errorResponse.getMessage());
        assertEquals(REQUEST_URI, errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }
}