package edu.stanford.bmir.protege.web.shared;

import com.google.common.base.Optional;
import com.google.gwt.i18n.shared.DateTimeFormat;
import edu.stanford.bmir.protege.web.shared.entity.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.XSDVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/11/2012
 */
public class DataFactory {

    public static final String FRESH_ENTITY_SCHEME = "wptmp";



//    public static final DateTimeFormat DATE_TIME_FORMAT = DateTimeFormat.getFormat("yyyy'-'MM'-'dd 'T' hh':'mm':'ss ZZZ");
//
//    public static final DateTimeFormat DATE_TIME_FORMAT_NO_TIME_ZONE = DateTimeFormat.getFormat("yyyy'-'MM'-'dd 'T' hh':'mm':'ss");
//
//    public static final DateTimeFormat DATE_TIME_FORMAT_NO_TIME = DateTimeFormat.getFormat("yyyy'-'MM'-'dd");


    private static OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    public static OWLDataFactory get() {
        return dataFactory;
    }

    public static OWLClass getOWLThing() {
        return dataFactory.getOWLThing();
    }

    public static IRI getIRI(String iri) {
        return IRI.create(iri);
    }

    public static OWLClass getOWLClass(String iri) {
        return getOWLClass(getIRI(iri));
    }

    public static OWLClass getOWLClass(IRI iri) {
        return dataFactory.getOWLClass(iri);
    }

    public static OWLObjectProperty getOWLObjectProperty(String iri) {
        return getOWLObjectProperty(getIRI(iri));
    }

    public static OWLObjectProperty getOWLObjectProperty(IRI iri) {
        return dataFactory.getOWLObjectProperty(iri);
    }

    public static OWLDataProperty getOWLDataProperty(IRI iri) {
        return dataFactory.getOWLDataProperty(iri);
    }

    public static OWLDataProperty getOWLDataProperty(String iri) {
        return dataFactory.getOWLDataProperty(getIRI(iri));
    }

    public static OWLAnnotationProperty getOWLAnnotationProperty(IRI iri) {
        return dataFactory.getOWLAnnotationProperty(iri);
    }

    public static OWLAnnotationProperty getOWLAnnotationProperty(String iri) {
        return dataFactory.getOWLAnnotationProperty(getIRI(iri));
    }


    public static OWLNamedIndividual getOWLNamedIndividual(String iri) {
        return getOWLNamedIndividual(getIRI(iri));
    }

    public static OWLNamedIndividual getOWLNamedIndividual(IRI iri) {
        return dataFactory.getOWLNamedIndividual(iri);
    }

    public static OWLDatatype getOWLDatatype(IRI iri) {
        return dataFactory.getOWLDatatype(iri);
    }

    public static OWLDatatype getOWLDatatype(String iri) {
        return dataFactory.getOWLDatatype(getIRI(iri));
    }

    public static <T extends OWLEntity> T getOWLEntity(EntityType<T> entityType, String iri) {
        return getOWLEntity(entityType, getIRI(iri));
    }

    public static IRI getFreshOWLEntityIRI(String shortName) {
        String iri = getFreshIRIString(shortName);
        return IRI.create(iri);
    }

    public static <T extends OWLEntity> T getFreshOWLEntity(EntityType<T> entityType, String shortName) {
        String iri = getFreshIRIString(shortName);
        return getOWLEntity(entityType, iri);
    }

    private static String getFreshIRIString(String shortName) {
        return FRESH_ENTITY_SCHEME + ":entity#" + shortName;
    }

    public static boolean isFreshEntity(OWLEntity entity) {
        IRI iri = entity.getIRI();
        String scheme = iri.getScheme();
        return scheme != null && FRESH_ENTITY_SCHEME.equalsIgnoreCase(scheme);
    }

    public static String getFreshEntityShortName(OWLEntity entity) {
        String iri = entity.getIRI().toString();
        if(!iri.startsWith(FRESH_ENTITY_SCHEME)) {
            throw new RuntimeException(entity.toStringID() + " is not a fresh entity");
        }
        int firstHashIndex = iri.indexOf('#');
        if(firstHashIndex == -1) {
            throw new RuntimeException("Malformed fresh entity: Could not find #");
        }
        return iri.substring(firstHashIndex + 1);
    }




    public static <T extends OWLEntity> T getOWLEntity(EntityType<T> entityType, IRI iri) {
        return dataFactory.getOWLEntity(entityType, iri);
    }

    public static OWLEntityData getOWLEntityData(OWLEntity entity, final String browserText) {
        return entity.accept(new OWLEntityVisitorEx<OWLEntityData>() {
            @Override
            public OWLEntityData visit(OWLClass owlClass) {
                return new OWLClassData(owlClass, browserText);
            }

            @Override
            public OWLEntityData visit(OWLObjectProperty property) {
                return new OWLObjectPropertyData(property, browserText);
            }

            @Override
            public OWLEntityData visit(OWLDataProperty property) {
                return new OWLDataPropertyData(property, browserText);
            }

            @Override
            public OWLEntityData visit(OWLNamedIndividual individual) {
                return new OWLNamedIndividualData(individual, browserText);
            }

            @Override
            public OWLEntityData visit(OWLDatatype datatype) {
                return new OWLDatatypeData(datatype, browserText);
            }

            @Override
            public OWLEntityData visit(OWLAnnotationProperty property) {
                return new OWLAnnotationPropertyData(property, browserText);
            }
        });
    }

    public static OWLLiteral getOWLLiteral(int value) {
        return dataFactory.getOWLLiteral(value);
    }

    public static OWLLiteral getOWLLiteral(boolean value) {
        return dataFactory.getOWLLiteral(value);
    }

    public static OWLLiteral getOWLLiteral(double value) {
        return dataFactory.getOWLLiteral(value);
    }


    private static OWLLiteral getOWLLiteral(Date date) {
        return DataFactory.getOWLLiteral(date.toString(), DataFactory.getXSDDateTime());
    }

    public static OWLLiteral getOWLLiteral(String value) {
        return dataFactory.getOWLLiteral(value);
    }

    public static OWLLiteral getOWLLiteral(String value, OWLDatatype datatype) {
        return dataFactory.getOWLLiteral(value, datatype);
    }

    public static OWLLiteral getDateTime(String value) {
        return dataFactory.getOWLLiteral(value, getXSDDateTime());
    }


    public static OWLLiteral getOWLLiteral(String value, String lang) {
        return dataFactory.getOWLLiteral(value, lang);
    }

    public static OWLDatatype getXSDInteger() {
        return dataFactory.getIntegerOWLDatatype();
    }

    public static OWLDatatype getXSDString() {
        return dataFactory.getOWLDatatype(XSDVocabulary.STRING.getIRI());
    }

    public static OWLDatatype getXSDDouble() {
        return dataFactory.getOWLDatatype(XSDVocabulary.DOUBLE.getIRI());
    }

    public static OWLDatatype getXSDDecimal() {
        return dataFactory.getOWLDatatype(XSDVocabulary.DECIMAL.getIRI());
    }

    public static OWLDatatype getXSDDateTime() {
        return dataFactory.getOWLDatatype(XSDVocabulary.DATE_TIME.getIRI());
    }


    /**
     * Parses a string and optional language string into an {@link OWLLiteral}.
     * @param lexicalValue The lecical value to parse.  Not {@code null}.
     * @param language The optional language.  Not {@code null}.
     * @return The parsed literal.  If {@code language} is present then the literal will be a plain literal with the
     * language tag.  If the lexical value corresponds to true or false then the literal will be typed as an xsd:boolean.
     * If the lexical value can be parsed as a datetime then the literal will be typed with xsd:dateTime.  If the lexical
     * value can be parsed as an integer then the lexical value will be typed with xsd:integet.  If the lexical value
     * can be parsed as a double then the literal will be typed with xsd:double.  Finally, if none of the previous can
     * be accomplished then the returned value is a plain literal without a language tag.
     * @throws NullPointerException if {@code lexicalValue} is {@code null} or {@code language} is {@code null}.
     */
    public static OWLLiteral parseLiteral(String lexicalValue, Optional<String> language) {
        String lang = language.or("");
        if (!lang.isEmpty()) {
            return DataFactory.getOWLLiteral(lexicalValue, lang);
        }
        String trimmedContent = lexicalValue.trim();
        if ("true".equalsIgnoreCase(trimmedContent)) {
            return DataFactory.getOWLLiteral(true);
        }
        if ("false".equalsIgnoreCase(trimmedContent)) {
            return DataFactory.getOWLLiteral(false);
        }
        try {
            return parseDateTimeFormat(trimmedContent);
        }
        catch (IllegalArgumentException e) {
            try {
                Integer value = Integer.parseInt(trimmedContent);
                return DataFactory.getOWLLiteral(value.intValue());
            }
            catch (NumberFormatException e3) {
                try {
                    Double value = Double.parseDouble(trimmedContent);
                    return DataFactory.getOWLLiteral(trimmedContent, DataFactory.getXSDDecimal());
                }
                catch (NumberFormatException e4) {
                    try {
                        Float value = Float.parseFloat(trimmedContent);
                        return DataFactory.getOWLLiteral(trimmedContent, DataFactory.getOWLDatatype(OWL2Datatype.XSD_FLOAT.getIRI()));
                    }
                    catch (NumberFormatException e5) {
                        return DataFactory.getOWLLiteral(lexicalValue);
                    }

                }
            }

        }
    }


    /**
     * Parses the specified lexical value into a datetime literal.
     * @param lexicalValue The lexical value to be parsed.  Not {@code null}.
     * @return The literal representing the specified datetime.
     * @throws IllegalArgumentException if the lexical value cannot be parsed into a date-time format.
     */
    public static OWLLiteral parseDateTimeFormat(final String lexicalValue) throws IllegalArgumentException {

        final DateTimeFormat DATE_TIME_FORMAT = DateTimeFormat.getFormat("yyyy'-'MM'-'dd 'T' hh':'mm':'ss ZZZ");

        final DateTimeFormat DATE_TIME_FORMAT_NO_TIME_ZONE = DateTimeFormat.getFormat("yyyy'-'MM'-'dd 'T' hh':'mm':'ss");

        final DateTimeFormat DATE_TIME_FORMAT_NO_TIME = DateTimeFormat.getFormat("yyyy'-'MM'-'dd");


        if (isNow(lexicalValue)) {
            DateTimeFormat longFormat = DATE_TIME_FORMAT;
            return DataFactory.getDateTime(longFormat.format(new Date()));
        }



        String strippedContent = lexicalValue.replace("st", "").replace("nd", "").replace("rd", "").replace("th", "");
        List<DateTimeFormat> formats = new ArrayList<DateTimeFormat>();




        formats.add(DATE_TIME_FORMAT);
        formats.add(DATE_TIME_FORMAT_NO_TIME_ZONE);
        formats.add(DATE_TIME_FORMAT_NO_TIME);
//        formats.add(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_LONG));
//        formats.add(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM));
//        formats.add(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT));
//        formats.add(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT));
//        formats.add(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));
        formats.add(DateTimeFormat.getFormat("dd/MM/yyyy"));
        formats.add(DateTimeFormat.getFormat("dd/MM/yy"));
        formats.add(DateTimeFormat.getFormat("dd/MMM/yyyy"));
        formats.add(DateTimeFormat.getFormat("MMM/dd/yyyy"));
        formats.add(DateTimeFormat.getFormat("dd MMM yyyy"));
        formats.add(DateTimeFormat.getFormat("dd MMM yyyy HH:mm"));
        formats.add(DateTimeFormat.getFormat("dd MMM yyyy hh:mm aaa"));
        formats.add(DateTimeFormat.getFormat("MMM dd yyyy"));
        formats.add(DateTimeFormat.getFormat("MMM dd yyyy hh:mm aaa"));
        for (DateTimeFormat format : formats) {
            try {
                Date date = format.parse(strippedContent);
                return getOWLLiteral(date);
            }
            catch (IllegalArgumentException e) {
                try {
                    DateTimeFormat formatNoSpace = DateTimeFormat.getFormat(format.getPattern().replaceAll(" ", ""));
                    Date date = formatNoSpace.parse(strippedContent);
                    return getOWLLiteral(date);
                }
                catch (IllegalArgumentException e1) {
                    // Contine
                }
            }

        }
        throw new IllegalArgumentException();
    }

    private static boolean isNow(String trimmedContent) {
        return "today".equalsIgnoreCase(trimmedContent) || "now".equalsIgnoreCase(trimmedContent);
    }

}
