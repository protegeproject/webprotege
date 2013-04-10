package edu.stanford.bmir.protege.web.shared.genericframe.grammar;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2013
 */
public class PathDoesNotStartWithPrefixException extends RuntimeException implements Serializable {

    private SectionGrammarNodePath path;

    private SectionGrammarNodePath prefix;

    /**
     * For the purposes of serialisation only
     */
    private PathDoesNotStartWithPrefixException() {
    }

    /**
     * Constructs a new runtime exception with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public PathDoesNotStartWithPrefixException(SectionGrammarNodePath path, SectionGrammarNodePath prefix) {
        this.path = path;
        this.prefix = prefix;
    }

    public SectionGrammarNodePath getPath() {
        return path;
    }

    public SectionGrammarNodePath getPrefix() {
        return prefix;
    }
}
