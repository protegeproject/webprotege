package edu.stanford.bmir.protege.web.server.util;

public class JavaUtil {
    /**
     * The purpose of this cast utility to be able to suppress unchecked
     * cast in one and only one place so we don't have to pollute the main
     * application code with @SuppressWarnings and thus be able to flag
     * places where the type cast warning(s) are/were unexpected.
     * @param <T>
     * @param obj
     * @return
     *
     * This great idea came from:
     * @see http://www.whizu.org/articles/how-to-avoid-unchecked-cast-warnings-with-java-generics.whizu
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
      return (T)obj;
    }
}
