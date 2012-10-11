package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class Literal implements DataPropertyEdgeValue, AnnotationPropertyEdgeValue, HasVisualObject, HasDefaultShortForm, Serializable {

    private String lexicalValue;

    private Datatype datatype;

    private String lang = null;
    
    public Literal(String lexicalValue, Datatype datatype) {
        this.lexicalValue = lexicalValue;
        this.datatype = datatype;
    }

    public Literal(String lexicalValue, String lang) {
        this.lexicalValue = lexicalValue;
        this.lang = lang;
        this.datatype = Datatype.getRDFPlainLiteral();
    }
    
    public static Literal getRDFPlainLiteral(String lexicalValue) {
        return new Literal(lexicalValue, Datatype.getRDFPlainLiteral());
    }

    public static Literal getRDFPlainLiteral(String lexicalValue, String languageTag) {
        return new Literal(lexicalValue, languageTag);
    }


    public static Literal getXSDStringLiteral(String lexicalValue) {
        return new Literal(lexicalValue, Datatype.getXSDString());
    }

    public static Literal getXSDIntegerLiteral(int intValue) {
        return new Literal(Integer.toString(intValue), Datatype.getXSDInteger());
    }

    public static Literal getXSDDoubleLiteral(double doubleValue) {
        return new Literal(Double.toString(doubleValue), Datatype.getXSDDouble());
    }

    public static Literal getXSDFloatLiteral(float floatValue) {
        return new Literal(Float.toString(floatValue), Datatype.getXSDFloat());
    }

    public static Literal getXSDBooleanLiteral(boolean booleanValue) {
        return new Literal(Boolean.toString(booleanValue), Datatype.getXSDBoolean());
    }
    
    
  
    public boolean isRDFPlainLiteral() {
        return datatype.isRDFPlainLiteral();
    }
    
    public String getLexicalValue() {
        return lexicalValue;
    }


    public boolean hasLang() {
        return isRDFPlainLiteral();
    }
    
    public String getLang() {
        if(lang == null) {
            return "";
        }
        else {
            return lang;
        }
    }

    public Datatype getDatatype() {
        return datatype;
    }

    
    public VisualObject<?> toVisualObject() {
        return new VisualLiteral(this, getDefaultShortForm());
    }

    public VisualObject<?> toVisualObject(String browserText) {
        return new VisualLiteral(this, browserText);
    }

    /**
     * Gets the default short form for this object.
     * @return A string representing the short form of the implementer of this interface. Not null.
     */
    public String getDefaultShortForm() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(lexicalValue);
        sb.append("\"");
        if(isRDFPlainLiteral()) {
            sb.append("@");
            if(hasLang()) {
                sb.append(lang);
            }
        }
        else {
            sb.append(lexicalValue);
            sb.append("^^");
            sb.append(datatype.getDefaultShortForm());
        }
        return sb.toString();
    }
}
