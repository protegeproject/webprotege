package edu.stanford.bmir.protege.web.server.crud;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

        JClassType cls = typeOracle.findType(typeName);
//
        final String generatedClassName = "EntityCrudKitManagerGenerated";
        final String generatedClassPackage = cls.getPackage().getName();
//
        ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(generatedClassPackage, generatedClassName);
//
//        PrintWriter pw = context.tryCreate(logger, generatedClassPackage, generatedClassName);
//        if (pw != null) {
//            composerFactory.addImport(cls.getQualifiedSourceName());
//            composerFactory.addImport("com.google.gwt.core.client.GWT");
//            composerFactory.addImport("java.util.*");
//            composerFactory.addImport("edu.stanford.bmir.protege.web.shared.crud.*");
//            composerFactory.addImplementedInterface(cls.getQualifiedSourceName());
//            SourceWriter sourceWriter = composerFactory.createSourceWriter(context, pw);
//
//            sourceWriter.println("java.util.List<EntityCrudKit> descriptors = new java.util.ArrayList<EntityCrudKit>();");
//            sourceWriter.println();
//            sourceWriter.println("public %s() {", generatedClassName);
//            sourceWriter.indent();
//
//            for(JClassType type : typeOracle.getTypes()) {
//                EntityCrudKitPlugin p = type.getAnnotation(EntityCrudKitPlugin.class);
//                if (p != null) {
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println("Found entity crud kit: " + p + "    -> " + type.getQualifiedSourceName());
//                    String serverPackageName = type.getPackage().getName().replace("shared", "server");
//                    System.out.println("Server side package: " + serverPackageName);
//                    String clientPackageName = type.getPackage().getName().replace("shared", "client");
//                    System.out.println("Client side package: " + clientPackageName);
//
//                    String namePrefix = type.getName();
//                    System.out.println("Handler class: " + serverPackageName + "." + namePrefix + "Handler");
//                    System.out.println("Editor class: " + clientPackageName + "." + namePrefix + "Editor");
//
//                    sourceWriter.println("descriptors.add(%s.get());", type.getQualifiedSourceName());
//
//                    try {
//                        Class<? extends EntityCrudKitHandler<?>> handlerClass = p.handlerClass();
//                        Type[] genIn = handlerClass.getGenericInterfaces();
//                        for(Type t : genIn) {
//                            ParameterizedType parameterizedType = (ParameterizedType) t;
//                            for(Type pt : parameterizedType.getActualTypeArguments()) {
//                                System.out.println(pt.getClass().getName());
//                            }
//                        }
//                        EntityCrudKitHandler<?> handler = handlerClass.newInstance();
//
//                        System.out.println(handler);
//                    } catch (InstantiationException e) {
//                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    }
//
////                    generatePortletFactory(logger, type, context);
//                }
//            }
//
//
//            sourceWriter.outdent();
//            sourceWriter.println("}");
//
//            sourceWriter.println();
//            sourceWriter.println();
//            sourceWriter.println("public List<EntityCrudKit> getKits() {");
//            sourceWriter.indent();
//            sourceWriter.println("return descriptors;");
//            sourceWriter.outdent();
//            sourceWriter.println("}");
//
////            sourceWriter.println("public void exec() {");
////            sourceWriter.println(" GWT.log(\"" + new Date() +"\");");
////            sourceWriter.println("}");
//
//            System.out.println("GENERATING " + composerFactory.getCreatedClassName());
//
//            sourceWriter.commit(logger);
//
//        }



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
