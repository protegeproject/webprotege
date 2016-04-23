package edu.stanford.bmir.protege.web.server.codegen;

import com.google.common.collect.Sets;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/04/16
 */
public class WebProtegeCodeGeneratorVelocityImpl {

    private static final String PACKAGE_NAME = "edu.stanford.bmir.protege.web.client.portlet";

    private static final String TEMPLATE_PATH_PREFIX = "/edu/stanford/bmir/protege/web/server/codegen/";

    private static final String DESCRIPTORS = "descriptors";

    private static final String IMPORTED_PACKAGES = "importedPackages";

    public static final String BLENDER_PACKAGE = "edu.stanford.bmir.protege.web.client.codegen";

    private static final String CLASSPATH_RESOURCE_LOADER_CLASS = "classpath.resource.loader.class";

    public static interface SourceWriter {

        void writeSource(String packageName, String className, String source);
    }


    private PortletDescriptorSet descriptors;

    private Set<PortletModuleDescriptor> moduleDescriptors;

    private SourceWriter sourceWriter;

    public WebProtegeCodeGeneratorVelocityImpl(PortletDescriptorSet descriptors, Set<PortletModuleDescriptor> moduleDescriptors, SourceWriter sourceWriter) {
        this.descriptors = checkNotNull(descriptors);
        this.sourceWriter = checkNotNull(sourceWriter);
        this.moduleDescriptors = Sets.newHashSet(moduleDescriptors);
    }

    public void generate() throws Exception {
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty(CLASSPATH_RESOURCE_LOADER_CLASS, ClasspathResourceLoader.class.getName());
        engine.init();

        VelocityContext context = new VelocityContext();
        context.put(DESCRIPTORS, descriptors.getDescriptors());
        context.put(IMPORTED_PACKAGES, descriptors.getImportedPackages());
        context.put("moduleDescriptors", moduleDescriptors);
        context.put("rootPackage", PACKAGE_NAME);

        generateSource("BlenderGenerated", BLENDER_PACKAGE, engine, context);
        generateSource("PortletFactoryGenerated", PACKAGE_NAME, engine, context);
        generateSource("PortletFactoryModuleGenerated", PACKAGE_NAME, engine, context);
    }


    private void generateSource(String className, String packageName, VelocityEngine engine, VelocityContext context) {
        Template template = engine.getTemplate(TEMPLATE_PATH_PREFIX + className + ".java.vm");
        writeSource(packageName, className, context, template);
    }


    private void writeSource(String packageName, String className, VelocityContext context, Template template) {
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        String source = writer.toString();
        sourceWriter.writeSource(packageName, className, source);
    }
}

