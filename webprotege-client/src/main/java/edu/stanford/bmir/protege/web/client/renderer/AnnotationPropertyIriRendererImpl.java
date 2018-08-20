package edu.stanford.bmir.protege.web.client.renderer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.tag.GetEntityTagsAction;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Jun 2018
 */
@ProjectSingleton
public class AnnotationPropertyIriRendererImpl implements AnnotationPropertyIriRenderer {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatchService;

    private final Multimap<IRI, Consumer<OWLAnnotationPropertyData>> pending = HashMultimap.create();

    @Inject
    public AnnotationPropertyIriRendererImpl(@Nonnull ProjectId projectId,
                                             @Nonnull DispatchServiceManager dispatchService) {
        this.projectId = checkNotNull(projectId);
        this.dispatchService = checkNotNull(dispatchService);
    }

    @Override
    public void renderAnnotationPropertyIri(@Nonnull IRI iri,
                                            @Nonnull Consumer<OWLAnnotationPropertyData> renderingConsumer) {
        if(!pending.get(iri).isEmpty()) {
            pending.put(iri, renderingConsumer);
            return;
        }
        pending.put(iri, renderingConsumer);
        // Don't go to server for well known renderings
        if(iri.equals(OWLRDFVocabulary.RDFS_LABEL.getIRI())) {
            propagateToPending(DataFactory.getRdfsLabelData());
        }
        else if(iri.equals(SKOSVocabulary.PREFLABEL.getIRI())) {
            propagateToPending(DataFactory.getSkosPrefLabelData());
        }
        else {
            dispatchService.execute(new GetEntityRenderingAction(projectId, DataFactory.getOWLAnnotationProperty(iri)),
                                    result -> {
                                        OWLAnnotationPropertyData ed = (OWLAnnotationPropertyData) result.getEntityData();
                                        propagateToPending(ed);
                                    });
        }

    }

    private void propagateToPending(OWLAnnotationPropertyData ed) {
        pending.removeAll(ed.getEntity().getIRI()).forEach(consumer -> consumer.accept(ed));
    }
}
