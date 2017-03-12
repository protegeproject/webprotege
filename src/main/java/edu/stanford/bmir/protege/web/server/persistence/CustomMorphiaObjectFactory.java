package edu.stanford.bmir.protege.web.server.persistence;

import org.mongodb.morphia.mapping.DefaultCreator;
import org.mongodb.morphia.mapping.MappingException;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2017
 */
public class CustomMorphiaObjectFactory extends DefaultCreator {

    // Taken from https://blog.jayway.com/2012/02/28/configure-morphia-to-work-without-a-default-constructor/

    @Override
    @SuppressWarnings("unchecked")
    public Object createInstance(Class cls) {
        try {
            final Constructor constructor = getNoArgsConstructor(cls);
            if(constructor != null) {
                return constructor.newInstance();
            }
            try {
                return ReflectionFactory.getReflectionFactory().newConstructorForSerialization(cls, Object.class.getDeclaredConstructor(null)).newInstance(null);
            } catch (Exception e) {
                throw new MappingException("Failed to instantiate " + cls.getName(), e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Constructor getNoArgsConstructor(final Class constructorType) {
        try {
            Constructor ctor = constructorType.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
