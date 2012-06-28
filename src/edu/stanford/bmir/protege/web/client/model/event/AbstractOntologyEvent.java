package edu.stanford.bmir.protege.web.client.model.event;

import java.io.Serializable;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

public class AbstractOntologyEvent implements Serializable, OntologyEvent {

    private static final long serialVersionUID = -4234136925402884154L;

    private int type;
	private String user;
	private EntityData source;

	private int revision;

	public AbstractOntologyEvent() {}

	public AbstractOntologyEvent(EntityData source, int type, String user, int revision) {
		this.source = source;
		this.type = type;
		this.user = user;
		this.revision = revision;
	}

	public String getUser() {
		return user;
	}

	public int getType() {
		return type;
	}

	public EntityData getEntity() {
		return source;
	}

	public int getRevision() {
        return revision;
    }

	@Override
	public String toString() {
	    return "OntologyEvent(" + type + ", " + source + ", " + user + ")";
	}

}
