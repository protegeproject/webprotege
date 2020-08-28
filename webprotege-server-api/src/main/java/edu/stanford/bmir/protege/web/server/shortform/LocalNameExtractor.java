package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Charsets;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.net.URLDecoder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Mar 2018
 */
public class LocalNameExtractor {

    @Inject
    public LocalNameExtractor() {
    }

    /**
     * Gets the local name from an IRI.
     *
     * @param iri The IRI.
     * @return A string that represents the local name.  This is either the string after the fragment
     * identifier or it is the IRI last path element plus any query string.
     * If the specified IRI does not have a fragment or does not have
     * a path then the empty string is returned.
     */
    @Nonnull
    public String getLocalName(@Nonnull IRI iri) {
        String iriString = checkNotNull(iri).toString();
        int hashIndex = iriString.lastIndexOf("#");
        if(hashIndex != -1 && hashIndex < iriString.length() - 1) {
            return decode(iriString.substring(hashIndex + 1));
        }
        int slashIndex = iriString.lastIndexOf("/");
        if(slashIndex != -1 && slashIndex < iriString.length() - 1) {
            return decode(iriString.substring(slashIndex + 1));
        }
        return "";
    }

    private String decode(@Nonnull String localName) {
        return URLDecoder.decode(localName, Charsets.UTF_8);
    }
}

