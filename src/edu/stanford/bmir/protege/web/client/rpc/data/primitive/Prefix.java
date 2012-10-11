package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2012
 */
public enum Prefix {

    OWL("owl", "http://www.w3.org/2002/07/owl#"),

    /**
     * RDFS namespace
     */
    RDFS("rdfs", "http://www.w3.org/2000/01/rdf-schema#"),

    /**
     * RDF namespace
     */
    RDF("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"),

    /**
     * XSD namespace
     */
    XSD("xsd", "http://www.w3.org/2001/XMLSchema#"),

    /**
     * XML namespace
     */
    XML("xsd", "http://www.w3.org/XML/1998/namespace"),

    /**
     * SWRL namespace
     */
    SWRL("swrl", "http://www.w3.org/2003/11/swrl#"),

    /**
     * SWRLB namespace
     */
    SWRLB("swrlb", "http://www.w3.org/2003/11/swrlb#"),

    /**
     * SKOS namespace
     */
    SKOS("skos", "http://www.w3.org/2004/02/skos/core#");

    
    
    private String prefixName;
    
    private String prefix;

    private Prefix(String prefixName, String prefix) {
        this.prefixName = prefixName;
        this.prefix = prefix;
    }

    /**
     * Gets the short nme for the prefix.  For example "rdf" is the short name for the prefix
     * "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
     * @return The short name for the prefix.  Not null.
     */
    public String getPrefixName() {
        return prefixName;
    }

    /**
     * Gets the lexical value of the prefix.
     * @return The prefix.  Not null.
     */
    public String getPrefix() {
        return prefix;
    }


    /**
     * Overridden to return the prefix as a string.
     * @see {@link #getPrefix()}
     * @return A string which is equal to the prefix.
     */
    @Override
    public String toString() {
        return getPrefix();
    }


}
