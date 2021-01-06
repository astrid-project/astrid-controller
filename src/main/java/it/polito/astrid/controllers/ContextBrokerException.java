package it.polito.astrid.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
public class ContextBrokerException extends Exception{
	
	 private static final long serialVersionUID = 1L;
	 
	 public ContextBrokerException(String message){
	     super(message);
	 }
}
