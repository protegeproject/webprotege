package edu.stanford.bmir.protege.web.shared;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/08/2012
 */
public interface HasLexicalForm {

    /**
     * Gets the lexical form for a particular object.  This may be (and probably is) different to the lexical rendering
     * provided by a {@link Object#toString()} method.  The exact semantics will of course be specified by the implementing
     * class or extending interface.
     * @return The lexical form.  Not {@code null}.  May be the empty string.
     */
    String getLexicalForm();
}
