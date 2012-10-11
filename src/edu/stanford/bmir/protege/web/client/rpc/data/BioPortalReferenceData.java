package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * @author Csongor Nyulas
 */

public class BioPortalReferenceData implements Serializable {
    //configuration properties     //WE COULD MOVE THESE TO A SEPARATE RPC DATA CLASS (e.g. BioPortalReferenceConfigurationData)
	private boolean createAsClass; 
	private String referenceClassName;
	private String referencePropertyName;
    private String urlPropertyName;
    private String ontologyNamePropertyName;
    private String ontologyNameAltPropertyName;
    private String ontologyIdPropertyName;
    private String conceptIdPropertyName;
    private String conceptIdAltPropertyName;
    private String preferredLabelPropertyName;
	
	private String bpUrl;
	private String conceptId;
	private String conceptIdShort;
	private String ontologyVersionId;
	private String preferredName;
	private String ontologyName;
	
    private boolean importFromOriginalOntology; 
    private String bpRestBaseUrl;
    private String bpRestCallSuffix;

	
	@Override
	public String toString() {
		return "BioPortalReferenceData [bpUrl=" + bpUrl
				+ ", conceptId=" + conceptId + ", conceptIdShort="
				+ conceptIdShort + ", createAsClass="
				+ createAsClass + ", ontologyName=" + ontologyName
				+ ", ontologyVersionId=" + ontologyVersionId
				+ ", preferredName=" + preferredName + ", referenceClassName="
				+ referenceClassName + ", referencePropertyName="
				+ referencePropertyName + " urlPropertyName="
                + urlPropertyName + ", ontologyNamePropertyName="
                + ontologyNamePropertyName + ", ontologyIdPropertyName="
                + ontologyIdPropertyName + ", conceptIdPropertyName="
                + conceptIdPropertyName + ", preferredLabelPropertyName="
                + preferredLabelPropertyName + ", importFromOriginalOntology="
                + importFromOriginalOntology + ", bpRestBaseUrl="
                + bpRestBaseUrl  + ", bpRestCallSuffix=" + bpRestCallSuffix 
                + "]";
	}

	public boolean createAsClass() {
		return createAsClass;
	}
	public void setCreateAsClass(boolean createAsClass) {
		this.createAsClass = createAsClass;
	}
	public String getReferenceClassName() {
		return referenceClassName;
	}
	public void setReferenceClassName(String referenceClassName) {
		this.referenceClassName = referenceClassName;
	}
	public String getReferencePropertyName() {
		return referencePropertyName;
	}
	public void setReferencePropertyName(String referencePropertyName) {
		this.referencePropertyName = referencePropertyName;
	}
    public String getUrlPropertyName() {
        return urlPropertyName;
    }
    public void setUrlPropertyName(String urlPropertyName) {
        this.urlPropertyName = urlPropertyName;
    }
    public String getOntologyNamePropertyName() {
        return ontologyNamePropertyName;
    }
    public void setOntologyNamePropertyName(String ontologyNamePropertyName) {
        this.ontologyNamePropertyName = ontologyNamePropertyName;
    }
    public String getOntologyNameAltPropertyName() {
        return ontologyNameAltPropertyName;
    }
    public void setOntologyNameAltPropertyName(String ontologyNameAltPropertyName) {
        this.ontologyNameAltPropertyName = ontologyNameAltPropertyName;
    }
    public String getOntologyIdPropertyName() {
        return ontologyIdPropertyName;
    }
    public void setOntologyIdPropertyName(String ontologyIdPropertyName) {
        this.ontologyIdPropertyName = ontologyIdPropertyName;
    }
    public String getConceptIdPropertyName() {
        return conceptIdPropertyName;
    }
    public void setConceptIdPropertyName(String conceptIdPropertyName) {
        this.conceptIdPropertyName = conceptIdPropertyName;
    }
    public String getConceptIdAltPropertyName() {
        return conceptIdAltPropertyName;
    }
    public void setConceptIdAltPropertyName(String conceptIdAltPropertyName) {
        this.conceptIdAltPropertyName = conceptIdAltPropertyName;
    }
    public String getPreferredLabelPropertyName() {
        return preferredLabelPropertyName;
    }
    public void setPreferredLabelPropertyName(String preferredLabelPropertyName) {
        this.preferredLabelPropertyName = preferredLabelPropertyName;
    }
	public String getBpUrl() {
		return bpUrl;
	}
	public void setBpUrl(String bpBaseUrl) {
		this.bpUrl = bpBaseUrl;
	}
	public String getConceptId() {
		return conceptId;
	}
	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}
	public String getConceptIdShort() {
		return conceptIdShort;
	}
	public void setConceptIdShort(String conceptIdShort) {
		this.conceptIdShort = conceptIdShort;
	}
	public String getOntologyVersionId() {
		return ontologyVersionId;
	}
	public void setOntologyVersionId(String ontologyVersionId) {
		this.ontologyVersionId = ontologyVersionId;
	}
	public String getPreferredName() {
		return preferredName;
	}
	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}
	public String getOntologyName() {
		return ontologyName;
	}
	public void setOntologyName(String ontologyName) {
		this.ontologyName = ontologyName;
	}
    public boolean importFromOriginalOntology() {
        return importFromOriginalOntology;
    }
    public void setImportFromOriginalOntology(boolean importFromOriginalOntology) {
        this.importFromOriginalOntology = importFromOriginalOntology;
    }
    public String getBpRestBaseUrl() {
        return bpRestBaseUrl;
    }
    public void setBpRestBaseUrl(String bpRestBaseUrl) {
        this.bpRestBaseUrl = bpRestBaseUrl;
    }
    public String getBpRestCallSuffix() {
        return bpRestCallSuffix;
    }
    public void setBpRestCallSuffix(String bpRestCallSuffix) {
        this.bpRestCallSuffix = bpRestCallSuffix;
    }
	
}
