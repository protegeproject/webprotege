package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 *
 */
public class ChangeData implements Serializable {

	private static final long serialVersionUID = -8172192983174354219L;
    private String author;
	private String description;
	private Date timestamp;
	private EntityData entityData;
	
	public ChangeData() {
	}
	
	public ChangeData(String author, String description, Date timestamp) {
		this.author = author;
		this.description = description;
		this.timestamp = timestamp;
	}

	public ChangeData(EntityData entityData, String author, String description, Date timestamp) {
		this(author, description, timestamp);
		this.entityData = entityData;
	}

	public String getAuthor() {
    	return author;
    }
	
	public void setAuthor(String author) {
    	this.author = author;
    }
	
	public String getDescription() {
    	return description;
    }
	
	public void setDescription(String description) {
    	this.description = description;
    }
	
	public Date getTimestamp() {
    	return timestamp;
    }
	
	public void setTimestamp(Date timestamp) {
    	this.timestamp = timestamp;
    }

	public EntityData getEntityData() {
    	return entityData;
    }

	public void setEntityData(EntityData entityData) {
    	this.entityData = entityData;
    }
}
