package edu.stanford.bmir.protege.web.shared.notes;


import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.IsSerializable;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/03/2013
 */
public class NoteTarget implements IsSerializable {

    private OWLEntity entity;

    private NoteTarget() {

    }



    private NoteTarget(OWLEntity entity) {
        this.entity = entity;
    }


    public static NoteTarget get(OWLEntity entity) {
        return new NoteTarget(entity);
    }

    public static NoteTarget get(Optional<OWLEntity> entity) {
        if(entity.isPresent()) {
            return new NoteTarget(entity.get());
        }
        else {
            return new NoteTarget();
        }
    }

    public static NoteTarget get() {
        return new NoteTarget();
    }


//    public NoteTarget() {
//    }

    public Optional<OWLEntity> getEntity() {
        if(entity == null) {
            return Optional.absent();
        }
        else {
            return Optional.of(entity);
        }
    }

    //    private static final EmptyNoteTarget EMPTY_NOTE_TARGET = new EmptyNoteTarget();

    // Entity
    // NoteId
    // Axiom


    /**
     * For serialization only
     */
//    private NoteTarget() {
//    }
//
//    public Optional<OWLEntity> asOWLEntity() {
//        if(this instanceof OWLEntityNoteTarget) {
//            return Optional.of(((OWLEntityNoteTarget) this).target);
//        }
//        else {
//            return Optional.absent();
//        }
//    }
//
//    public NoteTarget get(OWLEntity entity) {
//        return new OWLEntityNoteTarget(entity);
//    }
//
//    public static NoteTarget emptyTarget() {
//        return EMPTY_NOTE_TARGET;
//    }
//
//
//    private static class EmptyNoteTarget extends NoteTarget {
//
//
//        private EmptyNoteTarget() {
//        }
//    }
//
//    private static class OWLEntityNoteTarget extends NoteTarget {
//
//        private OWLEntity target;
//
//        private OWLEntityNoteTarget() {
//
//        }
//
//        private OWLEntityNoteTarget(OWLEntity target) {
//            this.target = target;
//        }
//
//        @Override
//        public int hashCode() {
//            return "OWLEntityNoteTarget".hashCode() + target.hashCode();
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if(obj == this) {
//                return true;
//            }
//            if(!(obj instanceof OWLEntityNoteTarget)) {
//                return false;
//            }
//            OWLEntityNoteTarget other = (OWLEntityNoteTarget) obj;
//            return this.target.equals(other.target);
//        }
//    }


}
