package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultimap;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.util.*;

/**
* @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
*/
public class AnnotationsGroup {

    private OWLAnnotationProperty property;

    private Collection<OWLAnnotationValue> values;

//    private AnnotationPropertyComparator annotationPropertyComparator;

    public AnnotationsGroup(OWLAnnotationProperty property, Collection<OWLAnnotationValue> values) {
        this.property = property;
        this.values = values;
//        this.annotationPropertyComparator = new DefaultAnnotationPropertyComparator();
    }

    public OWLAnnotationProperty getProperty() {
        return property;
    }

    public Collection<OWLAnnotationValue> getValues() {
        return values;
    }

    public List<OWLObject> getRenderables() {
        List<OWLObject> result = new ArrayList<OWLObject>();
        result.add(property);
        result.addAll(values);
        return result;
    }

    public static List<AnnotationsGroup> getAnnotationSets(OWLOntology ont, ShortFormProvider shortFormProvider) {
        Set<OWLAnnotation> annotations = ont.getAnnotations();
        return getAnnotationGroups(annotations, shortFormProvider);
    }

    private static List<AnnotationsGroup> getAnnotationGroups(Set<OWLAnnotation> annotations, final ShortFormProvider shortFormProvider) {
        Multimap<OWLAnnotationProperty, OWLAnnotationValue> grouped = TreeMultimap.create();
        Set<OWLAnnotation> standalone = new HashSet<OWLAnnotation>();
        for (OWLAnnotation anno : annotations) {
            if (canBeGrouped(anno)) {
                grouped.put(anno.getProperty(), anno.getValue());
            }
            else {
                standalone.add(anno);
            }
        }
        List<AnnotationsGroup> result = Lists.newArrayList();
        for (OWLAnnotationProperty prop : grouped.keySet()) {
            List<OWLObject> renderables = Lists.newArrayList();
            renderables.add(prop);
            renderables.addAll(grouped.get(prop));
            result.add(new AnnotationsGroup(prop, grouped.get(prop)));
        }
        for (OWLAnnotation anno : standalone) {
            result.add(new AnnotationsGroup(anno.getProperty(),
                    Collections.singleton(anno.getValue())));
        }
//        Collections.sort(result, new AnnotationsGroupComparator(new DefaultAnnotationPropertyComparator(shortFormProvider), shortFormProvider));
        return result;
    }

    private static boolean canBeGrouped(OWLAnnotation anno) {
        return false;//isLiteralUnderGroupingLength(anno) || anno.getValue() instanceof IRI;
    }

    private static boolean isLiteralUnderGroupingLength(OWLAnnotation anno) {
        return anno.getValue() instanceof OWLLiteral && ((OWLLiteral) anno.getValue()).getLiteral().length() < 50;
    }

    public static List<AnnotationsGroup> getAnnotationGroups(OWLAnnotationSubject entity,
                                                             OWLOntology ont,
                                                             ShortFormProvider shortFormProvider) {
        Set<OWLAnnotation> annos = Sets.newHashSet();
        for(OWLAnnotationAssertionAxiom ax : ont.getAnnotationAssertionAxioms(entity)) {
            annos.add(ax.getAnnotation());
        }
        return getAnnotationGroups(annos, shortFormProvider);
    }
}
