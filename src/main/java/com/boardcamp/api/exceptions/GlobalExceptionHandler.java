package com.boardcamp.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ GameNameConflictException.class })
    public ResponseEntity<Object> handlerGameConflict(GameNameConflictException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }   

    @ExceptionHandler({ GameNotFoundException.class })
    public ResponseEntity<Object> handlerGameNotFound(GameNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    } 

    @ExceptionHandler({ CustomerCpfConflictException.class })
    public ResponseEntity<Object> handlerCustomerConflict(CustomerCpfConflictException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    } 

    @ExceptionHandler({ CustomerNotFoundException.class })
    public ResponseEntity<Object> handlerCustomerNotFound(CustomerNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    } 

    @ExceptionHandler({ RentalNotFoundException.class })
    public ResponseEntity<Object> handlerRentalNotFound(RentalNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    } 

    @ExceptionHandler({ RentalUnprocessableEntityException.class })
    public ResponseEntity<Object> handlerRentalUnprocessableEntity(RentalUnprocessableEntityException exception){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    } 
}
