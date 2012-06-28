package edu.stanford.bmir.protege.web.server.owlapi.notes;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2012
 */
public class NoteAuthor {

    private String name;

    public NoteAuthor(String name) {
        if(name == null) {
            throw new NullPointerException("name must not be null");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NoteAuthor)) {
            return false;
        }
        NoteAuthor other = (NoteAuthor) obj;
        return other.getName().equals(name);
    }


}
