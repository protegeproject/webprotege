package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/12/2012
 */
public class ClassFrameData implements Serializable {

    private ClassFrame classFrame;

    private Map<OWLEntity, OWLEntityData> entity2EntityData;

    private ClassFrameData() {

    }

    public ClassFrameData(ClassFrame classFrame, Map<OWLEntity, OWLEntityData> entity2EntityData) {
        this.classFrame = classFrame;
        this.entity2EntityData = entity2EntityData;
    }

    public ClassFrame getClassFrame() {
        return classFrame;
    }

    public OWLEntityData getEntityData(OWLEntity entity) {
        return entity2EntityData.get(entity);
    }
}
