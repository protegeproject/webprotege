package edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.imports.exceptions;

import java.io.Serializable;

public class DetailsNotFoundException extends Exception implements Serializable {

	private static final long serialVersionUID = 2362549709642740697L;

	public DetailsNotFoundException() {}
	
	public DetailsNotFoundException(String message) {
		super(message);
	}
	
}
