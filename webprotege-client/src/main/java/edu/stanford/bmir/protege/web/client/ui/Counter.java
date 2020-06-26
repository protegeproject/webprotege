package edu.stanford.bmir.protege.web.client.ui;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class Counter {

    private int counter = 0;

    public int get() {
        return counter;
    }

    public String getValue() {
        return Integer.toString(counter);
    }

    public void increment() {
        counter++;
    }
}
