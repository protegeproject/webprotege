package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyType;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.render.BrowserTextRenderer;
import edu.stanford.bmir.protege.web.shared.BrowserTextProvider;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOWLObjectRendererImpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/03/2012
 *
 * This class is here to deal with the confusion/mess surrounding "names" in web-protege.  It translates between OWLObjects
 * and EntityData and names.
 */
public class RenderingManager implements BrowserTextProvider  {

    public static final String NULL_BROWSER_TEXT = "\"\"";

    private OWLAPIProject project;

    private WebProtegeBidirectionalShortFormProvider shortFormProvider;

    // An immutable map of IRI to OWLEntity for built in entities.
    private Map<IRI, OWLEntity> builtInEntities;


    public RenderingManager(OWLAPIProject prj) {
        this.project = prj;

        setupBuiltInEntities();

        shortFormProvider = new WebProtegeBidirectionalShortFormProvider(project);
    }


    /**
     * Called from the constructor
     */
    private void setupBuiltInEntities() {
        HashMap<IRI, OWLEntity> builtInEntities = new HashMap<IRI, OWLEntity>();
        
        OWLDataFactory df = project.getDataFactory();
        putEntity(df.getOWLThing(), builtInEntities);
        putEntity(df.getOWLNothing(), builtInEntities);
        putEntity(df.getOWLTopObjectProperty(), builtInEntities);
        putEntity(df.getOWLBottomObjectProperty(), builtInEntities);
        putEntity(df.getOWLTopDataProperty(), builtInEntities);
        putEntity(df.getOWLBottomDataProperty(), builtInEntities);
        for (IRI iri : OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS) {
            putEntity(df.getOWLAnnotationProperty(iri), builtInEntities);
        }
        for(DublinCoreVocabulary vocabulary : DublinCoreVocabulary.values()) {
            OWLAnnotationProperty prop = df.getOWLAnnotationProperty(vocabulary.getIRI());
            putEntity(prop, builtInEntities);
        }
        for (OWLAnnotationProperty prop : SKOSVocabulary.getAnnotationProperties(df)) {
            putEntity(prop, builtInEntities);
        }
        for (OWL2Datatype dt : OWL2Datatype.values()) {
            OWLDatatype datatype = df.getOWLDatatype(dt.getIRI());
            putEntity(datatype, builtInEntities);
        }
        // Called from the constructor - thread safe to set it here
        this.builtInEntities = Collections.unmodifiableMap(builtInEntities);

    }


    /**
     * Adds an {@link OWLEntity} to a map, so that they entity is keyed by its {@link IRI}.
     * @param entity The entity to add.  Not null.
     * @param map The map to add the entity to. Not null.
     */
    private static void putEntity(OWLEntity entity, HashMap<IRI, OWLEntity> map) {
        map.put(entity.getIRI(), entity);
    }

    public BidirectionalShortFormProvider getShortFormProvider() {
        return shortFormProvider;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////  Main public interface
    //////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the set of entities that match a given entity name.  The entity name should be a string corresponding to
     * an IRI.  However, this method will also work for browser text so long as the browser text was generated by this
     * rendering manager.
     * @param entityName The entity name.  Should be an IRI.
     * @return A set of entities that have the specified name (or browser text).  The set will be empty if no such
     * entities exist.
     */
    public Set<OWLEntity> getEntities(String entityName) {
        // Some UI components mix up the use of entity name and browser text (actually, it's not clear what should happen
        // because users presumably primarily edit browser text).  So, in order to have a belt
        // and braces approach to things, we check to see if the entity name supplied above is in fact some browser
        // text.  In some case, there may be a one-to-many mapping, but this is just tough luck - badly behaving UI
        // components get what they deserve!
        Set<OWLEntity> entities = shortFormProvider.getEntities(entityName);
        if (!entities.isEmpty()) {
            printBrowserTextReferenceWarningMessage(entityName);
            return entities;
        }
        // Not referring to browser text, or there is no browser text!

        IRI iri = IRI.create(entityName);
        OWLEntity builtInEntity = builtInEntities.get(iri);
        if (builtInEntity != null) {
            return Collections.singleton(builtInEntity);
        }
        OWLOntology rootOntology = project.getRootOntology();
        Set<OWLEntity> entitiesInSig = rootOntology.getEntitiesInSignature(iri, true);
        if(!entitiesInSig.isEmpty()) {
            return entitiesInSig;
        }
        return Collections.emptySet();
    }



    /**
     * Gets the IRI which the specified entity name corresponds to.  If the specified entity name is actually browser
     * text then the IRI of an entity with that browser text will be returned.  If there are multiple entities with that
     * browser text, then the first one will be returned in accordance with the {@link #selectEntity(java.util.Set)}
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

    @Override
    public Optional<String> getOWLEntityBrowserText(OWLEntity entity) {
        String shortForm = shortFormProvider.getShortForm(entity);
        if(shortForm == null) {
            return Optional.absent();
        }
        else {
            return Optional.of(shortForm);
        }
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
        OWLDataFactory df = project.getDataFactory();
        IRI iri = getIRI(entityName);
        return df.getOWLEntity(entityType, iri);
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
        OWLDataFactory df = project.getDataFactory();
        return df.getOWLEntity(entityType, iri);
    }

    /**
     * Gets all of the short forms (browser text) of all entities.
     * @return A set of short forms for all entities.
     */
    public Set<String> getShortForms() {
        return shortFormProvider.getShortForms();
    }

    /**
     * Gets the short for for the specified entity.
     * @param entity The entity.
     * @return The entity short form. Not null.
     */
    public String getShortForm(OWLEntity entity) {
        return shortFormProvider.getShortForm(entity);
    }

    public EntityData getEntityData(String entityName, EntityType<?> entityType) {
        OWLEntity entity = getEntity(entityName, entityType);
        return getEntityData(entity);
    }
    
    public EntityData getEntityData(OWLEntity entity) {
        if (entity instanceof OWLProperty  || entity instanceof OWLAnnotationProperty) {
            return getPropertyEntityData(entity);
        }
        else {
            EntityData entityData = new EntityData(entity.getIRI().toString());
            String browserText = getBrowserText(entity);
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
    
    public List<EntityData> getEntityData(Collection<? extends OWLEntity> entities) {
        List<EntityData> result = new ArrayList<EntityData>();
        for(OWLEntity entity : entities) {
            result.add(getEntityData(entity));
        }
        return result;
    }
    
    public EntityData getEntityData(OWLAnnotationValue annotationValue) {
        return annotationValue.accept(new OWLAnnotationValueVisitorEx<EntityData>() {
            public EntityData visit(IRI iri) {
                Set<OWLEntity> entities = project.getRootOntology().getEntitiesInSignature(iri);
                if(entities.isEmpty()) {
                    // Now what?
                    return new EntityData(iri.toString(), iri.toString());
                }
                else {
                    return getEntityData(selectEntity(entities));
                }
            }

            public EntityData visit(OWLAnonymousIndividual individual) {
                return getEntityData(individual);
            }

            public EntityData visit(OWLLiteral literal) {
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
    
    public EntityData getEntityDataFromLexicalValue(String lexicalValue) {
        if(lexicalValue == null) {
            throw new NullPointerException("lexicalValue must not be null");
        }
        OWL2Datatype datatype = OWL2Datatype.RDF_PLAIN_LITERAL;
        if(!lexicalValue.isEmpty()) {
            if(isInteger(lexicalValue)) {
                datatype = OWL2Datatype.XSD_INTEGER;
            }
            else if(isDecimal(lexicalValue)) {
                datatype = OWL2Datatype.XSD_DECIMAL;
            }
            else if(isBoolean(lexicalValue)) {
                datatype = OWL2Datatype.XSD_BOOLEAN;
            }
        }
        OWLDataFactory df = project.getDataFactory();
        OWLLiteral result = df.getOWLLiteral(lexicalValue, datatype);
        return getEntityData(result);

    }



    public boolean isInteger(String lexicalValue) {
        try {
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
//    public EntityData getEntityData(IRI entityIRI) {
//        return getEntityData(entityIRI.toString());
//    }

    public PropertyEntityData getPropertyEntityData(OWLEntity entity) {
        final PropertyEntityData entityData = new PropertyEntityData(entity.getIRI().toString());
        // Don't know if this is correct.
        entityData.setBrowserText(getShortForm(entity));
        entity.accept(new OWLEntityVisitor() {
            public void visit(OWLClass owlClass) {
            }

            public void visit(OWLObjectProperty owlObjectProperty) {
                entityData.setPropertyType(PropertyType.OBJECT);
                // Correct or not? Grrrrr
                entityData.setValueType(ValueType.Instance);
            }

            public void visit(OWLDataProperty owlDataProperty) {
                entityData.setPropertyType(PropertyType.DATATYPE);
                // Correct or not? Grrrrr
                entityData.setValueType(ValueType.Literal);
            }

            public void visit(OWLNamedIndividual owlNamedIndividual) {
            }

            public void visit(OWLDatatype owlDatatype) {
            }

            public void visit(OWLAnnotationProperty owlAnnotationProperty) {
                // Correct or not? Grrrrr
                entityData.setPropertyType(PropertyType.ANNOTATION);
                entityData.setValueType(ValueType.Any);
            }
        });
        return entityData;
    }

//    /**
//     * Gets the property entity data for a specified entity name.
//     * @param entityName The entity name.  This should be a string which represents an IRI, but browser text
//     * (as generated by this RenderingManager) will also be accepted.  Must not be null.
//     * @return The entity data for the specified entity.  Not null.
//     */
//    public PropertyEntityData getPropertyEntityData(String entityName) {
//        OWLEntity entity = getEntity(entityName);
//        return getPropertyEntityData(entity);
//    }

//    /**
//     * A convenience method which ultimately delegates to {@link #getEntity(String)}.
//     * @param entityIRI The IRI of the entity for which the entity data will be returned.
//     * @return The entity data of an entity which has the specified IRI.
//     */
//    public PropertyEntityData getPropertyEntityData(IRI entityIRI) {
//        return getPropertyEntityData(entityIRI.toString());
//    }

    /**
     * Tests to see whether an EntityData is well formed.  An EntityData object is considered well formed if it can be
     * converted to an OWLObject.  Specifically, 
     * @param entityData
     * @return
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
                        OWLEntity entity = project.getDataFactory().getOWLEntity(entityType, iri);
                        if(project.getRootOntology().containsEntityInSignature(entity)) {
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
        OWLDataFactory df = project.getDataFactory();
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
     * @return
     */
    public OWLDatatype getDatatype(ValueType valueType) {
        OWLDataFactory df = project.getDataFactory();
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


    /**
     * Gets the browser text for a given OWLObject.
     * @param object The object.
     * @return The browser text for the object.
     */
    public String getBrowserText(OWLObject object) {
        OWLObjectRenderer owlObjectRenderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();
        if (object instanceof OWLEntity || object instanceof IRI) {
            owlObjectRenderer.setShortFormProvider(shortFormProvider);
        }
        else {
            owlObjectRenderer.setShortFormProvider(new EscapingShortFormProvider(shortFormProvider));
        }
        String browserText = owlObjectRenderer.render(object);
        if(browserText == null) {
            return NULL_BROWSER_TEXT;
        }
        return browserText;
    }


    public String getHTMLBrowserText(OWLObject object) {
        return getHTMLBrowserText(object, new BrowserTextRenderer.HighlightChecker() {
            @Override
            public boolean isHighlighted(OWLEntity entity) {
                return false;
            }
        });
    }

    public String getHTMLBrowserText(OWLObject object, final Set<String> highlightedPhrases) {
        return getHTMLBrowserText(object, new BrowserTextRenderer.HighlightChecker() {
            @Override
            public boolean isHighlighted(OWLEntity entity) {
                return highlightedPhrases.contains(getShortForm(entity));
            }
        });
    }

    public String getHTMLBrowserText(OWLObject object, BrowserTextRenderer.HighlightChecker highlightChecker) {
        BrowserTextRenderer renderer = new BrowserTextRenderer(project);
        return renderer.render(object, highlightChecker, new BrowserTextRenderer.DeprecatedChecker() {
            @Override
            public boolean isDeprecated(OWLEntity entity) {
                return project.isDeprecated(entity);
            }
        });
    }


    /**
     * Selects a single entity from a set of entities.  The selection procedure works by sorting the entities in the
     * set and selecting the first entity.  This makes the choice of entity more predictable over multiple calls for
     * a given set.
     * @param entities The entities.  MUST NOT BE EMPTY!
     * @return A selected entity that is contained in the supplied set of entities.
     * @throws IllegalArgumentException if the set of entities is empty.
     */
    public static OWLEntity selectEntity(Set<OWLEntity> entities) throws IllegalArgumentException {
        if (entities.isEmpty()) {
            throw new IllegalArgumentException("Set of entities must not be empty");
        }
        else if (entities.size() == 1) {
            return entities.iterator().next();
        }
        else {
            List<OWLEntity> entitiesList = new ArrayList<OWLEntity>();
            entitiesList.addAll(entities);
            Collections.sort(entitiesList);
            return entitiesList.get(0);
        }
    }

    public OWLClassData getRendering(OWLClass cls) {
        return new OWLClassData(cls, getShortForm(cls));
    }

    public OWLObjectPropertyData getRendering(OWLObjectProperty property) {
        return new OWLObjectPropertyData(property, getShortForm(property));
    }

    public OWLDataPropertyData getRendering(OWLDataProperty property) {
        return new OWLDataPropertyData(property, getShortForm(property));
    }

    public OWLAnnotationPropertyData getRendering(OWLAnnotationProperty property) {
        return new OWLAnnotationPropertyData(property, getShortForm(property));
    }

    public OWLNamedIndividualData getRendering(OWLNamedIndividual individual) {
        return new OWLNamedIndividualData(individual, getShortForm(individual));
    }

    public OWLDatatypeData getRendering(OWLDatatype datatype) {
        return new OWLDatatypeData(datatype, getShortForm(datatype));
    }
    
    
    
    public Map<OWLEntity, OWLEntityData> getRendering(Set<OWLEntity> entities) {
        final Map<OWLEntity, OWLEntityData> result = new HashMap<OWLEntity, OWLEntityData>();
        for(OWLEntity entity : entities) {
            entity.accept(new OWLEntityVisitor() {
                @Override
                public void visit(OWLClass owlClass) {
                    result.put(owlClass, getRendering(owlClass));
                }

                @Override
                public void visit(OWLObjectProperty property) {
                    result.put(property, getRendering(property));
                }

                @Override
                public void visit(OWLDataProperty property) {
                    result.put(property, getRendering(property));
                }

                @Override
                public void visit(OWLNamedIndividual individual) {
                    result.put(individual, getRendering(individual));
                }

                @Override
                public void visit(OWLDatatype owlDatatype) {
                    result.put(owlDatatype, getRendering(owlDatatype));
                }

                @Override
                public void visit(OWLAnnotationProperty property) {
                    result.put(property, getRendering(property));
                }
            });
        }
        return result;
    }

    public void dispose() {
    }


    private void printBrowserTextReferenceWarningMessage(String referenceName) {
        ProjectId projectId = project.getProjectId();
        WebProtegeLoggerManager.get(RenderingManager.class).info(projectId, "Could not find entity by name \"%s\".  This name may be the browser text rather than an entity IRI.", referenceName);
    }

}
