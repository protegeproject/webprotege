package edu.stanford.bmir.protege.web.client.model.event;

public class EventType {

	private static final int BASE = 100;

	public static final int ENTITY_CREATED = BASE + 1;
	public static final int CLASS_CREATED = BASE + 2;
	public static final int PROPERTY_CREATED = BASE + 3;
	public static final int INDIVIDUAL_CREATED = BASE + 4;

	public static final int ENTITY_DELETED = BASE + 101;
	public static final int CLASS_DELETED = BASE + 102;
	public static final int PROPERTY_DELETED = BASE + 103;
	public static final int INDIVIDUAL_DELETED = BASE + 104;

	public static final int PROPERTY_VALUE_ADDED = BASE + 201;
	public static final int PROPERTY_VALUE_REMOVED = BASE + 202;
	public static final int PROPERTY_VALUE_CHANGED = BASE + 203;

	public static final int SUBCLASS_ADDED = BASE + 301;
	public static final int SUBCLASS_REMOVED = BASE + 302;

	public static final int ENTITY_RENAMED = BASE + 401;

	public static final int SUBPROPERTY_ADDED = BASE + 501;
	public static final int SUBPROPERTY_REMOVED = BASE + 502;

	public static final int INDIVIDUAL_ADDED_OR_REMOVED = BASE + 601; //from class
    
    public static final int ENTITY_BROWSER_TEXT_CHANGED = BASE + 701;

}
