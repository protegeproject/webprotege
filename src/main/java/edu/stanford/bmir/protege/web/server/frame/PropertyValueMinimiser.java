package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

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

    public List<PropertyValue> minimisePropertyValues(List<PropertyValue> propertyValues) {
        List<PropertyValue> result = Lists.newArrayList(propertyValues);
        for (int i = 0; i < propertyValues.size(); i++) {
            for (int j = 0; j < propertyValues.size(); j++) {
                if (i != j && result.get(i) != null && result.get(j) != null) {
                    PropertyValue propertyValueA = propertyValues.get(i);
                    PropertyValue propertyValueB = propertyValues.get(j);
                    if (subsumptionChecker.isSubsumedBy(propertyValueA, propertyValueB)) {
                        // Don't show B because this is more specific!
                        result.set(j, null);
                    }
                }
            }
        }
        for (Iterator<PropertyValue> it = result.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                it.remove();
            }
        }
        return result;
    }
}
