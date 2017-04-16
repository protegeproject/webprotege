package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.inject.project.ChangeHistoryFileProvider;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectDirectoryFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.binaryowl.BinaryOWLChangeLogHandler;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.binaryowl.chunk.SkipSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.semanticweb.binaryowl.chunk.SkipSetting.SKIP_DATA;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2017
 */
public class HeadRevisionNumberFinder {

    @Nonnull
    private final ProjectDirectoryFactory projectDirectoryFactory;

    @Inject
    public HeadRevisionNumberFinder(@Nonnull ProjectDirectoryFactory projectDirectoryFactory) {
        this.projectDirectoryFactory = checkNotNull(projectDirectoryFactory);
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
        File projectDir = projectDirectoryFactory.getProjectDirectory(projectId);
        ChangeHistoryFileProvider changeHistoryFileProvider = new ChangeHistoryFileProvider(projectDir);
        File changeHistoryFile = changeHistoryFileProvider.get();
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
