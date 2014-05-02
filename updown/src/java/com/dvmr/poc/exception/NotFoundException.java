package com.dvmr.poc.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;



/**
 * 
 * @author vreddy.fp
 *
 */
public class NotFoundException extends WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(String message) {
		super();
	}

}
