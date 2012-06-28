package edu.stanford.bmir.protege.web.client.model.event;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;


public interface OntologyEvent extends Event {

    public String getUser();

    public int getType();

    public EntityData getEntity();

    public int getRevision();

}
