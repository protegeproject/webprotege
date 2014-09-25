package edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.imports;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author marcosmr
 * 
 */
public class BioPortalResultBean implements IsSerializable {

	private String id;
	private String shortId;
	private String idSelf;
	private String preferredName;
	private String uriBioPortal;
	private String ontologyId;
	private String ontologyAcronym;
	private String ontologyName;
	private String ontologyUriBioPortal;
	private String matchingField;

	/**
	 * Empty constructor for serialization purposes only.
	 */
	public BioPortalResultBean() {
	}

	public BioPortalResultBean(String id, String shortId, String idSelf,
			String preferredName, String uriBioPortal, String ontologyId,
			String ontologyAcronym, String ontologyName,
			String ontologyUriBioPortal, String matchingField) {
		this.id = id;
		this.shortId = shortId;
		this.idSelf = idSelf;
		this.preferredName = preferredName;
		this.uriBioPortal = uriBioPortal;
		this.ontologyId = ontologyId;
		this.ontologyAcronym = ontologyAcronym;
		this.ontologyName = ontologyName;
		this.ontologyUriBioPortal = ontologyUriBioPortal;
		this.matchingField = matchingField;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShortId() {
		return shortId;
	}

	public void setShortId(String shortId) {
		this.shortId = shortId;
	}

	public String getIdSelf() {
		return idSelf;
	}

	public void setIdSelf(String idSelf) {
		this.idSelf = idSelf;
	}

	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public String getUriBioPortal() {
		return uriBioPortal;
	}

	public void setUriBioPortal(String uriBioPortal) {
		this.uriBioPortal = uriBioPortal;
	}

	public String getOntologyId() {
		return ontologyId;
	}

	public void setOntologyId(String ontologyId) {
		this.ontologyId = ontologyId;
	}

	public String getOntologyAcronym() {
		return ontologyAcronym;
	}

	public void setOntologyAcronym(String ontologyAcronym) {
		this.ontologyAcronym = ontologyAcronym;
	}

	public String getOntologyName() {
		return ontologyName;
	}

	public void setOntologyName(String ontologyName) {
		this.ontologyName = ontologyName;
	}

	public String getOntologyUriBioPortal() {
		return ontologyUriBioPortal;
	}

	public void setOntologyUriBioPortal(String ontologyUriBioPortal) {
		this.ontologyUriBioPortal = ontologyUriBioPortal;
	}

	public String getMatchingField() {
		return matchingField;
	}

	public void setMatchingField(String matchingField) {
		this.matchingField = matchingField;
	}

}
