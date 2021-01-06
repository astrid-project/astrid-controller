package it.polito.astrid.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AstridComponentNotFoundException extends Exception{
 private static final long serialVersionUID = 1L;
 public AstridComponentNotFoundException(String message){
     super(message);
    }
}