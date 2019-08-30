package edu.stanford.bmir.protege.web.server.revision;

import edu.stanford.bmir.protege.web.server.inject.ChangeHistoryFileFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.binaryowl.BinaryOWLChangeLogHandler;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.binaryowl.chunk.SkipSetting;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.binaryowl.chunk.SkipSetting.SKIP_DATA;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2017
 */
public class HeadRevisionNumberFinder {

    @Nonnull
    private final ChangeHistoryFileFactory changeHistoryFileFactory;

    @Inject
    public HeadRevisionNumberFinder(@Nonnull ChangeHistoryFileFactory changeHistoryFileFactory) {
        this.changeHistoryFileFactory = checkNotNull(changeHistoryFileFactory);
    }

    /**
     * Computes the head revision number for the specified project.
     *
     * @param projectId The project.
     * @return The {@link RevisionNumber}
     */
    @Nonnull
    public RevisionNumber getHeadRevisionNumber(@Nonnull ProjectId projectId) throws IOException {
        /*
            This method works fairly well, even for large
            projects, but it is only intended to be a stopgap and needs replacing.
         */
        File changeHistoryFile = changeHistoryFileFactory.getChangeHistoryFile(projectId);
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(changeHistoryFile.toPath()))) {
            BinaryOWLOntologyChangeLog log = new BinaryOWLOntologyChangeLog();
            RevisionExtractor extractor = new RevisionExtractor();
            log.readChanges(bufferedInputStream, new OWLDataFactoryImpl(), extractor, SKIP_DATA);
            return RevisionNumber.getRevisionNumber(extractor.getLastRevision());
        }
    }


    private static class RevisionExtractor implements BinaryOWLChangeLogHandler {

        private int counter = 0;

        @Override
        public void handleChangesRead(OntologyChangeRecordList list, SkipSetting skipSetting, long filePosition) {
            counter++;
        }

        public int getLastRevision() {
            return counter;
        }
    }
}
