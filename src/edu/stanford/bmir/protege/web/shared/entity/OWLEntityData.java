package edu.stanford.bmir.protege.web.shared.entity;

import org.semanticweb.owlapi.model.OWLEntity;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public abstract class OWLEntityData extends OWLPrimitiveData implements Comparable<OWLEntityData> {

    private final String browserText;


    public OWLEntityData(OWLEntity entity, String browserText) {
        super(entity);
        this.browserText = browserText;
    }

    public OWLEntity getEntity() {
        return (OWLEntity) getObject();
    }

    public boolean isIRIEmpty() {
        return getEntity().getIRI().length() == 0;
    }

    @Override
    public String getBrowserText() {
        return browserText;
    }

    @Override
    public int compareTo(OWLEntityData o) {
        return this.browserText.compareTo(o.getBrowserText());
    }

    public int compareToIgnorePrefixNames(OWLEntityData other) {
        int prefixSepIndex = getPrefixSeparatorIndex();
        String comparisonString = browserText.substring(prefixSepIndex != -1 ? prefixSepIndex : 0);
        int otherPrefixSepIndex = other.getPrefixSeparatorIndex();
        String otherComparisonString = other.browserText.substring(otherPrefixSepIndex != -1 ? otherPrefixSepIndex : 0);
        return comparisonString.compareToIgnoreCase(otherComparisonString);
    }

    public int compareToIgnoreCase(OWLEntityData other) {
        return browserText.compareToIgnoreCase(other.browserText);
    }

    public int getPrefixSeparatorIndex() {
        return browserText.indexOf(':');
    }

    @Override
    public String getUnquotedBrowserText() {
        String browserText = getBrowserText();
        if(browserText.startsWith("'") && browserText.endsWith("'")) {
            return browserText.substring(1, browserText.length() - 1);
        }
        else {
            return browserText;
        }
    }

}
