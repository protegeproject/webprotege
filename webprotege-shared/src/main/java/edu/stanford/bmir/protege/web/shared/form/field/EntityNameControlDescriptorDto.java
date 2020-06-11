package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class EntityNameControlDescriptorDto implements FormControlDescriptorDto {

    @Nonnull
    public static EntityNameControlDescriptorDto get(@Nonnull LanguageMap placeholder,
                                                     @Nullable CompositeRootCriteria matchCriteria) {
        return new AutoValue_EntityNameControlDescriptorDto(placeholder, matchCriteria);
    }

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public EntityNameControlDescriptor toFormControlDescriptor() {
        return EntityNameControlDescriptor.get(getPlaceholder(), getMatchCriteriaInternal());
    }

    @Nonnull
    public abstract LanguageMap getPlaceholder();

    @JsonIgnore
    @Nullable
    protected abstract CompositeRootCriteria getMatchCriteriaInternal();

    @Nonnull
    public Optional<CompositeRootCriteria> getMatchCriteria() {
        return Optional.ofNullable(getMatchCriteriaInternal());
    }
}
