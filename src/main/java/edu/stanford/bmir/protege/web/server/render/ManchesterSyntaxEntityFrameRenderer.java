package edu.stanford.bmir.protege.web.server.render;

import com.google.common.base.Optional;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 20/02/2014
 */
public class ManchesterSyntaxEntityFrameRenderer {

    private OWLOntology rootOntology;

    private ShortFormProvider shortFormProvider;

    private OntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    private ManchesterSyntaxObjectRenderer objectRenderer;

    private HighlightedEntityChecker highlightChecker;

    private DeprecatedEntityChecker deprecatedChecker;

    private ItemStyleProvider itemStyleProvider;

    private NestedAnnotationStyle nestedAnnotationStyle;

    private final ElementTagRenderer frameHeaderTagRenderer = new ElementRenderer("div", "frame-header");

    private final ElementTagRenderer frameTypeTagRenderer = new ElementRenderer("span", "ms-frame-kw");

    private final ElementTagRenderer frameSubjectTagRenderer = new ElementRenderer("span", "ms-frame-subject");

    private final ElementTagRenderer frameBlockTagRenderer = new ElementRenderer("div", "ms-frame-block");


    private final ElementTagRenderer sectionTagRenderer = new ElementRenderer("div", "ms-section");

    private final ElementTagRenderer sectionKwTagRenderer = new ElementRenderer("span", "ms-section-kw");

    private final ElementTagRenderer inOntologyTagRenderer = new ElementRenderer("span", "ms-ont-id");

    private final ElementTagRenderer sectionBlockTagRenderer = new ElementRenderer("div", "ms-section-block");


    private final ElementTagRenderer itemSeparatorTagRenderer = new ElementRenderer("span", "ms-sep");

    private final ElementTagRenderer annotationsTagRenderer = new ElementRenderer("div", "ms-annotations");

    private final ElementTagRenderer annotationsBlockTagRenderer = new ElementRenderer("div", "ms-annotations-block");

    public ManchesterSyntaxEntityFrameRenderer(OWLOntology rootOntology,
                                               ShortFormProvider shortFormProvider,
                                               OntologyIRIShortFormProvider ontologyIRIShortFormProvider,
                                               ManchesterSyntaxObjectRenderer objectRenderer,
                                               HighlightedEntityChecker highlightChecker,
                                               DeprecatedEntityChecker deprecatedChecker,
                                               ItemStyleProvider itemStyleProvider,
                                               NestedAnnotationStyle nestedAnnotationStyle) {
        this.rootOntology = rootOntology;
        this.ontologyIRIShortFormProvider = ontologyIRIShortFormProvider;
        this.shortFormProvider = shortFormProvider;
        this.objectRenderer = objectRenderer;
        this.highlightChecker = highlightChecker;
        this.deprecatedChecker = deprecatedChecker;
        this.itemStyleProvider = itemStyleProvider;
        this.nestedAnnotationStyle = nestedAnnotationStyle;
    }

    public void render(OWLOntology ontology, StringBuilder builder) {
        renderSection(new OntologyAnnotationsSectionRenderer(), ontology, builder);
    }

    public void render(OWLEntity entity, StringBuilder builder) {
        builder.append("<div class=\"ms-frame ");
        builder.append(getFrameClass(entity));
        builder.append("\">");
        writeFrameHeader(entity, builder);
        writeFrameContent(entity, builder);
        builder.append("</div>");
    }

    private String getFrameClass(OWLEntity entity) {
        return entity.accept(new OWLEntityVisitorEx<String>() {
            @Override
            public String visit(OWLClass cls) {
                return "ms-class-frame";
            }

            @Override
            public String visit(OWLObjectProperty property) {
                return "ms-objectproperty-frame";
            }

            @Override
            public String visit(OWLDataProperty property) {
                return "ms-dataproperty-frame";
            }

            @Override
            public String visit(OWLNamedIndividual individual) {
                return "ms-individual-frame";
            }

            @Override
            public String visit(OWLDatatype datatype) {
                return "ms-datatype-frame";
            }

            @Override
            public String visit(OWLAnnotationProperty property) {
                return "ms-annotationproperty-frame";
            }
        });
    }

    private void writeFrameHeader(OWLEntity entity, StringBuilder builder) {
        frameHeaderTagRenderer.renderOpeningTag(builder);
        frameTypeTagRenderer.renderOpeningTag(builder);
        String frameName = entity.getEntityType().getName();
        builder.append(frameName);
        builder.append(": ");
        frameTypeTagRenderer.renderClosingTag(builder);
        frameSubjectTagRenderer.renderOpeningTag(builder);
        renderEscaped(shortFormProvider.getShortForm(entity), builder);
        frameSubjectTagRenderer.renderClosingTag(builder);
        frameHeaderTagRenderer.renderClosingTag(builder);
    }

    private void writeFrameContent(final OWLEntity entity, final StringBuilder builder) {
        frameBlockTagRenderer.renderOpeningTag(builder);
        entity.accept(new OWLEntityVisitor() {
            @Override
            public void visit(OWLClass cls) {
                writeClassFrameContent(cls, builder);
            }

            @Override
            public void visit(OWLObjectProperty property) {
                writeObjectPropertyFrameContent(property, builder);
            }

            @Override
            public void visit(OWLDataProperty property) {
                writeDataPropertyFrameContent(property, builder);
            }

            @Override
            public void visit(OWLNamedIndividual individual) {
                writeNamedIndividualFrameContent(individual, builder);
            }

            @Override
            public void visit(OWLDatatype datatype) {
            }

            @Override
            public void visit(OWLAnnotationProperty property) {
                writeAnnotationProperty(property, builder);
            }
        });
        frameBlockTagRenderer.renderClosingTag(builder);
    }

    private <E extends OWLObject> void writeFrame(FrameRenderer<E> renderer, E subject, StringBuilder builder) {
        for(FrameSectionRenderer<E, ?, ?> ren : renderer.getSectionRenderers()) {
            renderSection(ren, subject, builder);
        }
    }

    private void writeClassFrameContent(final OWLClass cls, StringBuilder builder) {
        writeFrame(new ClassFrameRenderer(), cls, builder);
    }

    private void writeObjectPropertyFrameContent(final OWLObjectProperty property, StringBuilder builder) {
        writeFrame(new ObjectPropertyFrameRenderer(), property, builder);
    }

    private void writeDataPropertyFrameContent(final OWLDataProperty property, StringBuilder builder) {
        writeFrame(new DataPropertyFrameRenderer(), property, builder);
    }

    private void writeNamedIndividualFrameContent(final OWLNamedIndividual individual, StringBuilder builder) {
        writeFrame(new NamedIndividualFrameRenderer(), individual, builder);
    }

    private void writeAnnotationProperty(OWLAnnotationProperty property, StringBuilder builder) {
        writeFrame(new AnnotationPropertyFrameRenderer(), property, builder);
    }

    private void renderEscaped(String s, StringBuilder builder) {
        builder.append(new SafeHtmlBuilder().appendEscaped(s).toSafeHtml().asString());
    }


    private <E extends OWLObject, I, R> void renderSection(FrameSectionRenderer<E, I, R> renderer, E subject, StringBuilder builder) {
        Multimap<OWLOntology, I> items = getOntologyObjectPairs2(renderer, subject);
        if (items.isEmpty()) {
            return;
        }
        for (OWLOntology ontology : items.keySet()) {
            Collection<I> sectionItems = items.get(ontology);
            if(sectionItems.isEmpty()) {
                break;
            }
            sectionTagRenderer.renderOpeningTag(builder);
            renderSectionHeader(renderer.getSection(), builder, Optional.of(ontology));
            sectionBlockTagRenderer.renderOpeningTag(builder);
            List<I> sectionItemsList = Lists.newArrayList(sectionItems);
            for (Iterator<I> sectionItemIt = sectionItemsList.iterator(); sectionItemIt.hasNext(); ) {
                ElementTagRenderer elementTagRenderer = getSectionItemTagRenderer(renderer);
                elementTagRenderer.renderOpeningTag(builder);
                I sectionItem = sectionItemIt.next();

                // Annotations
                List<OWLAnnotation> annotations = renderer.getAnnotations(sectionItem);
                if (nestedAnnotationStyle == NestedAnnotationStyle.MANCHESTER_SYNTAX) {
                    // They come before the thing that is annotated
                    renderAnnotations(renderer, builder, annotations);
                }
                List<R> renderables = renderer.getRenderablesForItem(subject, sectionItem, ontology);
                for (int index = 0; index < renderables.size(); index++) {
                    Object renderable = renderables.get(index);
                    String rendering = getRendering(renderable, renderer);
                        builder.append(rendering);
                    boolean hasNext = index < renderables.size() - 1;
                    if (hasNext) {
                        builder.append(renderer.getSeparatorAfter(index, renderables));
                    }
                }
                if (sectionItemIt.hasNext()) {
                    renderItemSeparator(builder);
                }
                if(nestedAnnotationStyle == NestedAnnotationStyle.COMPACT) {
                    renderAnnotations(renderer, builder, annotations);
                }
                elementTagRenderer.renderClosingTag(builder);
            }
            sectionBlockTagRenderer.renderClosingTag(builder);
            sectionTagRenderer.renderClosingTag(builder);
        }
    }

    private <E extends OWLObject, I, R> void renderAnnotations(
            FrameSectionRenderer<E, I, R> renderer,
            StringBuilder builder,
            List<OWLAnnotation> annotations) {
        if (!annotations.isEmpty()) {
            annotationsTagRenderer.renderOpeningTag(builder);
            if (nestedAnnotationStyle != NestedAnnotationStyle.COMPACT) {
                renderSectionHeader(ManchesterOWLSyntax.ANNOTATIONS, builder, Optional.<OWLOntology>absent());
            }
            annotationsBlockTagRenderer.renderOpeningTag(builder);
            for(OWLAnnotation annotation : annotations) {
                String annoRendering = getRendering(annotation, renderer);
                ElementTagRenderer annotationItemTagRenderer = new ElementRenderer("div", "ms-nested-annotation");
                annotationItemTagRenderer.renderOpeningTag(builder);
                builder.append(annoRendering);
                renderAnnotations(renderer, builder, Lists.newArrayList(annotation.getAnnotations()));
                annotationItemTagRenderer.renderClosingTag(builder);
            }
            annotationsBlockTagRenderer.renderClosingTag(builder);
            annotationsTagRenderer.renderClosingTag(builder);
        }
    }

    private void renderItemSeparator(StringBuilder builder) {
        itemSeparatorTagRenderer.renderOpeningTag(builder);
        builder.append(", ");
        itemSeparatorTagRenderer.renderClosingTag(builder);
    }

    private ElementTagRenderer getSectionItemTagRenderer(FrameSectionRenderer<?, ?, ?> renderer) {
        if(renderer.getSectionFormatting() == FrameSectionRenderer.Formatting.LINE_PER_ITEM) {
            return new ElementRenderer("div", "ms-section-item");
        }
        else {
            return new ElementRenderer("span", "ms-section-item");
        }
    }


    private void renderSectionHeader(ManchesterOWLSyntax sectionKeyword, StringBuilder builder, Optional<OWLOntology> ont) {
        sectionKwTagRenderer.renderOpeningTag(builder);
        builder.append(sectionKeyword.keyword());
        sectionKwTagRenderer.renderClosingTag(builder);
        if (ont.isPresent()) {
            OWLOntology ontology = ont.get();
            if (!ontology.isAnonymous()) {
                inOntologyTagRenderer.renderOpeningTag(builder);
                Optional<IRI> ontologyIRI = ontology.getOntologyID().getOntologyIRI();
                if (ontologyIRI.isPresent()) {
                    builder.append("    [in ");
                    builder.append(ontologyIRIShortFormProvider.getShortForm(ontologyIRI.get()));
                    builder.append("]");
                }
                inOntologyTagRenderer.renderClosingTag(builder);
            }
        }
    }

    private String getRendering(Object renderable, FrameSectionRenderer<?,?,?> renderer) {
        String rendering = "";
        if (renderable instanceof OWLObject) {
            Optional<String> itemStyle = itemStyleProvider.getItemStyle(renderable);
            if(itemStyle.isPresent()) {
                rendering = "<span class=\"" + itemStyle.get() + "\">";
            }
            rendering += objectRenderer.render((OWLObject) renderable, highlightChecker, deprecatedChecker);
            if(itemStyle.isPresent()) {
                rendering += "</span>";
            }
        }
        else {
            rendering = renderable.toString();
        }
        return rendering;
    }



    public <S extends OWLObject, I> Multimap<OWLOntology, I> getOntologyObjectPairs2(FrameSectionRenderer<S,I,?> renderer, S subject) {
        Multimap<OWLOntology, I> result = LinkedHashMultimap.create();
        for (OWLOntology ont : rootOntology.getImportsClosure()) {
            for (I item : renderer.getItemsInOntology(subject, ont, shortFormProvider, null)) {
                result.put(ont, item);
            }
        }
        return result;
    }




    private static interface ElementTagRenderer {
        void renderOpeningTag(StringBuilder stringBuilder);
        void renderClosingTag(StringBuilder stringBuilder);
    }

    private static class ElementRenderer implements ElementTagRenderer {

        private String elementName;

        private String className;

        private ElementRenderer(String elementName, String className) {
            this.elementName = elementName;
            this.className = className;
        }

        @Override
        public void renderOpeningTag(StringBuilder stringBuilder) {
            stringBuilder.append("<");
            stringBuilder.append(elementName);
            stringBuilder.append(" class=\"");
            stringBuilder.append(className);
            stringBuilder.append("\">");
        }

        @Override
        public void renderClosingTag(StringBuilder stringBuilder) {
            stringBuilder.append("</");
            stringBuilder.append(elementName);
            stringBuilder.append(">");
        }
    }
}
