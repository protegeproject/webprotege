package edu.stanford.bmir.protege.web.server.project;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-04-30
 */
@AutoValue
public abstract class BuiltInPrefixDeclarations {

    @Nonnull
    public static BuiltInPrefixDeclarations get(@Nonnull ImmutableList<PrefixDeclaration> prefixDeclarations) {
        return new AutoValue_BuiltInPrefixDeclarations(prefixDeclarations);
    }

    /**
     * Gets a list of prefix declarations that are considered to be built in prefix
     * declarations.
     */
    @Nonnull
    public abstract ImmutableList<PrefixDeclaration> getPrefixDeclarations();
}
