package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public interface PropertyValueSubsumptionChecker {

    boolean isSubsumedBy(PropertyValue propertyValueA, PropertyValue propertyValueB);
}
