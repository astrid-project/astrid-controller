package it.polito.astrid.controllers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.springframework.web.client.HttpStatusCodeException;

public class SystemException extends Throwable implements
ExceptionMapper<Throwable> {

public SystemException(String string, String responseBodyAsString, HttpStatusCodeException ex) {
		// TODO Auto-generated constructor stub
	}

public SystemException(String string, RuntimeException ex) {
	// TODO Auto-generated constructor stub
}

public SystemException(String string, Exception ex) {
	// TODO Auto-generated constructor stub
}

@Override
public Response toResponse(Throwable ex) {
	return null;
}
}