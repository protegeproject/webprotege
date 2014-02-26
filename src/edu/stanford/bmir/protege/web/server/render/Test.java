package edu.stanford.bmir.protege.web.server.render;

import com.googlecode.gwt.crypto.util.Sys;
import edu.stanford.bmir.protege.web.server.hierarchy.HasGetAncestors;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class Test {

    public static void main(String[] args) throws Exception {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        manager.setSilentMissingImportsHandling(true);
        manager.addIRIMapper(new AutoIRIMapper(new File("/Users/matthewhorridge/Downloads/fibo"), false));
        final OWLOntology ont = manager.loadOntologyFromOntologyDocument(
                new FileDocumentSource(new File("/Users/matthewhorridge/Downloads/fibo/IRSwaps.owl")),
                new OWLOntologyLoaderConfiguration().setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
//        final OWLOntology ont = manager.loadOntologyFromOntologyDocument(IRI.create("file:/tmp/bmp.owl"));
//        final OWLOntology ont = manager.loadOntologyFromOntologyDocument(IRI.create("file:/tmp/bfo.owl"));
//        final OWLOntology ont = manager.loadOntologyFromOntologyDocument(IRI.create("file:/tmp/people.owl"));
//        final OWLOntology ont = manager.loadOntologyFromOntologyDocument(IRI.create("file:/tmp/pizza.owl"));
//        final OWLOntology ont = manager.loadOntologyFromOntologyDocument(IRI.create("file:/Users/matthewhorridge/Downloads/ext.owl"));
        StringBuilder stringBuilder = new StringBuilder();
        ShortFormProvider sfp = new ShortFormProvider() {
            @Override
            public String getShortForm(OWLEntity entity) {
                Set<OWLAnnotation> labels = entity.getAnnotations(ont,
                        ont.getOWLOntologyManager().getOWLDataFactory().getRDFSLabel());
                if (labels.isEmpty()) {
                    return entity.getIRI().getFragment();
                }
                return ((OWLLiteral) labels.iterator().next().getValue()).getLiteral();
//                return entity.getIRI().getFragment();
            }

            @Override
            public void dispose() {
            }
        };
        ManchesterSyntaxEntityFrameRenderer renderer = new ManchesterSyntaxEntityFrameRenderer(ont,
                sfp,
                new OntologyIRIShortFormProvider(),
                new ManchesterSyntaxObjectRenderer(sfp,
                        new DefaultEntityIRIChecker(ont),
                        LiteralStyle.REGULAR,
                        new DefaultHttpLinkRenderer(),
                        new MarkdownLiteralRenderer()),
                new ManchesterSyntaxObjectRenderer.HighlightChecker() {
                    @Override
                    public boolean isHighlighted(OWLEntity entity) {
                        return false;
                    }
                },
                new ManchesterSyntaxObjectRenderer.DeprecatedChecker() {
                    @Override
                    public boolean isDeprecated(OWLEntity entity) {
                        return false;
                    }
                },
                new DefaultItemStyleProvider(),
                NestedAnnotationStyle.COMPACT
        );
        long t0 = System.currentTimeMillis();
        renderer.render(ont, stringBuilder);
        stringBuilder.append("<br>");
        stringBuilder.append("<br>");
        stringBuilder.append("<br>");
        int count = 0;
        for (OWLClass cls : ont.getClassesInSignature(true)) {
            renderer.render(cls, stringBuilder);
            count++;
            if (count > 1000) {
                break;
            }
        }
        for (OWLObjectProperty prop : ont.getObjectPropertiesInSignature(true)) {
            renderer.render(prop, stringBuilder);
        }
        for (OWLNamedIndividual ind : ont.getIndividualsInSignature(true)) {
            renderer.render(ind, stringBuilder);
        }

        long t1 = System.currentTimeMillis();
        System.out.println("Rendered in " + (t1 - t0));
        FileWriter fw = new FileWriter("/Users/matthewhorridge/Desktop/out.html");
        fw.write("<html>");
        fw.write("<head>");
        fw.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"ms-style.css\">");
        fw.write("<script src=\"js/codemirror/lib/codemirror.js\"></script>\n" +
                "<link rel=\"stylesheet\" href=\"js/codemirror/lib/codemirror.css\">\n" +
                "<script src=\"js/codemirror/mode/manchestersyntax/manchestersyntax.js\"></script>");
        fw.write("</head>");
        fw.write("<body>");
        fw.write(stringBuilder.toString());
        fw.write("</body>");
        fw.write("</html>");
        fw.close();
    }
}
