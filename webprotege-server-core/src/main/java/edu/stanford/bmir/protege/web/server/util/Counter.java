package edu.stanford.bmir.protege.web.server.util;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Apr 2018
 */
public class Counter {

    private int counter;

    public void increment() {
        counter++;
    }

    /**
     * A convenience method that allows a counter the easily by used in streams as a lambda expression.
     * @param object An object that will be passed through this method.
     */
    public <T> T increment(T object) {
        counter++;
        return object;
    }

    public int getCounter() {
        return counter;
    }

    public void reset() {
        counter = 0;
    }
}
