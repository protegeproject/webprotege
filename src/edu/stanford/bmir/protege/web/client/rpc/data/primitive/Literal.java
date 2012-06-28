package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class Literal extends Primitive implements Serializable {

    private String lexicalValue;

    private Datatype datatype;

    public Literal(String lexicalValue, Datatype datatype) {
        this.lexicalValue = lexicalValue;
        this.datatype = datatype;
    }

    public boolean isRDFPlainLiteral() {
        return datatype.isRDFPlainLiteral();
    }
    
    public String getLexicalValue() {
        return lexicalValue;
    }
    
    public boolean hasLang() {
        throw new RuntimeException("TODO");
    }
    
    public String getLang() {
        throw new RuntimeException("TODO");
    }


    public Datatype getDatatype() {
        return datatype;
    }
}
