package edu.stanford.bmir.protege.web.server.settings;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.*;
import org.xml.sax.SAXException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2012
 */
public class SettingsImpl implements Settings {


    private static final String STRING_TYPE_NAME = "string";

    
    
    private static final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
    
    private static final Lock READ_LOCK = READ_WRITE_LOCK.readLock();
    
    private static final Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();

    public static final String INTEGER_TYPE_NAME = "integer";

    public static final String LONG_TYPE_NAME = "long";

    public static final String DOUBLE_TYPE_NAME = "double";

    public static final String BOOLEAN_TYPE_NAME = "boolean";

    public static final String UTF_8 = "utf-8";


    private final Multimap<String, Object> store;



    private final File file;

    private final SaveMode saveMode;



    /**
     * Constructs an AttributesStoreImpl which is based on the specified file for storage.
     * @param file The file where attribute values will be saved and read from
     * @param saveMode The save mode AUTO_SAVE or MANUAL_SAVE
     */
    private SettingsImpl(File file, SaveMode saveMode) {
        this.file = file;
        this.saveMode = saveMode;
        this.store = HashMultimap.create();
        if(saveMode == SaveMode.AUTO_SAVE) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        save();
                    }
                    catch (IOException e){
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        if(file.exists()) {
            readFromFile();
        }
    }



    public static SettingsImpl createAutoSavingAttributesStore(File backingFile) {
        return new SettingsImpl(backingFile, SaveMode.AUTO_SAVE);
    }
    
    public static SettingsImpl createAttributesStore(File backingFile) {
        return new SettingsImpl(backingFile, SaveMode.MANUAL_SAVE);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private <T> T getValue(String attributeName, Class<T> cls, T defaultValue) {
        try {
            READ_LOCK.lock();
            Collection<Object> values = store.get(attributeName);
            for(Object value : values) {
                if(cls.isInstance(value)) {
                    return cls.cast(value);
                }
            }
            return defaultValue;
        }
        finally {
            READ_LOCK.unlock();
        }
    }

    private <T> List<T> getValues(String attributeName, Class<T> cls, List<T> defaultValues) {
        try {
            WRITE_LOCK.lock();
            final Collection<Object> storedValues = store.get(attributeName);
            if(storedValues.isEmpty()) {
                return defaultValues;
            }
            List<T> result = new ArrayList<T>();
            for(Object value : storedValues) {
                if(cls.isInstance(value)) {
                    result.add(cls.cast(value));
                }
            }
            return result;
        }
        finally {
            READ_LOCK.unlock();
        }
    }

    private void setValue(String attributeName, Object value) {
        checkNotNull(attributeName, "attributeName must not be null");
        checkNotNull(value, "value must not be null");
        setValues(attributeName, Arrays.asList(value));
    }

    private void setValues(String attributeName, List<?> values) {
        checkNotNull(attributeName, "attributeName must not be null");
        checkNotNull(values, "values must not be null");
        for(Object value : values) {
            checkNotNull(value, "value in specified list is null");
        }

        try {
            WRITE_LOCK.lock();
            Collection<?> replacedValues = store.replaceValues(attributeName, values);
            if(!replacedValues.equals(values)) {
                // TODO: Broadcast?
            }
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }

    public void clearValues(String attributeName) {
        try {
            WRITE_LOCK.lock();
            store.removeAll(attributeName);
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void clear(String propertyName) {
        clearValues(propertyName);
    }

    public String getStringValue(String attributeName, String defaultValue) {
        return getValue(attributeName, String.class, defaultValue);
    }

    public List<String> getStringValues(String attributeName, List<String> defaultValues) {
        return getValues(attributeName, String.class, defaultValues);
    }

    public void setStringValue(String attributeName, String value) {
        setValue(attributeName, value);
    }

    public void setStringValues(String attributeName, List<String> values) {
        setValues(attributeName, values);
    }

    public Integer getIntegerValue(String attributeName, Integer defaultValue) {
        return getValue(attributeName, Integer.class, defaultValue);
    }

    public List<Integer> getIntegerValues(String attributeName, List<Integer> defaultValues) {
        return getValues(attributeName, Integer.class, defaultValues);
    }

    public void setIntegerValue(String attributeName, Integer value) {
        setValue(attributeName, value);
    }

    public void setIntegerValues(String attributeName, List<Integer> values) {
        setValues(attributeName, values);
    }

    public Long getLongValue(String attributeName, Long defaultValue) throws NumberFormatException {
        return getValue(attributeName, Long.class, defaultValue);
    }

    public List<Long> getLongValues(String attributeName, List<Long> defaultValues) throws NumberFormatException {
        return getValues(attributeName, Long.class, defaultValues);
    }

    public void setLongValue(String attributeName, Long value) {
        setValue(attributeName, value);
    }

    public void setLongValues(String attributeName, List<Long> values) {
        setValues(attributeName, values);
    }

    public Double getDoubleValue(String attributeName, Double defaultValue) throws NumberFormatException {
        return getValue(attributeName, Double.class, defaultValue);
    }

    public List<Double> getDoubleValues(String attributeName, List<Double> defaultValues) throws NumberFormatException {
        return getValues(attributeName, Double.class, defaultValues);
    }

    public void setDoubleValue(String attributeName, Double value) {
        setValue(attributeName, value);
    }

    public void setDoubleValues(String attributeName, List<Double> values) {
        setValues(attributeName, values);
    }

    public Boolean getBooleanValue(String attributeName, Boolean defaultValue) {
        return getValue(attributeName, Boolean.class, defaultValue);
    }

    public void setBooleanValue(String attributeName, Boolean value) {
        setValue(attributeName, value);
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

    /**
     * Creates a copy of the store in a thread safe way.
     * @return A copy of the store.
     */
    private Multimap<String, Object> createStoreCopy() {
        try {
            READ_LOCK.lock();
            return HashMultimap.create(store);
        }
        finally {
            READ_LOCK.unlock();
        }

    }

    private void write(OutputStream os) throws IOException {
        Multimap<String, Object> copy = createStoreCopy();
        JsonObject rootElement = new JsonObject();
        for(String key : copy.keySet()) {
            Collection values = copy.get(key);
            JsonArray valuesElement = new JsonArray();
            for(Object value : values) {
                if(value instanceof String) {
                    valuesElement.add(new JsonPrimitive((String) value));
                }
                else if(value instanceof Number) {
                    valuesElement.add(new JsonPrimitive((Number) value));
                }
                else if(value instanceof Boolean) {
                    valuesElement.add(new JsonPrimitive((Boolean) value));
                }
            }
            rootElement.add(key, valuesElement);
        }
        Gson gson = createGSON();
        String s = gson.toJson(rootElement);
        final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(os);
        outputStreamWriter.write(s);
        outputStreamWriter.flush();
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

    @SuppressWarnings("unchecked")
    private void read(InputStream is) {
        Gson gson = createGSON();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Multimap<String, Object> multimap = gson.fromJson(reader, Multimap.class);
        if (multimap != null) {
            try {
                WRITE_LOCK.lock();
                store.clear();
                store.putAll(multimap);
            }
            finally {
                WRITE_LOCK.unlock();
            }
        }
    }



    private Gson createGSON() {
        return new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().registerTypeHierarchyAdapter(Multimap.class, new JsonTypeAdapter()).create();
    }


    @Override
    public String toString() {
        return String.format("Settings(%s)", store);
    }

    public static void main(String[] args) throws IOException, SAXException {
        SettingsImpl store = SettingsImpl.createAutoSavingAttributesStore(new File("/tmp/atts.txt"));
        store.setBooleanValue("test", true);
        store.setIntegerValue("age", 33);
        store.setLongValues("height", Arrays.asList(1l, 33l, 55l));
        store.setDoubleValue("myProp", 33.3);
        store.setStringValue("prop", "{% 4d\\\"f\"g}");
        store.save();
        System.out.println(store);
    }
    
    
    
    private static enum SaveMode {
        
        MANUAL_SAVE,
        
        AUTO_SAVE
    }



    private class JsonTypeAdapter implements JsonSerializer<Multimap>, JsonDeserializer<Multimap> {


        @Override
        public Multimap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Multimap<String, Object> result = HashMultimap.create();
            JsonObject root = json.getAsJsonObject();
            final Set<Map.Entry<String, JsonElement>> entries = root.entrySet();
            for(Map.Entry<String,JsonElement> entry : entries) {
                String key = entry.getKey();
                JsonArray values = entry.getValue().getAsJsonArray();
                Set<Object> currentValues = new HashSet<Object>(values.size());
                for(JsonElement value : values) {
                    JsonPrimitive primitiveValue = value.getAsJsonPrimitive();
                    if(primitiveValue.isString()) {
                        currentValues.add(primitiveValue.getAsString());
                    }
                    else if(primitiveValue.isNumber()) {
                        currentValues.add(primitiveValue.getAsNumber());
                    }
                    else if(primitiveValue.isBoolean()) {
                        currentValues.add(primitiveValue.getAsBoolean());
                    }
                }
                result.putAll(key, currentValues);

            }
            return result;
        }

        @Override
        public JsonElement serialize(Multimap src, Type typeOfSrc, JsonSerializationContext context) {
            Multimap<String, Object> copy = createStoreCopy();
            JsonObject rootElement = new JsonObject();
            for(String key : copy.keySet()) {
                Collection values = copy.get(key);
                JsonArray valuesElement = new JsonArray();
                for(Object value : values) {
                    if(value instanceof String) {
                        valuesElement.add(new JsonPrimitive((String) value));
                    }
                    else if(value instanceof Number) {
                        valuesElement.add(new JsonPrimitive((Number) value));
                    }
                    else if(value instanceof Boolean) {
                        valuesElement.add(new JsonPrimitive((Boolean) value));
                    }
                }
                rootElement.add(key, valuesElement);
            }
            return rootElement;
        }
    }
}
