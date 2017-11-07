package edu.stanford.bmir.protege.web.server.cmdline;

import javax.annotation.Nonnull;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Nov 2017
 */
public abstract class Cmd {

    @Nonnull
    private final String name;

    @Nonnull
    private final String help;

    public Cmd(@Nonnull String name, @Nonnull String help) {
        this.name = checkNotNull(name);
        this.help = checkNotNull(help);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public String getHelp() {
        return help;
    }

    /**
     * Run this command with the specified arguments.
     * @param args The arguments.  Note that the arguments does not include the name of the command.
     */
    public abstract void run(@Nonnull List<String> args);
}
