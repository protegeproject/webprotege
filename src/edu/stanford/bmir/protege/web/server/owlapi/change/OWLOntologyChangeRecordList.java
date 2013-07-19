package edu.stanford.bmir.protege.web.server.owlapi.change;

import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/07/2013
 */
public class OWLOntologyChangeRecordList implements Iterable<OWLOntologyChangeRecord> {

    private OWLOntologyID ontologyID = null;

    private List<OWLAxiom> axioms;

    private List<OWLOntologyChangeRecord> changeRecordList = null;

    private int size;

    public OWLOntologyChangeRecordList() {
        changeRecordList = Collections.emptyList();
    }

    public OWLOntologyChangeRecordList(List<OWLOntologyChangeRecord> list) {
        this.size = list.size();
        for(OWLOntologyChangeRecord record : list) {
            OWLOntologyID ontologyID = record.getOntologyID();
            if(this.ontologyID == null) {
                this.ontologyID = ontologyID;
            }
            else if(!this.ontologyID.equals(ontologyID)) {
                // Not all same
                changeRecordList = list;
                break;
            }
            OWLOntologyChangeData changeData = record.getData();
            if(changeData instanceof AddAxiomData) {
                OWLAxiom axiom = ((AddAxiomData) changeData).getAxiom();
                if(axioms == null) {
                    axioms = new ArrayList<OWLAxiom>(list.size());
                }
                axioms.add(axiom);
            }
            else {
                changeRecordList = list;
                axioms = null;
                break;
            }
        }
    }


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size != 0;
    }

    public Iterator<OWLOntologyChangeRecord> iterator() {
        if (axioms != null) {
            return new Iterator<OWLOntologyChangeRecord>() {

                private Iterator<OWLAxiom> delegate = axioms.iterator();

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public OWLOntologyChangeRecord next() {
                    OWLAxiom axiom = delegate.next();
                    return new OWLOntologyChangeRecord(ontologyID, new AddAxiomData(axiom));
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
        else {
            return changeRecordList.iterator();
        }
    }

}
