package edu.stanford.bmir.protege.web.server.change;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */
public class FixedMessageChangeDescriptionGenerator<S> implements ChangeDescriptionGenerator<S> {

    private String message;

    public static <S> FixedMessageChangeDescriptionGenerator<S> get(String message) {
        return new FixedMessageChangeDescriptionGenerator<S>(message);
    }

    public FixedMessageChangeDescriptionGenerator(String message) {
        this.message = message;
    }

    @Override
    public String generateChangeDescription(ChangeApplicationResult<S> result) {
        return message;
    }
}
