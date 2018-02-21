package edu.stanford.bmir.protege.web.shared.dispatch;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public interface UpdateObjectAction<T> extends Action<Result> {

    T getFrom();

    T getTo();

}
