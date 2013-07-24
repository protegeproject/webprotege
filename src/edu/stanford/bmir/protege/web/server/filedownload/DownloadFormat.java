package edu.stanford.bmir.protege.web.server.filedownload;

import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyFormat;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public enum DownloadFormat {

    RDF_XML(new RDFXMLOntologyFormat(), "application/rdf+xml", DownloadFormatExtension.rdf),

    RDF_TURLE(new TurtleOntologyFormat(), "text/turtle", DownloadFormatExtension.ttl),

    OWL_XML(new OWLXMLOntologyFormat(), "application/owl+xml", DownloadFormatExtension.owx),

    MANCHESTER(new ManchesterOWLSyntaxOntologyFormat(), "text/owl-manchester", DownloadFormatExtension.omn),

    FUNCTIONAL_SYNTAX(new OWLFunctionalSyntaxOntologyFormat(), "text/owl-functional", DownloadFormatExtension.ofn);


    private OWLOntologyFormat ontologyFormat;

    private String mimeType;

    private DownloadFormatExtension extension;

    private DownloadFormat(OWLOntologyFormat ontologyFormat, String mimeType, DownloadFormatExtension extension) {
        this.ontologyFormat = ontologyFormat;
        this.mimeType = mimeType;
        this.extension = extension;
    }


    public String getParameterValue() {
        return extension.getExtension();
    }

    public OWLOntologyFormat getOntologyFormat() {
        return ontologyFormat;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getExtension() {
        return extension.getExtension();
    }


    /**
     * Gets the format for the specified name.
     * @param parameterName The parameter name.  May be {@code null}.
     * @return The format that has the specified parameter name, or the default format (returned by
     * {@link #getDefaultFormat()} if the paramter name is {@code null} or the parameter name is not recognised).
     * Not {@code null}.
     */
    public static DownloadFormat getDownloadFormatFromParameterName(String parameterName) {
        if(parameterName == null) {
            return getDefaultFormat();
        }
        for(DownloadFormat format : values()) {
            if(format.getParameterValue().equals(parameterName)) {
                return format;
            }
        }
        return getDefaultFormat();
    }

    /**
     * Gets the default format.
     * @return The default format.  Not {@code null}.
     */
    public static DownloadFormat getDefaultFormat() {
        return RDF_XML;
    }
}
