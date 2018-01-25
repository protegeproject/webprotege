package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;
import org.obolibrary.obo2owl.Obo2OWLConstants;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class XRefConverter {

    private static Pattern SEPARATOR_PATTERN = Pattern.compile("([^#_|_]+)(#_|_)(.+)");

    @Inject
    public XRefConverter() {
    }

    /**
     * Convert the specified value and description to an OBO XRef
     * @param value The value.
     * @param description The description
     * @return The corresponding OBOXRef
     */
    @Nonnull
    public OBOXRef toOBOXRef(@Nonnull String value,
                             @Nonnull String description) {
        // Need to peel apart the ID
        if (value.startsWith(Obo2OWLConstants.DEFAULT_IRI_PREFIX)) {
            String localValue = value.substring(Obo2OWLConstants.DEFAULT_IRI_PREFIX.length());
            Matcher matcher = SEPARATOR_PATTERN.matcher(localValue);
            if (matcher.matches()) {
                String dbname = unescapeSpaces(matcher.group(1));
                String dbid = matcher.group(3);
                return new OBOXRef(dbname, dbid, description);
            }
            else {
                return new OBOXRef("", value, description);
            }
        }
        else {
            final int nameIdSeparatorIndex = value.indexOf(':');
            if (nameIdSeparatorIndex != -1) {
                return new OBOXRef(value.substring(0, nameIdSeparatorIndex), value.substring(nameIdSeparatorIndex + 1), description);
            }
            return new OBOXRef("", value, description);
        }
    }

    private String unescapeSpaces(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("%20", " ");
    }
}
