package edu.stanford.bmir.protege.web.server;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.tools.ant.filters.StringInputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2012
 */
public class AttributesStoreImpl implements AttributesStore {


    private static final String STRING_TYPE_NAME = "string";

    
    
    private static final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
    
    private static final Lock READ_LOCK = READ_WRITE_LOCK.readLock();
    
    private static final Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();

    public static final String INTEGER_TYPE_NAME = "integer";

    public static final String LONG_TYPE_NAME = "long";

    public static final String DOUBLE_TYPE_NAME = "double";

    public static final String BOOLEAN_TYPE_NAME = "boolean";

    public static final String UTF_8 = "utf-8";


    private final Map<String, List<String>> stringAttributesValuesMap = new HashMap<String, List<String>>();

    private final  Map<String, List<Integer>> integerAttributesValuesMap = new HashMap<String, List<Integer>>();
    
    private final  Map<String, List<Long>> longAttributesValuesMap = new HashMap<String, List<Long>>();

    private final  Map<String, List<Double>> doubleAttributeValuesMap = new HashMap<String, List<Double>>();
    
    private final  Map<String, List<Boolean>> booleanAttributeValuesMap = new HashMap<String, List<Boolean>>();


    private final File file;

    private final SaveMode saveMode;

    /**
     * Constructs an AttributesStoreImpl which is based on the specified file for storage.
     * @param file The file where attribute values will be saved and read from
     * @param saveMode The save mode AUTO_SAVE or MANUAL_SAVE
     */
    private AttributesStoreImpl(File file, SaveMode saveMode) {
        this.file = file;
        this.saveMode = saveMode;
        if(saveMode == SaveMode.AUTO_SAVE) {
//            Runtime.getRuntime().addShutdownHook(new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        save();
//                    }
//                    catch (IOException e){
//                        throw new RuntimeException(e);
//                    }
//                }
//            });
        }
        if(file.exists()) {
            readFromFile();
        }
    }


    public static AttributesStoreImpl createAutoSavingAttributesStore(File backingFile) {
        return new AttributesStoreImpl(backingFile, SaveMode.AUTO_SAVE);
    }
    
    public static AttributesStoreImpl createAttributesStore(File backingFile) {
        return new AttributesStoreImpl(backingFile, SaveMode.MANUAL_SAVE);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Generic Handline of values.

    private <T> T getValue(String attributeName, T defaultValue, Map<String, List<T>> valuesMap) {
        try {
            READ_LOCK.lock();
            if(attributeName == null) {
                throw new NullPointerException("attributeName must not be null");
            }
            List<T> values = valuesMap.get(attributeName);
            if(values == null) {
                return defaultValue;
            }
            if(values.isEmpty()) {
                return defaultValue;
            }
            return values.get(0);
        }
        finally {
            READ_LOCK.unlock();
        }
    }
    
    private <T> List<T> getValues(String attributeName, List<T> defaultValues, Map<String, List<T>> valuesMap) {
        try {
            READ_LOCK.lock();
            if(attributeName == null) {
                throw new NullPointerException("attributeName must not be null");
            }
            List<T> values = valuesMap.get(attributeName);
            if(values == null) {
                return defaultValues;
            }
            return new ArrayList<T>(values);
        }
        finally {
            READ_LOCK.unlock();
        }
    }
    
    private <T> void setValue(String attributeName, T value, Map<String, List<T>> valuesMap) {
        try {
            WRITE_LOCK.lock();
            if(attributeName == null) {
                throw new NullPointerException("attributeName must not be null");
            }
            if(value == null) {
                throw new NullPointerException("value must not be null");
            }
            List<T> values = valuesMap.get(attributeName);
            if(values == null) {
                values = new ArrayList<T>(2);
                valuesMap.put(attributeName, values);
            }
            values.clear();
            values.add(value);
            saveAsynchronously();
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }
    
    private <T> void setValues(String attributeName, List<T> values, Map<String, List<T>> valuesMap) {
        try {
            WRITE_LOCK.lock();
            if(attributeName == null) {
                throw new NullPointerException("attributeName must not be null");
            }
            if(values == null) {
                throw new NullPointerException("values must not be null");
            }
            List<T> existingValues = valuesMap.get(attributeName);
            if(existingValues == null) {
                existingValues = new ArrayList<T>(values.size() + 1);
                valuesMap.put(attributeName, existingValues);
            }
            existingValues.clear();
            existingValues.addAll(values);
            saveAsynchronously();
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }
    
    private <T> void addValue(String attributeName, T value, Map<String, List<T>> valuesMap) {
        try {
            WRITE_LOCK.lock();
            if(attributeName == null) {
                throw new NullPointerException("attributeName must not be null");
            }
            if(value == null) {
                throw new NullPointerException("value must not be null");
            }
            List<T> values = valuesMap.get(attributeName);
            if(values == null) {
                values = new ArrayList<T>();
                valuesMap.put(attributeName, values);
            }
            values.add(value);
            saveAsynchronously();
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }
    
    private <T> void clearValues(String attributeName, Map<String, List<T>> valuesMap) {
        try {
            WRITE_LOCK.lock();
            valuesMap.remove(attributeName);
            saveAsynchronously();
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }

    public void clearAll() {
        try {
            WRITE_LOCK.lock();
            stringAttributesValuesMap.clear();
            integerAttributesValuesMap.clear();
            longAttributesValuesMap.clear();
            doubleAttributeValuesMap.clear();
            booleanAttributeValuesMap.clear();
            saveAsynchronously();
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    

    public String getStringValue(String attributeName, String defaultValue) {
        return getValue(attributeName, defaultValue, stringAttributesValuesMap);
    }

    public List<String> getStringValues(String attributeName, List<String> defaultValues) {
        return getValues(attributeName, defaultValues, stringAttributesValuesMap);
    }

    public void setStringValue(String attributeName, String value) {
        setValue(attributeName, value, stringAttributesValuesMap);
    }

    public void setStringValues(String attributeName, List<String> values) {
        setValues(attributeName, values, stringAttributesValuesMap);
    }

    public Integer getIntegerValue(String attributeName, Integer defaultValue) {
        return getValue(attributeName, defaultValue, integerAttributesValuesMap);
    }

    public List<Integer> getIntegerValues(String attributeName, List<Integer> defaultValues) {
        return getValues(attributeName, defaultValues, integerAttributesValuesMap);
    }

    public void setIntegerValue(String attributeName, Integer value) {
        setValue(attributeName, value, integerAttributesValuesMap);
    }

    public void setIntegerValues(String attributeName, List<Integer> values) {
        setValues(attributeName, values, integerAttributesValuesMap);
    }

    public Long getLongValue(String attributeName, Long defaultValue) throws NumberFormatException {
        return getValue(attributeName, defaultValue, longAttributesValuesMap);
    }

    public List<Long> getLongValues(String attributeName, List<Long> defaultValues) throws NumberFormatException {
        return getValues(attributeName, defaultValues, longAttributesValuesMap);
    }

    public void setLongValue(String attributeName, Long value) {
        setValue(attributeName, value, longAttributesValuesMap);
    }

    public void setLongValues(String attributeName, List<Long> values) {
        setValues(attributeName, values, longAttributesValuesMap);
    }

    public Double getDoubleValue(String attributeName, Double defaultValue) throws NumberFormatException {
        return getValue(attributeName, defaultValue, doubleAttributeValuesMap);
    }

    public List<Double> getDoubleValues(String attributeName, List<Double> defaultValues) throws NumberFormatException {
        return getValues(attributeName, defaultValues, doubleAttributeValuesMap);
    }

    public void setDoubleValue(String attributeName, Double value) {
        setValue(attributeName, value, doubleAttributeValuesMap);
    }

    public void setDoubleValues(String attributeName, List<Double> values) {
        setValues(attributeName, values, doubleAttributeValuesMap);
    }

    public Boolean getBooleanValue(String attributeName, Boolean defaultValue) {
        return getValue(attributeName, defaultValue, booleanAttributeValuesMap);
    }

    public void setBooleanValue(String attributeName, Boolean value) {
        setValue(attributeName, value, booleanAttributeValuesMap);
    }

    public void clearStringValues(String attributeName) {
        clearValues(attributeName, stringAttributesValuesMap);
    }


    public void clearIntegerValues(String attributeName) {
        clearValues(attributeName, integerAttributesValuesMap);
    }


    public void clearLongValues(String attributeName) {
        clearValues(attributeName, longAttributesValuesMap);
    }


    public void clearDoubleValues(String attributeName) {
        clearValues(attributeName, doubleAttributeValuesMap);
    }


    public void clearBooleanValues(String attributeName) {
        clearValues(attributeName, booleanAttributeValuesMap);
    }
    



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private static AtomicInteger counter = new AtomicInteger();
    
    private ExecutorService saveExecutor = Executors.newSingleThreadExecutor();

    private void saveAsynchronously() {
        
        saveExecutor.submit(new Callable<Object>() {
        
            private int counterI = counter.getAndIncrement();
                
            public Object call() throws IOException {
                System.out.println("Saving " + counterI);
                save();
                return true;
            }
        });

    }
    
    private void save() throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        write(bos);
        bos.close();
    }

    private void write(OutputStream os) {
        Map<String, List<String>> stringAttributeValuesCopy;
        Map<String, List<Integer>> integerAttributeValuesCopy;
        Map<String, List<Long>> longAttributeValuesCopy;
        Map<String, List<Double>> doubleAttributeValuesCopy;
        Map<String, List<Boolean>> booleanAttributeValuesCopy;
        try {
            READ_LOCK.lock();

            stringAttributeValuesCopy = new HashMap<String, List<String>>(stringAttributesValuesMap);
            integerAttributeValuesCopy = new HashMap<String, List<Integer>>(integerAttributesValuesMap);
            longAttributeValuesCopy = new HashMap<String, List<Long>>(longAttributesValuesMap);
            doubleAttributeValuesCopy = new HashMap<String, List<Double>>(doubleAttributeValuesMap);
            booleanAttributeValuesCopy = new HashMap<String, List<Boolean>>(booleanAttributeValuesMap);
        }
        finally {
            READ_LOCK.unlock();
        }

        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, UTF_8), true);
            pw.println("<?xml version='1.0' encoding='" + UTF_8 + "'?>");
            pw.println("<attributes>");
            write(stringAttributeValuesCopy, STRING_TYPE_NAME, pw);
            write(integerAttributeValuesCopy, INTEGER_TYPE_NAME, pw);
            write(longAttributeValuesCopy, LONG_TYPE_NAME, pw);
            write(doubleAttributeValuesCopy, DOUBLE_TYPE_NAME, pw);
            write(booleanAttributeValuesCopy, BOOLEAN_TYPE_NAME, pw);
            pw.println("</attributes>");
            pw.flush();
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Broken JVM:  UTF-8 Not supported");
        }

    }
    
    
    private <T> void write(Map<String, List<T>> valuesMap, String typeName, PrintWriter pw) {
        for(String att : valuesMap.keySet()) {
            for (T value : valuesMap.get(att)) {
                pw.println("\t<entry type=\"" + StringEscapeUtils.escapeXml(typeName) + "\">");
                pw.print("\t\t<attribute>");
                pw.print(StringEscapeUtils.escapeXml(att));
                pw.println("</attribute>");
                pw.print("\t\t<value>");
                pw.print(StringEscapeUtils.escapeXml(value.toString()));
                pw.println("</value>");
                pw.println("\t</entry>");
            }
        }
    }

    private void readFromFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream is = new BufferedInputStream(fileInputStream);
            read(is);
            is.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void read(InputStream is) {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(is, new AttributesParserHandler());
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
    }
    
    

    
    
    
    
    
    private class AttributesParserHandler extends DefaultHandler {

        public static final String ENTRY_ELEMENT_NAME = "entry";

        public static final String ATTRIBUTE_ELEMENT_NAME = "attribute";

        public static final String VALUE_ELEMENT_NAME = "value";

        private String typeName;

        private StringBuilder textContent;

        private String attributeName;

        private String attributeLexicalValue;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if(ENTRY_ELEMENT_NAME.equals(qName)) {
                typeName = attributes.getValue("type");
            }
            else if(ATTRIBUTE_ELEMENT_NAME.equals(qName)) {
                textContent = new StringBuilder();
            }
            else if(VALUE_ELEMENT_NAME.equals(qName)) {
                textContent = new StringBuilder();
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (textContent != null) {
                textContent.append(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(ATTRIBUTE_ELEMENT_NAME.equals(qName)) {
                attributeName = getLastTextContent();
            }
            else if(VALUE_ELEMENT_NAME.equals(qName)) {
                attributeLexicalValue = getLastTextContent();
            }
            else if(ENTRY_ELEMENT_NAME.equals(qName)) {
                addParsedValue(typeName, attributeName, attributeLexicalValue);
            }
        }

        private String getLastTextContent() {
            String trimmedContent = textContent.toString().trim();
            return StringEscapeUtils.unescapeXml(trimmedContent);
        }

        private void addParsedValue(String typeName, String attributeName, String lexicalValue) {
            if(STRING_TYPE_NAME.equals(typeName)) {
                addValue(attributeName, lexicalValue, stringAttributesValuesMap);
            }
            else if(INTEGER_TYPE_NAME.equals(typeName)) {
                addValue(attributeName, Integer.parseInt(lexicalValue), integerAttributesValuesMap);
            }
            else if(LONG_TYPE_NAME.equals(typeName)) {
                addValue(attributeName, Long.parseLong(lexicalValue), longAttributesValuesMap);
            }
            else if(DOUBLE_TYPE_NAME.equals(typeName)) {
                addValue(attributeName, Double.parseDouble(lexicalValue), doubleAttributeValuesMap);
            }
            else if(BOOLEAN_TYPE_NAME.equals(typeName)) {
                addValue(attributeName, Boolean.parseBoolean(lexicalValue), booleanAttributeValuesMap);
            }
            else {
                throw new RuntimeException("Unknown type name: " + typeName);
            }
        }
    }

    @Override
    public String toString() {
        try {
            READ_LOCK.lock();
            return String.format("AttributeValues(%s %s %s %s %s)", stringAttributesValuesMap, integerAttributesValuesMap, longAttributesValuesMap, doubleAttributeValuesMap, booleanAttributeValuesMap);
        }
        finally {
            WRITE_LOCK.lock();
        }
    }

    public static void main(String[] args) throws IOException, SAXException {
        AttributesStoreImpl store = AttributesStoreImpl.createAutoSavingAttributesStore(new File("/tmp/atts.txt"));
        store.setBooleanValue("test", true);
        store.setIntegerValue("age", 33);
        store.setLongValues("height", Arrays.asList(1l, 33l, 55l));
        store.setDoubleValue("myProp", 33.3);
        store.setStringValue("prop", "<% 4dfg>");

        System.out.println(store);
    }
    
    
    
    private static enum SaveMode {
        
        MANUAL_SAVE,
        
        AUTO_SAVE
    }
}
