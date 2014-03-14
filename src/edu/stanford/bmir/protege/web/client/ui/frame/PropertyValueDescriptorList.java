package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueDescriptor;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueState;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 02/03/2014
 */
public class PropertyValueDescriptorList {

    private List<PropertyValueDescriptor> propertyValueDescriptorList;

    public PropertyValueDescriptorList(List<PropertyValueDescriptor> propertyValueDescriptorList) {
        this.propertyValueDescriptorList = Lists.newArrayList(propertyValueDescriptorList);
    }

    public List<PropertyValueDescriptor> getPropertyValueDescriptorList() {
        return Lists.newArrayList(propertyValueDescriptorList);
    }

    public Set<OWLAxiom> getCorrespondingAxioms() {
        Set<OWLAxiom> result = Sets.newHashSet();
        for(PropertyValueDescriptor descriptor : propertyValueDescriptorList) {
            if(descriptor.getState() == PropertyValueState.ASSERTED) {
                result.addAll(descriptor.getAdditionalAxioms());
            }
        }
        return result;
    }
}
