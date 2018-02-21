package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;
import org.semanticweb.owlapi.formats.*;
import org.semanticweb.owlapi.model.OWLDocumentFormat;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public enum DownloadFormat {

    RDF_XML(new RDFXMLDocumentFormat(), "application/rdf+xml", DownloadFormatExtension.owl),

    RDF_TURLE(new TurtleDocumentFormat(), "text/turtle", DownloadFormatExtension.ttl),

    OWL_XML(new OWLXMLDocumentFormat(), "application/owl+xml", DownloadFormatExtension.owx),

    MANCHESTER(new ManchesterSyntaxDocumentFormat(), "text/owl-manchester", DownloadFormatExtension.omn),

    FUNCTIONAL_SYNTAX(new FunctionalSyntaxDocumentFormat(), "text/owl-functional", DownloadFormatExtension.ofn);


    private OWLDocumentFormat DocumentFormat;

    private String mimeType;

    private DownloadFormatExtension extension;

    DownloadFormat(OWLDocumentFormat DocumentFormat, String mimeType, DownloadFormatExtension extension) {
        this.DocumentFormat = DocumentFormat;
        this.mimeType = mimeType;
        this.extension = extension;
    }


    public String getParameterValue() {
        return extension.getExtension();
    }

    public OWLDocumentFormat getDocumentFormat() {
        return DocumentFormat;
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
