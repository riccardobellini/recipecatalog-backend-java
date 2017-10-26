package com.bellini.recipecatalog.handler;

import com.bellini.recipecatalog.exception.dishtype.DuplicateDishTypeException;
import com.bellini.recipecatalog.exception.dishtype.NotExistingDishTypeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RecipeCatalogExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DuplicateDishTypeException.class})
    public ResponseEntity<Object> handleDuplicate(RuntimeException ex, WebRequest req) {
        String msg = ex.getMessage();
        return handleExceptionInternal(ex, msg, new HttpHeaders(), HttpStatus.CONFLICT, req);
    }

    @ExceptionHandler(value = {NotExistingDishTypeException.class})
    public ResponseEntity<Object> handleNotExisting(RuntimeException ex, WebRequest req) {
        String msg = ex.getMessage();
        return handleExceptionInternal(ex, msg, new HttpHeaders(), HttpStatus.NOT_FOUND, req);
    }

}
