package edu.stanford.bmir.protege.web.server.db.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import edu.stanford.bmir.protege.web.server.settings.Settings;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/03/2013
 */
public class SettingsMongoDBImpl implements Settings {

    private String dbName;

    private String settings;

    public SettingsMongoDBImpl(String dbName) {
        this.dbName = dbName;
    }


    private BasicDBObject getSettingsObject() {
        DB db = getDB();
        DBCollection settingsCollection = db.getCollection("settings");
        DBObject dbObject = settingsCollection.findOne();
        return (BasicDBObject) dbObject;
    }

    private DB getDB() {
        return MongoDBManager.get().getClient().getDB(dbName);
    }

    @Override
    public void clear(String propertyName) {
        final BasicDBObject settingsObject = getSettingsObject();
        settingsObject.remove(propertyName);
        getSettingsCollection().save(settingsObject);
    }

    private DBCollection getSettingsCollection() {
        return getDB().getCollection("settings");
    }

    @Override
    public String getStringValue(String propertyName, String defaultValue) {
        BasicDBObject basicDBObject = getSettingsObject();
        return basicDBObject.getString(propertyName, defaultValue);

    }


    @Override
    public List<String> getStringValues(String propertyName, List<String> defaultValues) {
        return null;
    }

    @Override
    public void setStringValue(String propertyName, String value) {
    }

    @Override
    public void setStringValues(String propertyName, List<String> values) {
    }

    @Override
    public Integer getIntegerValue(String propertyName, Integer defaultValue) throws NumberFormatException {
        return null;
    }

    @Override
    public List<Integer> getIntegerValues(String propertyName, List<Integer> defaultValues) throws NumberFormatException {
        return null;
    }

    @Override
    public void setIntegerValue(String propertyName, Integer value) {
    }

    @Override
    public void setIntegerValues(String propertyName, List<Integer> values) {
    }

    @Override
    public Long getLongValue(String propertyName, Long defaultValue) throws NumberFormatException {
        return null;
    }

    @Override
    public List<Long> getLongValues(String propertyName, List<Long> defaultValues) throws NumberFormatException {
        return null;
    }

    @Override
    public void setLongValue(String propertyName, Long value) {
    }

    @Override
    public void setLongValues(String propertyName, List<Long> values) {
    }

    @Override
    public Double getDoubleValue(String propertyName, Double defaultValue) throws NumberFormatException {
        return null;
    }

    @Override
    public List<Double> getDoubleValues(String propertyName, List<Double> defaultValues) throws NumberFormatException {
        return null;
    }

    @Override
    public void setDoubleValue(String propertyName, Double value) {
    }

    @Override
    public void setDoubleValues(String propertyName, List<Double> values) {
    }

    @Override
    public Boolean getBooleanValue(String propertyName, Boolean defaultValue) {
        return null;
    }

    @Override
    public void setBooleanValue(String propertyName, Boolean value) {
    }
}
