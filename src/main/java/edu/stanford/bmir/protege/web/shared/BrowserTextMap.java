package edu.stanford.bmir.protege.web.shared;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/12/2012
 * <p>
 *     An immutable map which maps {@link OWLEntity} objects to {@link String} objects representing the browser text.
 * </p>
 */
public class BrowserTextMap implements Serializable {

    private Map<OWLEntity, String> map = new HashMap<OWLEntity, String>();

    private BrowserTextMap() {

    }

    public BrowserTextMap(Map<OWLEntity, String> map) {
        this.map = new HashMap<OWLEntity, String>(map);
    }

    public BrowserTextMap(HasSignature hasSignature, BrowserTextProvider browserTextProvider) {
        this(hasSignature.getSignature(), browserTextProvider);
    }

//    public BrowserTextMap(BrowserTextProvider browserTextProvider, HasSignature ... hasSignatures) {
//        for(HasSignature hasSignature : hasSignatures) {
//            for(OWLEntity entity : hasSignature.getSignature()) {
//                Optional<String> value = browserTextProvider.getOWLEntityBrowserText(entity);
//                if(value.isPresent()) {
//                    map.put(entity, value.get());
//                }
//            }
//        }
//    }
//
//    public BrowserTextMap(OWLObject object, BrowserTextProvider browserTextProvider) {
//        this(object.getSignature(), browserTextProvider);
//    }


    public BrowserTextMap(Set<? extends OWLObject> signature, BrowserTextProvider browserTextProvider) {
        for(OWLObject object : signature) {
            for (OWLEntity entity : object.getSignature()) {
                Optional<String> value = browserTextProvider.getOWLEntityBrowserText(entity);
                if(value.isPresent()) {
                    map.put(entity, value.get());
                }
            }
        }
    }





    public Collection<OWLEntityData> getOWLEntityData() {
        Set<OWLEntityData> result = new HashSet<OWLEntityData>();
        for(OWLEntity entity : map.keySet()) {
            result.add(DataFactory.getOWLEntityData(entity, map.get(entity)));
        }
        return result;
    }



    public Optional<String> getBrowserText(OWLEntity entity) {
        String value = map.get(entity);
        if(value == null) {
            return Optional.absent();
        }
        else {
            return Optional.of(value);
        }
    }

    /**
     * A convenience method which builds a browser text map from the specified browser text provider for (possibly a subset of)
     * the objects in the list of specified objects.
     * @param browserTextProvider The browser text provider that is used to obtain the browser text of objects.
     * @param objects The objects.  May be empty.  Not {@code null}.  Browser text is generated for the signature of
     * each object.  This means that objects in the list are processed as follows: If an object is an {@link OWLObject}
     * then its signature is obtained and processed.  If the object is an instance of {@link HasSignature} then its
     * signature is obtained and processed.  If an object is a collection then its contained objects are processed
     * recursively.
     * @return A BrowserTextMap for the signature of objects in the list.
     * {@link HasSignature} objects.
     */
    public static BrowserTextMap build(BrowserTextProvider browserTextProvider, Object ... objects) {
        checkNotNull(objects);
        Builder builder = new Builder();
        for(Object o : objects) {
            build(builder, o);
        }
        return builder.build(browserTextProvider);
    }

    /**
     * Adds the signature of the specified object to the specified builder.
     * @param builder The builder to add the signature to.
     * @param o The object whose signature will be extracted and added to the builder.  If the object is an {@link OWLObject}
     * then its signature is obtained by casting it to an {@link OWLObject} and calling {@link org.semanticweb.owlapi.model.OWLObject#getSignature()},
     * if the object is an instance of {@link HasSignature} then its signature is obtained by casting it to {@link HasSignature}
     * and calling {@link edu.stanford.bmir.protege.web.shared.HasSignature#getSignature()}, finally, if the object is
     * a collection then each object in the collection is processed recursively with this method.
     */
    private static void build(Builder builder, Object o) {
        if(o instanceof OWLObject) {
            builder.addAll(((OWLObject) o).getSignature());
        }
        else if(o instanceof HasSignature) {
            builder.addAll((HasSignature) o);
        }
        else if(o instanceof Collection) {
            handleCollection(builder, (Collection) o);
        }
    }

    private static void handleCollection(Builder builder, Collection<?> collection) {
        for(Object o : collection) {
            build(builder, o);
        }
    }


    public static class Builder {

        private Set<OWLEntity> signature = new HashSet<OWLEntity>();

        public void add(OWLEntity entity) {
            signature.add(entity);
        }

        public void addAll(OWLEntity ... entities) {
            Collections.addAll(signature, entities);
        }

        public void addAll(Collection<? extends OWLEntity> entities) {
            signature.addAll(entities);
        }

        public void addAll(HasSignature ... hasSignatures) {
            for(HasSignature hasSignature : hasSignatures) {
                addAll(hasSignature.getSignature());
            }
        }

        public BrowserTextMap build(BrowserTextProvider browserTextProvider) {
            BrowserTextMap map = new BrowserTextMap();
            for(OWLEntity entity : signature) {
                Optional<String> bt = browserTextProvider.getOWLEntityBrowserText(entity);
                if(bt.isPresent()) {
                    map.map.put(entity, bt.get());
                }
            }
            return map;
        }



    }
}
