package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class AnnotationData implements Serializable {
	private String name;
	private String value;
	private String lang;
	
	public AnnotationData() {
	}

	public AnnotationData(String name) {
		this.name = name;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
