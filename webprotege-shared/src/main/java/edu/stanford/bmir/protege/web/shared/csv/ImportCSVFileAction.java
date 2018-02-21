package edu.stanford.bmir.protege.web.shared.csv;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2013
 */
public class ImportCSVFileAction extends AbstractHasProjectAction<ImportCSVFileResult> {

    private OWLClass importRootClass;

    private DocumentId documentId;

    private CSVImportDescriptor descriptor;

    /**
     * For serialization only
     */
    private ImportCSVFileAction() {
    }

    public ImportCSVFileAction(ProjectId projectId, DocumentId csvDocumentId, OWLClass importRootClass, CSVImportDescriptor descriptor) {
        super(projectId);
        this.importRootClass = checkNotNull(importRootClass);
        this.documentId = checkNotNull(csvDocumentId);
        this.descriptor = checkNotNull(descriptor);
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public CSVImportDescriptor getDescriptor() {
        return descriptor;
    }

    public OWLClass getImportRootClass() {
        return importRootClass;
    }
}
