package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Entity;
import edu.stanford.bmir.protege.web.client.ui.propertygrid.PropertyGrid;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLClass;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

public interface PropertyGridServiceAsync {

    void getPropertyGrid(ProjectId projectId, Entity entity, AsyncCallback<PropertyGrid> async);
}
