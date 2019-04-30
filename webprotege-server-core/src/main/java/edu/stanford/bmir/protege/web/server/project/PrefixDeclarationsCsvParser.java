package edu.stanford.bmir.protege.web.server.project;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-04-30
 */
public class PrefixDeclarationsCsvParser {

    @Nonnull
    public ImmutableList<PrefixDeclaration> parse(@Nonnull InputStream inputStream) throws IOException {
        checkNotNull(inputStream);
        CsvMapper mapper = new CsvMapper();
        mapper.registerModule(new GuavaModule());
        CsvSchema schema = mapper.schemaFor(PrefixDeclaration.class);
        MappingIterator<PrefixDeclaration> iterator = mapper.readerFor(PrefixDeclaration.class)
                .with(schema)
                .readValues(inputStream);
        return ImmutableList.copyOf(iterator.readAll());
    }
}
