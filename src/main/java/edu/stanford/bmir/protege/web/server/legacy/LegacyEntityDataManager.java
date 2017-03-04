package edu.stanford.bmir.protege.web.server.legacy;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyType;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.editor.OWLEntityContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.StringTokenizer;

import static edu.stanford.bmir.protege.web.server.renderer.RenderingManager.selectEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Mar 2017
 */
@Deprecated
@ProjectSingleton
@SuppressWarnings("deprecation")
public class LegacyEntityDataManager implements LegacyEntityDataProvider {

    private final BidirectionalShortFormProvider shortFormProvider;

    private final RenderingManager renderingManager;

    private final OWLDataFactory df;

    @RootOntology
    private final OWLOntology rootOntology;

    @Inject
    public LegacyEntityDataManager(BidirectionalShortFormProvider shortFormProvider,
                                   RenderingManager renderingManager,
                                   OWLDataFactory df,
                                   OWLOntology rootOntology) {
        this.shortFormProvider = shortFormProvider;
        this.renderingManager = renderingManager;
        this.df = df;
        this.rootOntology = rootOntology;
    }

    public Set<OWLEntity> getEntities(String name) {
        return renderingManager.getEntities(name);
    }

    public String getBrowserText(OWLObject object) {
        return renderingManager.getBrowserText(object);
    }

    /**
     * Gets an entity of a specific type, whose identity is specified  by an {@link EntityData} object.  The IRI of the
     * returned entity will be computed by either mapping the browser text specified by the specified EntityData into
     * an existing entity, and then retriving that entity's IRI, or, by using the name specified by the specified
     * EntityData and then mapping that name into an IRI (using {@link #getIRI(String)}.
     * @param entityData The entity data object containing a name and browser text.
     * @param entityType The type of entity to be retrieved.
     * @param <E> The type specifier.
     * @return An entity of the specified type.
     * @throws NullPointerException if entityData is <code>null</code> or entityType is <code>null</code>.
     */
    public <E extends OWLEntity> E getEntity(EntityData entityData, EntityType<E> entityType) {
        if(entityData == null) {
            throw new NullPointerException("entityData must not be null!");
        }
        if(entityType == null) {
            throw new NullPointerException("entityType must not be null");
        }
        Set<OWLEntity> entities = shortFormProvider.getEntities(entityData.getBrowserText());
        IRI iri;
        if(!entities.isEmpty()) {
            iri = selectEntity(entities).getIRI();
        }
        else {
            iri = getIRI(entityData.getName());
        }
        return df.getOWLEntity(entityType, iri);
    }



    public EntityData getEntityData(OWLEntity entity) {
        if (entity instanceof OWLProperty) {
            return getPropertyEntityData(entity);
        }
        else {
            EntityData entityData = new EntityData(entity.getIRI().toString());
            String browserText = renderingManager.getBrowserText(entity);
            entityData.setBrowserText(browserText);
            if(entity instanceof OWLClass) {
                entityData.setValueType(ValueType.Cls);
            }
            else if(entity instanceof OWLIndividual) {
                entityData.setValueType(ValueType.Instance);
            }
            return entityData;
        }
    }

    public EntityData getEntityData(OWLAnnotationValue annotationValue) {
        return annotationValue.accept(new OWLAnnotationValueVisitorEx<EntityData>() {
            @Nonnull
            public EntityData visit(@Nonnull IRI iri) {
                Set<OWLEntity> entities = rootOntology.getEntitiesInSignature(iri);
                if(entities.isEmpty()) {
                    // Now what?
                    return new EntityData(iri.toString(), iri.toString());
                }
                else {
                    return getEntityData(selectEntity(entities));
                }
            }

            @Nonnull
            public EntityData visit(@Nonnull OWLAnonymousIndividual individual) {
                return getEntityData(individual);
            }

            @Nonnull
            public EntityData visit(@Nonnull OWLLiteral literal) {
                return getEntityData(literal);
            }
        });
    }

    public EntityData getEntityData(OWLLiteral literal) {
        P3LiteralParser literalParser = new P3LiteralParser(literal);
        String p3Format = literalParser.formatP3Literal();
        EntityData entityData = new EntityData(p3Format);
        entityData.setBrowserText(p3Format);
        // The UI seems a bit inconsistent in accepting ~# sometimes it returns it and other times it doesn't and it
        // doesn't seem to accept it in places that return it.
        OWLDatatype dt = literal.getDatatype();
        if(dt.isInteger()) {
            entityData.setValueType(ValueType.Integer);
        }
        else if(dt.isBoolean()) {
            entityData.setValueType(ValueType.Boolean);
        }
        else if(dt.isDouble()) {
            entityData.setValueType(ValueType.Float);
        }
        else if(dt.isFloat()) {
            entityData.setValueType(ValueType.Float);
        }
        else if(dt.isString()) {
            entityData.setValueType(ValueType.String);
        }
        else {
            // Totally unsatisfactory
            entityData.setValueType(ValueType.Any);
        }
        return entityData;
    }


    public EntityData getEntityData(OWLAnonymousIndividual individual) {
        EntityData entityData = new EntityData(individual.getID().getID());
        String browserText = individual.getID().getID();
        entityData.setBrowserText(browserText);
        entityData.setValueType(ValueType.Any);
        return entityData;
    }

//    /**
//     * A convenience method which ultimately delegates to {@link #getEntity(String)}.
//     * @param entityIRI The IRI of the entity for which the entity data will be returned.
//     * @return The entity data of an entity which has the specified IRI.
//     */
//    public EntityData getEntity(IRI entityIRI) {
//        return getEntity(entityIRI.toString());
//    }

    public PropertyEntityData getPropertyEntityData(OWLEntity entity) {
        final PropertyEntityData entityData = new PropertyEntityData(entity.getIRI().toString());
        // Don't know if this is correct.
        entityData.setBrowserText(shortFormProvider.getShortForm(entity));
        entity.accept(new OWLEntityVisitor() {
            public void visit(@Nonnull OWLClass owlClass) {
            }

            public void visit(@Nonnull OWLObjectProperty owlObjectProperty) {
                entityData.setPropertyType(PropertyType.OBJECT);
                // Correct or not? Grrrrr
                entityData.setValueType(ValueType.Instance);
            }

            public void visit(@Nonnull OWLDataProperty owlDataProperty) {
                entityData.setPropertyType(PropertyType.DATATYPE);
                // Correct or not? Grrrrr
                entityData.setValueType(ValueType.Literal);
            }

            public void visit(@Nonnull OWLNamedIndividual owlNamedIndividual) {
            }

            public void visit(@Nonnull OWLDatatype owlDatatype) {
            }

            public void visit(@Nonnull OWLAnnotationProperty owlAnnotationProperty) {
                // Correct or not? Grrrrr
                entityData.setPropertyType(PropertyType.ANNOTATION);
                entityData.setValueType(ValueType.Any);
            }
        });
        return entityData;
    }

    /**
     * Tests to see whether an EntityData is well formed.  An EntityData object is considered well formed if it can be
     * converted to an OWLObject.  Specifically,
     * @param entityData the EntityData
     */
    public boolean isWellFormed(EntityData entityData) {
        if(entityData.getValueType() == null) {
            if(!(entityData instanceof PropertyEntityData)) {
                // Can infer type
                return false;
            }
        }
        if(entityData instanceof PropertyEntityData) {
            if(((PropertyEntityData) entityData).getPropertyType() == null) {
                return false;
            }
        }
        else {
            if(entityData.getValueType() == null) {
                return false;
            }
        }
        return true;
    }


    public OWLObject getOWLObject(EntityData entityData) {
        ValueType valueType = entityData.getValueType();
        if(valueType == null) {
            // Now what?  Totally and utterly horrible.
            // So ugly.
            if(entityData instanceof PropertyEntityData) {
                valueType = ValueType.Property;
            }
            else {
                // Make this clear - this is not the thing to do.
                System.err.println("WARNING:  EntityData.valueType is NOT SET!  Trying to make a guess.  UI code should set the value type!");
                // Try to guess if from the name....
                try {
                    URI uri = new URI(entityData.getName());
                    IRI iri = IRI.create(uri);
                    for(EntityType<?> entityType : EntityType.values()) {
                        OWLEntity entity = df.getOWLEntity(entityType, iri);
                        if(rootOntology.containsEntityInSignature(entity)) {
                            return entity;
                        }
                    }
                }
                catch (URISyntaxException e) {
                    // NOPE!
                    System.err.println("Tried to convert an entity name to a URI but failed: " + entityData.getName());
                }

                throw new RuntimeException("Cannot translate EntityData to OWLObject. Reason: The ValueType in the entity data is null.  Cannot determine type of entity.");
            }
        }


        switch (valueType) {
            case Cls:
                return getEntity(entityData.getName(), EntityType.CLASS);
            case Property:
                if(entityData instanceof PropertyEntityData) {
                    PropertyEntityData ped = (PropertyEntityData) entityData;
                    PropertyType propertyType = ped.getPropertyType();
                    if(propertyType == PropertyType.OBJECT) {
                        return getEntity(entityData.getName(), EntityType.OBJECT_PROPERTY);
                    }
                    else if(propertyType == PropertyType.DATATYPE) {
                        return getEntity(entityData.getName(), EntityType.DATA_PROPERTY);
                    }
                    else if(propertyType == PropertyType.ANNOTATION) {
                        return getEntity(entityData.getName(), EntityType.ANNOTATION_PROPERTY);
                    }
                    else {
                        throw new RuntimeException("Cannot translate entity data to OWLObject.  Reason: Unknown property type: " + propertyType);
                    }
                }
            case Instance:
                return getEntity(entityData.getName(), EntityType.NAMED_INDIVIDUAL);

            case Boolean:
            case Date:
            case Integer:
            case Literal:
            case Float:
            case String:
            case Symbol:
            case Any:
            default:
                return getLiteral(entityData);

        }
//        throw new RuntimeException("Unable to translate EntityData to OWLObject.  There is insufficient information. Culprit: " + entityData);
    }

    /**
     * Gets an OWLEntity from an {@link EntityData} object.  The {@link EntityData} object must correspond to an
     * {@link OWLEntity} otherwise a RuntimeException will be thrown.  This method essentially calls the {@link #getOWLObject(edu.stanford.bmir.protege.web.client.rpc.data.EntityData)}
     * method, checks to see of the result is an instance of {@link OWLEntity} and then casts it to an {@link OWLEntity}
     * if it is.
     * @param entityData The entity data to translate
     * @return The {@link OWLEntity} that corresponds to the {@link EntityData}.
     */
    public OWLEntity getOWLEntity(EntityData entityData) {
        OWLObject object = getOWLObject(entityData);
        if(!(object instanceof OWLEntity)) {
            throw new RuntimeException("Cannot translate entity data to OWLEntity. The entity data corresponds to an " + object.getClass().getSimpleName());
        }
        return (OWLEntity) object;
    }

    /**
     * Translates an {@link EntityData} object to an {@link OWLLiteral}.  This assumes that the lexical value for the
     * literal is stored in the name of the entity data.  Ideally, the name should therefore not be <code>null</code>,
     * however, it appears that certain UI components are a bit sloppy about creating entity data objects correctly, and therefore,
     * in order not to break backwards compatibility, this method treats null values as the empty string.  I also, have
     * not idea how language tags are specified using the encoding scheme in WP, so this method assumes that literals
     * are typed as follows: {@link ValueType#Boolean} as {@link OWL2Datatype#XSD_BOOLEAN},
     * {@link ValueType#Date} as {@link OWL2Datatype#XSD_DATE_TIME}, as {@link ValueType#Integer} {@link OWL2Datatype#XSD_INTEGER},
     * {@link ValueType#Float} as {@link OWL2Datatype#XSD_DECIMAL} and {@link ValueType#String} as {@link OWL2Datatype#XSD_STRING},
     * {@link ValueType#Symbol} as {@link OWL2Datatype#RDF_PLAIN_LITERAL}, {@link ValueType#Any} as {@link OWL2Datatype#RDF_PLAIN_LITERAL},
     * {@link ValueType#Literal} as {@link OWL2Datatype#RDF_PLAIN_LITERAL}.
     * @param entityData The entity data to translate
     * @return An {@link OWLLiteral} corresponding to the {@link EntityData} object.  Never <code>null</code>.
     */
    public OWLLiteral getLiteral(EntityData entityData) {
        // I assume the lexical value is stored in the name.  But there's no documentation that specifies this.
        String entityName = entityData.getName();
        if(entityName == null) {
            // Oh dear - Try to be more robust and assume null means empty string.
            entityName = "";
        }
        // Unfortunately, the UI uses a P3 encoding for literals.
        P3LiteralParser literalParser = new P3LiteralParser(entityName);
        String lexicalValue = literalParser.getLiteral();
        String lang = literalParser.getLang();
        if(!lang.isEmpty()) {
            return df.getOWLLiteral(lexicalValue, lang);
        }
        else {
            OWLDatatype datatype = df.getRDFPlainLiteral();
            ValueType valueType = entityData.getValueType();
            if(valueType != null) {
                switch (valueType) {
                    case Boolean:
                        datatype = df.getBooleanOWLDatatype();
                        break;
                    case Date:
                        datatype = df.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME.getIRI());
                        break;
                    case Integer:
                        datatype = df.getOWLDatatype(OWL2Datatype.XSD_INTEGER.getIRI());
                        break;
                    case Float:
                        datatype = df.getOWLDatatype(OWL2Datatype.XSD_DECIMAL.getIRI());
                        break;
                    case String:
                        datatype = df.getOWLDatatype(OWL2Datatype.XSD_STRING.getIRI());
                        break;
                    case Symbol:
                        // What's this?
                    case Any:
                    case Literal:
                    case Cls:
                    default:
                        // Nothing!  Leave as plain literal
                }
            }
            return df.getOWLLiteral(lexicalValue, datatype);
        }

    }

    /**
     * Gets an OWLDatatype for the corresponding Protege 3 ValueType.
     * @param valueType The value type
     */
    public OWLDatatype getDatatype(ValueType valueType) {
        switch (valueType) {
            case Boolean:
                return df.getBooleanOWLDatatype();
            case Date:
                return df.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME.getIRI());
            case Integer:
                return df.getOWLDatatype(OWL2Datatype.XSD_INTEGER.getIRI());
            case Float:
                return df.getOWLDatatype(OWL2Datatype.XSD_FLOAT.getIRI());
            case String:
                return df.getOWLDatatype(OWL2Datatype.XSD_STRING.getIRI());
            case Symbol:
                // What's this?
            case Any:
            case Literal:
            case Cls:
                // Fall through
        }
        return df.getTopDatatype();
    }


    public boolean isInteger(String lexicalValue) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(lexicalValue);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean isDecimal(String lexicalValue) {
        try {
            // Not quite right, but good enough.
            //noinspection ResultOfMethodCallIgnored
            Double.parseDouble(lexicalValue);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean isBoolean(String lexicalValue) {
        return lexicalValue != null && (lexicalValue.equalsIgnoreCase("true") || lexicalValue.equals("1"));
    }

    /**
     * Gets the IRI which the specified entity name corresponds to.  If the specified entity name is actually browser
     * text then the IRI of an entity with that browser text will be returned.  If there are multiple entities with that
     * browser text, then the first one will be returned in accordance with the {@link RenderingManager#selectEntity(java.util.Set)}
     * method.  If the specified entity name does not correspond to any browser text, then a fresh IRI will be generated
     * using the entityName.  This generation process first replaces converts the entityName to camel case.
     * @param entityName The entity name.  This should be a string which represents an IRI, but browser text
     * (as generated by this RenderingManager) will also be accepted.  Must not be null!
     * @return An entity which has an IRI corresponding to the IRI specified by entityName, or an entity which has
     * the browser text corresponding to entityName if browser text was supplied.
     */
    public IRI getIRI(String entityName) {
        if(entityName == null) {
            throw new NullPointerException("Supplied entity name must not be null!");
        }
        // Right could be browser text
        Set<OWLEntity> entitiesByShortForm = shortFormProvider.getEntities(entityName);
        if(!entitiesByShortForm.isEmpty()) {
            return selectEntity(entitiesByShortForm).getIRI();
        }

        try {
            URI uri = new URI(toCamelCase(entityName));
            return IRI.create(uri);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException("Error when generating an IRI from the supplied entity name: " + entityName, e);
        }
    }

    /**
     * Gets an entity of a specific type.  The return value of this method is never null.  For the supplied entity name,
     * the {@link #getIRI(String)} method is called to resolve the name to an IRI.  Next, an entity of the specified type
     * with this IRI is returned.
     * @param entityName The name of the entity.  This should be a string which represents an IRI, but browser text
     * (as generated by this RenderingManager) will also be accepted.  Must not be null.
     * @param entityType The type of entity to return.
     * @param <E> The entity type. Not null.
     * @return An entity which has an IRI corresponding to the IRI specified by entityName, or an entity which has
     * the browser text corresponding to entityName if browser text was supplied.  The entity will be of the specified
     * type.
     */
    public <E extends OWLEntity> E getEntity(String entityName, EntityType<E> entityType) {
        if(entityName == null) {
            throw new NullPointerException("entityName must not be null!");
        }
        if(entityType == null) {
            throw new NullPointerException("entityType must not be null");
        }
        IRI iri = getIRI(entityName);
        return df.getOWLEntity(entityType, iri);
    }


    /**
     * Converts a string to camel case.  Words which are delimited by spaces are capitalised and then run together
     * so that the resulting string does not contain any spaces.  The capitalisation of the first word is preserved.
     * @param s The string that will be converted to camel case.
     * @return The string in camel case.
     */
    public static String toCamelCase(String s) {
        if(!s.contains(" ")) {
            return s;
        }
        StringTokenizer tokenizer = new StringTokenizer(s, " ", false);
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (count == 0) {
                sb.append(token);
            }
            else {
                sb.append(token.substring(0, 1).toUpperCase());
                if(token.length() > 1) {
                    sb.append(token.substring(1));
                }
            }
            count++;
        }
        return sb.toString();
    }


}
