package edu.stanford.bmir.protege.web.server.owlapi.notes;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2012
 */
public class NoteBody {

    private String body;



    public NoteBody(String content) {
        if (content == null) {
            throw new NullPointerException("body must not be null");
        }
        this.body = content;
    }

    public String getBody() {
        return body;
    }


    @Override
    public int hashCode() {
        return 17 * body.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NoteBody)) {
            return false;
        }
        NoteBody other = (NoteBody) obj;
        return other.getBody().equals(body);
    }
}
