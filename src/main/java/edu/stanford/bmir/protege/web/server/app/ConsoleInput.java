package edu.stanford.bmir.protege.web.server.app;

import javax.annotation.Nonnull;
import java.io.Console;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Mar 2017
 *
 * A wrapper around Console that is easy to mock in unit tests.
 */
public class ConsoleInput {

    private final Console console;

    public ConsoleInput(@Nonnull Console console) {
        this.console = checkNotNull(console);
    }

    public String readLine() {
        return console.readLine();
    }

    public char [] readPassword() {
        return console.readPassword();
    }

    public void printf(String format, Object ... args) {
        console.printf(format, args);
    }
}
