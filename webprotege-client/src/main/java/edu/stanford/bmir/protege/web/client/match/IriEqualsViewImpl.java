package edu.stanford.bmir.protege.web.client.match;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.*;
import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
public class IriEqualsViewImpl extends Composite implements IriEqualsView {

    interface IriEqualsViewImplUiBinder extends UiBinder<HTMLPanel, IriEqualsViewImpl> {

    }

    private static IriEqualsViewImplUiBinder ourUiBinder = GWT.create(IriEqualsViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl iriEditor;

    @Inject
    public IriEqualsViewImpl(PrimitiveDataEditorImpl editor) {
        this.iriEditor = checkNotNull(editor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public Optional<IRI> getIri() {
            return iriEditor.getValue()
                            .map((OWLPrimitiveData pd) -> {
                                IRI iri = null;
                                if(pd instanceof OWLEntityData) {
                                    iri = ((OWLEntityData) pd).getEntity().getIRI();
                                }
                                else if(pd instanceof IRIData) {
                                    iri = ((IRIData) pd).getObject();
                                }
                                return iri;
                            })
                            .filter(Objects::nonNull);
    }

    @Override
    public void setIri(IRI iri) {
        this.iriEditor.setValue(IRIData.get(iri, ImmutableMap.of()));
    }
}