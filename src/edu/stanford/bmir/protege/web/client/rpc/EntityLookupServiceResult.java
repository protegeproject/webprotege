package edu.stanford.bmir.protege.web.client.rpc;

import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class EntityLookupServiceResult implements Serializable, Comparable<EntityLookupServiceResult> {

    private OWLEntityData visualEntity;
    
    private int browserTextMatchStart;
    
    private int browserTextMatchEnd;

    private EntityLookupServiceResult() {
    }

    public EntityLookupServiceResult(OWLEntityData visualEntity, int browserTextMatchStart, int browserTextMatchEnd) {
        this.visualEntity = visualEntity;
        this.browserTextMatchStart = browserTextMatchStart;
        this.browserTextMatchEnd = browserTextMatchEnd;
    }

    public OWLEntityData getOWLEntityData() {
        return visualEntity;
    }

    public int getBrowserTextMatchStart() {
        return browserTextMatchStart;
    }

    public int getBrowserTextMatchEnd() {
        return browserTextMatchEnd;
    }
    
    public String getDisplayText() {
        StringBuilder sb = new StringBuilder();
        String browserText = visualEntity.getBrowserText();
        if (browserTextMatchStart < browserText.length() && browserTextMatchEnd <= browserText.length()) {
            sb.append(browserText.substring(0, browserTextMatchStart));
            sb.append("<span class=\"web-protege-entity-match-substring\">");
            sb.append(browserText.substring(browserTextMatchStart, browserTextMatchEnd));
            sb.append("</span>");
            sb.append(browserText.substring(browserTextMatchEnd));
        }
        else {
            sb.append(browserText);
        }
        return sb.toString();
    }

    @Override
    public int compareTo(EntityLookupServiceResult other) {

        boolean probablyWordStart = isProbablyWordStart(browserTextMatchStart);
        boolean otherProbablyWordStart = other.isProbablyWordStart(other.browserTextMatchStart);
        if(probablyWordStart) {
            if(!otherProbablyWordStart) {
                return -1;
            }
        }
        else if(otherProbablyWordStart) {
            return 1;
        }

        // TODO: CASE

        if(!hasPrefixSeparator()) {
            if(other.hasPrefixSeparator()) {
                return -1;
            }
        }
        else if(!other.hasPrefixSeparator()) {
            return 1;
        }

        boolean matchInLocalName = isMatchInLocalName();
        boolean otherMatchInLocalName = other.isMatchInLocalName();

        if(matchInLocalName) {
            if(!otherMatchInLocalName) {
                return -1;
            }
        }
        else if(otherMatchInLocalName) {
            return 1;
        }


        int thisOffsetMatchStart = getOffsetMatchStart();
        int otherOffsetMatchStart = other.getOffsetMatchStart();

        if(thisOffsetMatchStart < otherOffsetMatchStart) {
            return -1;
        }
        else if(thisOffsetMatchStart > otherOffsetMatchStart) {
            return 1;
        }
        return visualEntity.compareToIgnorePrefixNames(other.visualEntity);
    }

    private boolean hasPrefixSeparator() {
        return getPrefixSeparatorIndex() != -1;
    }

    private boolean isMatchInLocalName() {
        int prefixSeparatorIndex = getPrefixSeparatorIndex();
        // Either no prefix separator or the match is after the prefix separator
        return prefixSeparatorIndex == -1 || browserTextMatchStart > prefixSeparatorIndex;
    }

    private boolean isProbablyWordStart(int index) {
        if(index < 0) {
            return false;
        }
        if(index == 0) {
            return true;
        }
        String browserText = visualEntity.getBrowserText();
        if(index >= browserText.length()) {
            // TODO: Fix
            return false;
        }
        char previousChar = browserText.charAt(index - 1);
        if(previousChar == ':') {
            return true;
        }
        if(previousChar == '_') {
            return true;
        }
        if(previousChar == ' ') {
            return true;
        }
        if(previousChar == '\'') {
            return true;
        }
        if(Character.isUpperCase(browserText.charAt(index))) {
            return true;
        }
        return false;

    }

    private int getOffsetMatchStart() {
        int prefixSeparatorIndex = getPrefixSeparatorIndex();
        final int offsetMatchStart;
        if(prefixSeparatorIndex != -1 && prefixSeparatorIndex < browserTextMatchStart) {
            offsetMatchStart = browserTextMatchStart - prefixSeparatorIndex;
        }
        else {
            offsetMatchStart = browserTextMatchStart;
        }
        return offsetMatchStart;
    }


    private int getPrefixSeparatorIndex() {
        return visualEntity.getBrowserText().indexOf(':');
    }
}
