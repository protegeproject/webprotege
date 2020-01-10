package edu.stanford.bmir.protege.web.shared.entity;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.HasLexicalForm;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class OWLLiteralData extends OWLPrimitiveData implements HasLexicalForm {

    public static OWLLiteralData get(@Nonnull OWLLiteral literal) {
        return new AutoValue_OWLLiteralData(ImmutableMap.of(), literal);
    }

    @Nonnull
    @Override
    public abstract OWLLiteral getObject();

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.LITERAL;
    }


    public OWLLiteral getLiteral() {
        return getObject();
    }

    @Override
    public String getBrowserText() {
        OWLLiteral literal = getLiteral();
        return literal.getLiteral();
    }

    @Override
    public String getUnquotedBrowserText() {
        return getBrowserText();
    }

    @Override
    public String getLexicalForm() {
        return getLiteral().getLiteral();
    }

    public boolean hasLang() {
        return getLiteral().hasLang();
    }

    @Nonnull
    public String getLang() {
        return getLiteral().getLang();
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return defaultValue;
    }

    @Override
    public Optional<OWLAnnotationValue> asAnnotationValue() {
        return Optional.of(getLiteral());
    }

    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.empty();
    }

    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.of(getLiteral());
    }
}
