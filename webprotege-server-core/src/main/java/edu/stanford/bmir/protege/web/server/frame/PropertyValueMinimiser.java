package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class PropertyValueMinimiser {


    @Nonnull
    private final PropertyValueSubsumptionChecker subsumptionChecker;

    @Inject
    public PropertyValueMinimiser(@Nonnull PropertyValueSubsumptionChecker subsumptionChecker) {
        this.subsumptionChecker = checkNotNull(subsumptionChecker);
    }

    public Stream<PlainPropertyValue> minimisePropertyValues(List<PlainPropertyValue> propertyValues) {
        var result = Lists.newArrayList(propertyValues);
        for (int i = 0; i < propertyValues.size(); i++) {
            for (int j = 0; j < propertyValues.size(); j++) {
                if (i != j && result.get(i) != null && result.get(j) != null) {
                    var propertyValueA = propertyValues.get(i);
                    var propertyValueB = propertyValues.get(j);
                    if (subsumptionChecker.isSubsumedBy(propertyValueA, propertyValueB)) {
                        // Don't show B because this is more specific!
                        result.set(j, null);
                    }
                }
            }
        }
        result.removeIf(Objects::isNull);
        return result.stream();
    }
}
