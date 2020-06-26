package edu.stanford.bmir.protege.web.client.bulkop;

import edu.stanford.bmir.protege.web.shared.HasBrowserText;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2018
 */
public class BulkOpMessageFormatter {

    private static final int LIMIT_SIZE = 10;

    public static String format(@Nonnull Collection<? extends OWLEntityData> entities) {
        if(entities.size() > LIMIT_SIZE) {
            return Integer.toString(entities.size());
        }
        else {
            return entities.stream()
                    .map(HasBrowserText::getBrowserText)
                    .collect(Collectors.joining(", "));
        }
    }

    public static String sortAndFormat(@Nonnull Collection<? extends OWLEntityData> entities) {
        if(entities.size() > LIMIT_SIZE) {
            return Integer.toString(entities.size()) + " entities";
        }
        else {
            return entities.stream()
                    .map(OWLEntityData::getBrowserText)
                    .sorted(Comparator.comparing(String::toLowerCase))
                    .collect(Collectors.joining(", "));
        }
    }

}
