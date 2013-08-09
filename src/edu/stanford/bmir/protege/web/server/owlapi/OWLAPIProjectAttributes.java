package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.BinaryOWLVersion;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.model.OWLObject;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2012
 */
public final class OWLAPIProjectAttributes {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final List<OWLAPIProjectAttributesListener> listeners = new ArrayList<OWLAPIProjectAttributesListener>();

    private BinaryOWLMetadata metadata = new BinaryOWLMetadata();

    public OWLAPIProjectAttributes() {
    }

    public OWLAPIProjectAttributes(InputStream inputStream) throws IOException {
        try {
            BinaryOWLVersion version = BinaryOWLVersion.getVersion(1);
            BinaryOWLInputStream bos = new BinaryOWLInputStream(inputStream, new OWLDataFactoryImpl(), version);
            this.metadata = new BinaryOWLMetadata(bos);
        }
        catch (BinaryOWLParseException e) {
            throw new IOException(e);
        }
    }

    private OWLAPIProjectAttributes(BinaryOWLMetadata metadata) {
        this.metadata = metadata;
    }

    public synchronized void addListener(OWLAPIProjectAttributesListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeListener(OWLAPIProjectAttributesListener listener) {
        listeners.remove(listener);
    }
    
    private synchronized ArrayList<OWLAPIProjectAttributesListener> getListenersCopy() {
        return new ArrayList<OWLAPIProjectAttributesListener>(listeners);
    }


    private void fireAttributeChanged(String attributeName) {
        for (OWLAPIProjectAttributesListener listener : getListenersCopy()) {
            listener.attributeChanged(this, attributeName);
        }
    }


    private void fireAttributeRemoved(String attributeName) {
        for (OWLAPIProjectAttributesListener listener : getListenersCopy()) {
            listener.attributeRemoved(this, attributeName);
        }
    }

    private void fireAttributesRemoved() {
        for (OWLAPIProjectAttributesListener listener : getListenersCopy()) {
            listener.attributesRemoved(this);
        }
    }

    /**
     * Removes all attributes in this metadata object.
     */
    public void removeAll() {
        try {
            writeLock.lock();
            metadata.removeAll();
        }
        finally {
            writeLock.unlock();
        }
        fireAttributesRemoved();
    }

    /**
     * Removes all types (int, string, long etc.) of attributes with the specified name.
     * @param name The name.
     */
    public void removeAttribute(String name) {
        try {
            writeLock.lock();
            metadata.removeAttribute(name);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeRemoved(name);
    }

    /**
     * Gets the string value of an attribute.
     * @param name The name of the attribute.  Not null.
     * @param defaultValue The default value for the attribute.  May be null.  This value will be returned if this
     * metadata object does not contain a string value for the specified attribute name.
     * @return Either the string value of the attribute with the specified name, or the value specified by the
     *         defaultValue
     *         object if this metadata object does not contain a string value for the specified attribute name.
     * @throws NullPointerException if name is null.
     */
    public String getStringAttribute(String name, String defaultValue) {
        try {
            readLock.lock();
            return metadata.getStringAttribute(name, defaultValue);
        }
        finally {
            readLock.unlock();
        }
    }

    /**
     * Sets the string value of an attribute.
     * @param name The name of the attribute.  Not null.
     * @param value The value of the attribute.  Not null.
     * @throws NullPointerException if the name parameter, or the value parameter is null.
     */
    public void setStringAttribute(String name, String value) {
        try {
            writeLock.lock();
            metadata.setStringAttribute(name, value);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeChanged(name);
    }


    /**
     * Removes a string attribute with the specified name.
     * @param name The name of the attribute.  Not null.
     * @throws NullPointerException if the name parameter is null.
     */
    public void removeStringAttribute(String name) {
        try {
            writeLock.lock();
            metadata.removeStringAttribute(name);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeRemoved(name);
    }

    /**
     * Sets the integer value for the specified attribute name.
     * @param name The name of the attribute. Not null.
     * @param value The value of the attribute to set.
     * @throws NullPointerException if the name parameter is null.
     */
    public void setIntAttribute(String name, int value) {
        try {
            writeLock.unlock();
            metadata.setIntAttribute(name, value);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeChanged(name);
    }

    /**
     * Gets the int value of an attribute.
     * @param name The name of the attribute.  Not null.
     * @param defaultValue The default value for the attribute.  May be null.  This value will be returned if this
     * metadata object does not contain an int value for the specified attribute name.
     * @return Either the int value of the attribute with the specified name, or the value specified by the
     *         defaultValue
     *         object if this metadata object does not contain an int value for the specified attribute name.
     * @throws NullPointerException if name is null.
     */
    public Integer getIntAttribute(String name, Integer defaultValue) {
        try {
            readLock.lock();
            return metadata.getIntAttribute(name, defaultValue);
        }
        finally {
            readLock.unlock();
        }
    }


    /**
     * Removes an int attribute with the specified name.
     * @param name The name of the attribute.  Not null.
     * @throws NullPointerException if the name parameter is null.
     */
    public void removeIntAttribute(String name) {
        try {
            writeLock.lock();
            metadata.removeIntAttribute(name);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeRemoved(name);
    }

    /**
     * Sets the long value for the specified attribute name.
     * @param name The name of the attribute. Not null.
     * @param value The value of the attribute to set.
     * @throws NullPointerException if the name parameter is null.
     */
    public void setLongAttribute(String name, long value) {
        try {
            writeLock.lock();
            metadata.setLongAttribute(name, value);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeChanged(name);
    }


    /**
     * Gets the long value of an attribute.
     * @param name The name of the attribute.  Not null.
     * @param defaultValue The default value for the attribute.  May be null.  This value will be returned if this
     * metadata object does not contain a long value for the specified attribute name.
     * @return Either the long value of the attribute with the specified name, or the value specified by the
     *         defaultValue
     *         object if this metadata object does not contain a long value for the specified attribute name.
     * @throws NullPointerException if name is null.
     */
    public Long getLongAttribute(String name, Long defaultValue) {
        try {
            readLock.lock();
            return metadata.getLongAttribute(name, defaultValue);
        }
        finally {
            readLock.unlock();
        }
    }


    /**
     * Removes a long attribute with the specified name.
     * @param name The name of the attribute.  Not null.
     * @throws NullPointerException if the name parameter is null.
     */
    public void removeLongAttribute(String name) {
        try {
            writeLock.lock();
            metadata.removeLongAttribute(name);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeRemoved(name);
    }

    /**
     * Sets the boolean value for the specified attribute name.
     * @param name The name of the attribute. Not null.
     * @param value The value of the attribute to set.
     * @throws NullPointerException if the name parameter is null.
     */
    public void setBooleanAttribute(String name, boolean value) {
        try {
            writeLock.lock();
            metadata.setBooleanAttribute(name, value);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeChanged(name);
    }

    /**
     * Gets the boolean value of an attribute.
     * @param name The name of the attribute.  Not null.
     * @param defaultValue The default value for the attribute.  May be null.  This value will be returned if this
     * metadata object does not contain a boolean value for the specified attribute name.
     * @return Either the boolean value of the attribute with the specified name, or the value specified by the
     *         defaultValue
     *         object if this metadata object does not contain a boolean value for the specified attribute name.
     * @throws NullPointerException if name is null.
     */
    public Boolean getBooleanAttribute(String name, Boolean defaultValue) {
        try {
            readLock.lock();
            return metadata.getBooleanAttribute(name, defaultValue);
        }
        finally {
            readLock.unlock();
        }
    }


    /**
     * Removes a boolean attribute with the specified name.
     * @param name The name of the attribute.  Not null.
     * @throws NullPointerException if the name parameter is null.
     */
    public void removeBooleanAttribute(String name) {
        try {
            writeLock.lock();
            metadata.removeBooleanAttribute(name);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeRemoved(name);
    }


    /**
     * Sets the double value for the specified attribute name.
     * @param name The name of the attribute. Not null.
     * @param value The value of the attribute to set.
     * @throws NullPointerException if the name parameter is null.
     */
    public void setDoubleAttribute(String name, double value) {
        try {
            writeLock.lock();
            metadata.setDoubleAttribute(name, value);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeChanged(name);
    }

    /**
     * Gets the double value of an attribute.
     * @param name The name of the attribute.  Not null.
     * @param defaultValue The default value for the attribute.  May be null.  This value will be returned if this
     * metadata object does not contain a double value for the specified attribute name.
     * @return Either the double value of the attribute with the specified name, or the value specified by the
     *         defaultValue
     *         object if this metadata object does not contain a double value for the specified attribute name.
     * @throws NullPointerException if name is null.
     */
    public Double getDoubleAttribute(String name, Double defaultValue) {
        try {
            readLock.lock();
            return metadata.getDoubleAttribute(name, defaultValue);
        }
        finally {
            readLock.unlock();
        }
    }


    /**
     * Removes a double attribute with the specified name.
     * @param name The name of the attribute.  Not null.
     * @throws NullPointerException if the name parameter is null.
     */
    public void removeDoubleAttribute(String name) {
        try {
            writeLock.lock();
            metadata.removeDoubleAttribute(name);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeRemoved(name);
    }


    /**
     * Sets the byte [] value for the specified attribute name.  This parameter will be copied so that modifications
     * to the passed in parameter are not reflected in the storage of the parameter in this metadata object.
     * @param name The name of the attribute. Not null.
     * @param value The value of the attribute to set.
     * @throws NullPointerException if the name parameter is null or if the value parameter is null.
     */
    public void setByteArrayAttribute(String name, byte[] value) {
        try {
            writeLock.lock();
            metadata.setByteArrayAttribute(name, value);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeChanged(name);
    }

    /**
     * Gets the byte [] value of an attribute.  This will be a copy of the actual byte [] stored in this metadata
     * object, hence modifications to the return value will not affect the stored value.
     * @param name The name of the attribute.  Not null.
     * @param defaultValue The default value for the attribute.  May be null.  This value will be returned if this
     * metadata object does not contain a byte [] value for the specified attribute name.
     * @return Either the byte [] value of the attribute with the specified name, or the value specified by the
     *         defaultValue
     *         object if this metadata object does not contain a byte [] value for the specified attribute name.
     * @throws NullPointerException if name is null.
     */
    public byte[] getByteArrayAttribute(String name, byte[] defaultValue) {
        try {
            readLock.lock();
            return metadata.getByteArrayAttribute(name, defaultValue);
        }
        finally {
            readLock.unlock();
        }
    }


    /**
     * Removes a byte [] attribute with the specified name.
     * @param name The name of the attribute.  Not null.
     * @throws NullPointerException if the name parameter is null.
     */
    public void removeByteArrayAttribute(String name) {
        try {
            writeLock.lock();
            metadata.removeByteArrayAttribute(name);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeRemoved(name);
    }


    /**
     * Gets the {@link org.semanticweb.owlapi.model.OWLObject} list value of an attribute.
     * @param name The name of the attribute.  Not null.
     * @param defaultValue The default value for the attribute.  May be null.  This value will be returned if this
     * metadata object does not contain an {@link org.semanticweb.owlapi.model.OWLObject} list value for the specified
     * attribute name.
     * @return Either the {@link org.semanticweb.owlapi.model.OWLObject} list value of the attribute with the specified
     *         name, or the value specified by the defaultValue
     *         object if this metadata object does not contain an {@link org.semanticweb.owlapi.model.OWLObject} list
     *         value for the specified attribute name.
     * @throws NullPointerException if name is null.
     */
    public List<OWLObject> getOWLObjectListAttribute(String name, List<OWLObject> defaultValue) {
        try {
            readLock.lock();
            return metadata.getOWLObjectListAttribute(name, defaultValue);
        }
        finally {
            readLock.unlock();
        }
    }

    /**
     * Sets the {@link OWLObject} list values for an attribute.
     * @param name The name of the attribute.  Not null.
     * @param value The value of the attribute.  Not null.
     * @throws NullPointerException if name is null or value is null.
     */
    public void setOWLObjectListAttribute(String name, List<OWLObject> value) {
        try {
            writeLock.lock();
            metadata.setOWLObjectListAttribute(name, value);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeChanged(name);
    }


    /**
     * Removes an {@link OWLObject} list attribute with the specified name.
     * @param name The name of the attribute.  Not null.
     * @throws NullPointerException if the name parameter is null.
     */
    public void removeOWLObjectListAttribute(String name) {
        try {
            writeLock.lock();
            metadata.removeOWLObjectListAttribute(name);
        }
        finally {
            writeLock.unlock();
        }
        fireAttributeRemoved(name);
    }


    public void write(OutputStream output) throws IOException {
        try {
            writeLock.lock();
            metadata.write(new BinaryOWLOutputStream(output, BinaryOWLVersion.getVersion(1)));
        }
        finally {
            writeLock.unlock();
        }
    }


}
