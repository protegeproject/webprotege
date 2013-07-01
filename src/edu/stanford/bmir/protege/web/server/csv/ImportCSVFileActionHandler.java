package edu.stanford.bmir.protege.web.server.csv;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.filesubmission.FileUploadConstants;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.csv.CSVGrid;
import edu.stanford.bmir.protege.web.shared.csv.ImportCSVFileAction;
import edu.stanford.bmir.protege.web.shared.csv.ImportCSVFileResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2013
 */
public class ImportCSVFileActionHandler extends AbstractProjectChangeHandler<Integer,ImportCSVFileAction, ImportCSVFileResult> {

    @Override
    public Class<ImportCSVFileAction> getActionClass() {
        return ImportCSVFileAction.class;
    }

    @Override
    protected ChangeListGenerator<Integer> getChangeListGenerator(ImportCSVFileAction action, OWLAPIProject project, ExecutionContext executionContext) {
        CSVGrid csvGrid = parseCSVGrid(action);
        return new ImportCSVFileChangeListGenerator(action.getImportRootClass(), csvGrid, action.getDescriptor());
    }

    private CSVGrid parseCSVGrid(ImportCSVFileAction action) {
        try {
            CSVGridParser parser = new CSVGridParser();
            final File file = new File(FileUploadConstants.UPLOADS_DIRECTORY, action.getDocumentId().getDocumentId());
            final FileReader reader = new FileReader(file);
            CSVGrid grid = parser.readAll(reader);
            reader.close();
            return grid;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected ChangeDescriptionGenerator<Integer> getChangeDescription(ImportCSVFileAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<Integer>("Imported CSV File");
    }

    @Override
    protected ImportCSVFileResult createActionResult(ChangeApplicationResult<Integer> changeApplicationResult, ImportCSVFileAction action, OWLAPIProject project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        int eventListSize = eventList.getEvents().size();

        return new ImportCSVFileResult(new EventList<ProjectEvent<?>>(EventTag.get(0), EventTag.get(1)), changeApplicationResult.getSubject().get());
    }

    @Override
    protected RequestValidator<ImportCSVFileAction> getAdditionalRequestValidator(ImportCSVFileAction action, RequestContext requestContext) {
        return new UserHasProjectWritePermissionValidator<ImportCSVFileAction, ImportCSVFileResult>();
    }
}
