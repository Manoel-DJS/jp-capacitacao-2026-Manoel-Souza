package jp_2026_Manoel_Souza.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<ApiError> handleProdutoNaoEncontrado(ProdutoNaoEncontradoException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiError> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNoSuchElement(NoSuchElementException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Recurso não encontrado.");
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ApiError> handleRegraDeNegocio(RegraDeNegocioException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicidadeException.class)
    public ResponseEntity<ApiError> handleDuplicidade(DuplicidadeException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<ApiError> handleEstoqueInsuficiente(EstoqueInsuficienteException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
