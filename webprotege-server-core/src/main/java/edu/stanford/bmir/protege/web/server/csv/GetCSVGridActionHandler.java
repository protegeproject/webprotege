package edu.stanford.bmir.protege.web.server.csv;

import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.inject.UploadsDirectory;
import edu.stanford.bmir.protege.web.shared.csv.CSVGrid;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.csv.GetCSVGridAction;
import edu.stanford.bmir.protege.web.shared.csv.GetCSVGridResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2013
 */
public class GetCSVGridActionHandler implements ApplicationActionHandler<GetCSVGridAction, GetCSVGridResult> {

    private final File uploadsDirectory;

    @Inject
    public GetCSVGridActionHandler(@UploadsDirectory File uploadsDirectory) {
        this.uploadsDirectory = checkNotNull(uploadsDirectory);
    }

    @Nonnull
    @Override
    public Class<GetCSVGridAction> getActionClass() {
        return GetCSVGridAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetCSVGridAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetCSVGridResult execute(@Nonnull GetCSVGridAction action, @Nonnull ExecutionContext executionContext) {
        DocumentId documentId = action.getCSVDocumentId();
        File file = new File(uploadsDirectory, documentId.getDocumentId());
        if(!file.exists()) {
            throw new RuntimeException("CSV file does not exist");
        }
        CSVGrid grid = getCSVGrid(file, action.getRowLimit());
        return new GetCSVGridResult(grid);

    }

    private CSVGrid getCSVGrid(File file, int rowLimit) {
        CSVGrid grid;
        try {
            Reader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            CSVGridParser gridParser = new CSVGridParser();
            grid = gridParser.readToLimit(fileReader, rowLimit);
            fileReader.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return grid;
    }
}
