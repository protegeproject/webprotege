package edu.stanford.bmir.protege.web.server.codegen;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/04/16
 */
public class PortletDescriptorSet {

    private final ImmutableSet<PortletDescriptor> descriptors;

    private final ImmutableSet<String> importedPackages;

    private PortletDescriptorSet(Collection<PortletDescriptor> descriptors) {
        ImmutableSet.Builder<PortletDescriptor> descriptorBuilder = ImmutableSet.builder();
        ImmutableSet.Builder<String> packagesBuilder = ImmutableSet.builder();
        for(PortletDescriptor descriptor : descriptors) {
            descriptorBuilder.add(descriptor);
            packagesBuilder.add(descriptor.getPackageName());
        }
        this.descriptors = descriptorBuilder.build();
        importedPackages = packagesBuilder.build();
    }

    public ImmutableSet<PortletDescriptor> getDescriptors() {
        return descriptors;
    }

    public ImmutableSet<String> getImportedPackages() {
        return importedPackages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Set<PortletDescriptor> portletDescriptors = Sets.newLinkedHashSet();

        public Builder add(PortletDescriptor descriptor) {
            portletDescriptors.add(descriptor);
            return this;
        }

        public PortletDescriptorSet build() {
            return new PortletDescriptorSet(portletDescriptors);
        }
    }
}
