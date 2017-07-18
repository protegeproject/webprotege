package edu.stanford.bmir.protege.web.shared.util;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.project.ProjectIdFormatException;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2017
 */
public class UUIDUtil {

    /**
     * A regular expression that specifies a pattern for a UUID
     */
    public static final transient String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    private static final transient RegExp REG_EXP = RegExp.compile(UUID_PATTERN);

    /**
     * Checks that the specified string matches the UUID pattern {@link #UUID_PATTERN}.
     * @param id The string to check.
     * @return The specified string.
     * @throws ProjectIdFormatException if the specified string does not match the UUID pattern.
     */
    public static boolean isWellFormed(@Nonnull String id) {
        MatchResult result = REG_EXP.exec(checkNotNull(id));
        return result != null;
    }

    /**
     * Get the regular expression that specifies the lexical format of UUIDs.  The returned regular expression
     * specifies a UUID format consisting of a series of characters from the range a-z0-9 separated by dashes.  The
     * first block contains 8 characters, the second block 4 characters, the third block 4 characters, the fourth
     * block 4 characters, and the fifth block 12 characters.  For example, cb88785a-bfc5-4299-9b5b-7920451aba06.
     * @return The {@link RegExp} for UUID lexical values.  Not {@code null}.
     */
    public static RegExp getIdRegExp() {
        return REG_EXP;
    }

}
