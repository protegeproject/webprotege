package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class ReviewData implements Serializable {
	private static final long serialVersionUID = -4466243548934386784L;
    
    private Date created;
    private String author;
    private String body;
    private String subject;
    
    public ReviewData() {    	
    }
    
    public ReviewData(String author, String subject, String body, Date created) {
	    this.author = author;
	    this.subject = subject;
	    this.body = body;
	    this.created = created;
    }

	public String getAuthor() {
    	return author;
    }

	public void setAuthor(String author) {
    	this.author = author;
    }

	public String getBody() {
    	return body;
    }

	public void setBody(String body) {
    	this.body = body;
    }

	public Date getCreated() {
    	return created;
    }

	public void setCreated(Date created) {
    	this.created = created;
    }

	public String getSubject() {
    	return subject;
    }

	public void setSubject(String subject) {
    	this.subject = subject;
    }
}
