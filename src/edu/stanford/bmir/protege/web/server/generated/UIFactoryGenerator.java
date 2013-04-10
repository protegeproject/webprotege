package edu.stanford.bmir.protege.web.server.generated;

public class UIFactoryGenerator {

}


//import com.google.gwt.core.ext.Generator;
//import com.google.gwt.core.ext.GeneratorContext;
//import com.google.gwt.core.ext.TreeLogger;
//import com.google.gwt.core.ext.UnableToCompleteException;
//import com.google.gwt.core.ext.typeinfo.JClassType;
//import com.google.gwt.core.ext.typeinfo.TypeOracle;
//import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
//import com.google.gwt.user.rebind.SourceWriter;
//import edu.stanford.bmir.protege.web.client.annotation.WebProtegePortlet;
//import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
//
//import java.io.PrintWriter;
//import java.util.Date;
//
///**
// * Author: Matthew Horridge<br>
// * Stanford University<br>
// * Bio-Medical Informatics Research Group<br>
// * Date: 31/01/2013
// */
//public class UIFactoryGenerator extends Generator {
//
//    /**
//     * Generate a default constructible subclass of the requested type. The
//     * generator throws <code>UnableToCompleteException</code> if for any reason
//     * it cannot provide a substitute class
//     * @return the name of a subclass to substitute for the requested class, or
//     *         return <code>null</code> to cause the requested type itself to be
//     *         used
//     */
//    @Override
//    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
//        TypeOracle typeOracle = context.getTypeOracle();
////        for(JClassType type : typeOracle.getTypes()) {
////            System.out.println(type.getName());
////        }
//        for(JClassType type : typeOracle.getTypes()) {
//            WebProtegePortlet p = type.getAnnotation(WebProtegePortlet.class);
//            if (p != null) {
//                System.out.println("Found portlet: " + p.displayName() + "    -> " + type.getQualifiedSourceName());
//                generatePortletFactory(logger, type, context);
//            }
//        }
//        JClassType cls = typeOracle.findType(typeName);
//
//
//
//        final String generatedClassName = "UIFactoryGenerated";
//        final String generatedClassPackage = cls.getPackage().getName();
//
//        ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(generatedClassPackage, generatedClassName);
//
//        PrintWriter pw = context.tryCreate(logger, generatedClassPackage, generatedClassName);
//        if (pw != null) {
//            composerFactory.addImport(cls.getQualifiedSourceName());
//            composerFactory.addImport("com.google.gwt.core.client.GWT");
//            composerFactory.addImplementedInterface(cls.getQualifiedSourceName());
//            SourceWriter sourceWriter = composerFactory.createSourceWriter(context, pw);
//            sourceWriter.println("public void exec() {");
//            sourceWriter.println(" GWT.log(\"" + new Date() +"\");");
//            sourceWriter.println("}");
//            System.out.println("GENERATING " + composerFactory.getCreatedClassName());
//            sourceWriter.commit(logger);
//
//        }
//
//
//
//        return composerFactory.getCreatedClassName();
//    }
//
//
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
//}
