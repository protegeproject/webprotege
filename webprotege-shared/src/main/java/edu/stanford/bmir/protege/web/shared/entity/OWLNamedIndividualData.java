package edu.stanford.bmir.protege.web.shared.entity;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class OWLNamedIndividualData extends OWLEntityData {


    public static OWLNamedIndividualData get(@Nonnull OWLNamedIndividual individual,
                                             @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return new AutoValue_OWLNamedIndividualData(shortForms, false, individual);
    }

    public static OWLNamedIndividualData get(@Nonnull OWLNamedIndividual individual,
                                             @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms,
                                             boolean depreacted) {
        return new AutoValue_OWLNamedIndividualData(shortForms, depreacted, individual);
    }

    @Nonnull
    @Override
    public abstract OWLNamedIndividual getObject();

    @Override
    public OWLNamedIndividual getEntity() {
        return getObject();
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.NAMED_INDIVIDUAL;
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return visitor.visit(getEntity());
    }

    @Override
    public <R> R accept(OWLEntityDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

}
