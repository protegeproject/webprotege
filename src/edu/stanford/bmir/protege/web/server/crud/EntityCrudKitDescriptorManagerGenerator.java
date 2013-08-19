package edu.stanford.bmir.protege.web.server.crud;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPlugin;

import java.io.PrintWriter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class EntityCrudKitDescriptorManagerGenerator extends Generator {

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
                TypeOracle typeOracle = context.getTypeOracle();
//        for(JClassType type : typeOracle.getTypes()) {
//            System.out.println(type.getName());
//        }

        JClassType cls = typeOracle.findType(typeName);

        final String generatedClassName = "EntityCrudKitManagerGenerated";
        final String generatedClassPackage = cls.getPackage().getName();

        ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(generatedClassPackage, generatedClassName);

        PrintWriter pw = context.tryCreate(logger, generatedClassPackage, generatedClassName);
        if (pw != null) {
            composerFactory.addImport(cls.getQualifiedSourceName());
            composerFactory.addImport("com.google.gwt.core.client.GWT");
            composerFactory.addImport("java.util.*");
            composerFactory.addImport("edu.stanford.bmir.protege.web.shared.crud.*");
            composerFactory.addImplementedInterface(cls.getQualifiedSourceName());
            SourceWriter sourceWriter = composerFactory.createSourceWriter(context, pw);

            sourceWriter.println("java.util.List<EntityCrudKit> descriptors = new java.util.ArrayList<EntityCrudKit>();");
            sourceWriter.println();
            sourceWriter.println("public %s() {", generatedClassName);
            sourceWriter.indent();

            for(JClassType type : typeOracle.getTypes()) {
                EntityCrudKitPlugin p = type.getAnnotation(EntityCrudKitPlugin.class);
                if (p != null) {
                    System.out.println("Found entity crud kit: " + p + "    -> " + type.getQualifiedSourceName());
                    sourceWriter.println("descriptors.add(%s.get());", type.getQualifiedSourceName());
//                    generatePortletFactory(logger, type, context);
                }
            }


            sourceWriter.outdent();
            sourceWriter.println("}");

            sourceWriter.println();
            sourceWriter.println();
            sourceWriter.println("public List<EntityCrudKit> getKits() {");
            sourceWriter.indent();
            sourceWriter.println("return descriptors;");
            sourceWriter.outdent();
            sourceWriter.println("}");

//            sourceWriter.println("public void exec() {");
//            sourceWriter.println(" GWT.log(\"" + new Date() +"\");");
//            sourceWriter.println("}");

            System.out.println("GENERATING " + composerFactory.getCreatedClassName());

            sourceWriter.commit(logger);

        }



        return composerFactory.getCreatedClassName();
    }


//    private void generatePortletFactory(TreeLogger logger, JClassType portletClass, GeneratorContext context) {
//        final String generatedClassName = portletClass.getQualifiedSourceName().replace(".", "_") + "_Factory";
//        final String generatedClassPackage = "edu.stanford.bmir.protege.web.client.ui.generated";
//
//        ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(generatedClassPackage, generatedClassName);
//
//        PrintWriter pw = context.tryCreate(logger, generatedClassPackage, generatedClassName);
//        if (pw != null) {
//            composerFactory.addImport(portletClass.getQualifiedSourceName());
//            composerFactory.addImport("com.google.gwt.core.client.GWT");
//            composerFactory.addImport(AbstractEntityPortlet.class.getName());
//            composerFactory.addImplementedInterface("edu.stanford.bmir.protege.web.client.ui.generated.PortletFactory");
//            SourceWriter sourceWriter = composerFactory.createSourceWriter(context, pw);
//            sourceWriter.indent();
//            sourceWriter.println("public " + portletClass.getSimpleSourceName() + " exec() {");
//            sourceWriter.indent();
//            sourceWriter.println(" return new " + portletClass.getSimpleSourceName() + "();");
//            sourceWriter.outdent();
//            sourceWriter.println("}");
//            sourceWriter.outdent();
//            System.out.println("GENERATING " + composerFactory.getCreatedClassName());
//            sourceWriter.commit(logger);
//
//        }
//
//    }

}
