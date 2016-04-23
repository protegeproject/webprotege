package edu.stanford.bmir.protege.web.server.codegen;

import com.google.common.collect.Sets;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import edu.stanford.bmir.protege.web.shared.portlet.Portlet;
import edu.stanford.bmir.protege.web.shared.portlet.PortletModule;
import edu.stanford.bmir.protege.web.shared.portlet.PortletPluginId;

import java.io.PrintWriter;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/04/16
 */

public class BlenderGenerator extends Generator {

    /**
     * Generate a default constructible subclass of the requested type. The
     * generator throws <code>UnableToCompleteException</code> if for any reason
     * it cannot provide a substitute class
     * @return the name of a subclass to substitute for the requested class, or
     * return <code>null</code> to cause the requested type itself to be
     * used
     */
    @Override
    public String generate(final TreeLogger logger, final GeneratorContext context, String typeName) throws UnableToCompleteException {
        WebProtegeCodeGeneratorVelocityImpl.SourceWriter sourceWriter = new WebProtegeCodeGeneratorVelocityImpl.SourceWriter() {
            public void writeSource(String packageName, String className, String source) {
                PrintWriter pw = context.tryCreate(logger, packageName, className);
                if(pw == null) {
                    return;
                }
                pw.print(source);
                pw.flush();
                context.commit(logger, pw);
            }
        };
        PortletDescriptorSet descriptors = findPortlets(context);
        Set<PortletModuleDescriptor> moduleDescriptors = findModuleDescriptors(context);
        WebProtegeCodeGeneratorVelocityImpl generator = new WebProtegeCodeGeneratorVelocityImpl(descriptors, moduleDescriptors, sourceWriter);
        try {
            generator.generate();
        } catch (Exception e) {
            System.err.println("Unable to complete due to ERROR: " + e.getMessage());
            e.printStackTrace();
            throw new UnableToCompleteException();
        }
        return typeName + "Generated";
    }



    private PortletDescriptorSet findPortlets(GeneratorContext generatorContext) {
        PortletDescriptorSet.Builder builder = PortletDescriptorSet.builder();
        TypeOracle typeOracle = generatorContext.getTypeOracle();
        JClassType[] types = typeOracle.getTypes();
        for (JClassType type : types) {
            Portlet portlet = type.getAnnotation(Portlet.class);
            if (portlet != null) {
                PortletDescriptor desc = getPortletDescriptor(type);
                builder.add(desc);
            }
        }
        return builder.build();
    }



    private Set<PortletModuleDescriptor> findModuleDescriptors(GeneratorContext context) {
        Set<PortletModuleDescriptor> descriptors = Sets.newLinkedHashSet();
        for(JClassType type : context.getTypeOracle().getTypes()) {
            PortletModule portletModule = type.getAnnotation(PortletModule.class);
            if(portletModule != null) {
                descriptors.add(new PortletModuleDescriptor(type.getQualifiedSourceName()));
            }
        }
        return descriptors;
    }

    private static PortletDescriptor getPortletDescriptor(JClassType portletClass) {

        return new PortletDescriptor(
                new PortletPluginId(portletClass.getAnnotation(Portlet.class).id()),
                portletClass.getPackage().getName(),
                portletClass.getSimpleSourceName());
    }



}