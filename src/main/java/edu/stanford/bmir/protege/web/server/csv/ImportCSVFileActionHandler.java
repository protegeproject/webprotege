package edu.stanford.bmir.protege.web.server.csv;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.inject.UploadsDirectory;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.csv.CSVGrid;
import edu.stanford.bmir.protege.web.shared.csv.ImportCSVFileAction;
import edu.stanford.bmir.protege.web.shared.csv.ImportCSVFileResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.*;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static java.util.Collections.singletonList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2013
 */
public class ImportCSVFileActionHandler extends AbstractProjectChangeHandler<Integer,ImportCSVFileAction, ImportCSVFileResult> {

    @Nonnull
    private final File uploadsDirectory;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public ImportCSVFileActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                      @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull @UploadsDirectory File uploadsDirectory,
                                      @Nonnull @RootOntology OWLOntology rootOntology,
                                      @Nonnull OWLDataFactory dataFactory) {
        super(accessManager, eventManager, applyChanges);
        this.uploadsDirectory = uploadsDirectory;
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
    }

    @Override
    public Class<ImportCSVFileAction> getActionClass() {
        return ImportCSVFileAction.class;
    }

    @Override
    protected ChangeListGenerator<Integer> getChangeListGenerator(ImportCSVFileAction action,
                                                                  ExecutionContext executionContext) {
        CSVGrid csvGrid = parseCSVGrid(action);
        return new ImportCSVFileChangeListGenerator(action.getImportRootClass(),
                                                    csvGrid, action.getDescriptor(),
                                                    rootOntology,
                                                    dataFactory);
    }

    private CSVGrid parseCSVGrid(ImportCSVFileAction action) {
        try {
            CSVGridParser parser = new CSVGridParser();
            final File file = new File(uploadsDirectory, action.getDocumentId().getDocumentId());
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            CSVGrid grid = parser.readAll(reader);
            reader.close();
            return grid;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected ChangeDescriptionGenerator<Integer> getChangeDescription(ImportCSVFileAction action,
                                                                       ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<>("Imported CSV File");
    }

    @Override
    protected ImportCSVFileResult createActionResult(ChangeApplicationResult<Integer> changeApplicationResult,
                                                     ImportCSVFileAction action,
                                                     ExecutionContext executionContext,
                                                     EventList<ProjectEvent<?>> eventList) {
        return new ImportCSVFileResult(new EventList<ProjectEvent<?>>(EventTag.get(0), EventTag.get(1)), changeApplicationResult.getSubject().get());
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return singletonList(EDIT_ONTOLOGY);
    }
}
