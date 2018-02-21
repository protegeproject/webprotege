package edu.stanford.bmir.protege.web.server.cmdline;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Nov 2017
 */
public class WebProtegeCli {


    private Map<String, Cmd> commands = new TreeMap<>();

    public WebProtegeCli(@Nonnull Collection<Cmd> commands) {
        commands.forEach(cmd -> this.commands.put(cmd.getName(), cmd));
    }

    public static WebProtegeCli create() {
        return new WebProtegeCli(asList(
                new CreateAdminAccountCmd(),
                new RebuildPermissionsCmd()
        ));
    }

    public void run(@Nonnull List<String> args) {
        checkNotNull(args);
        if(args.size() == 0) {
            printHelp();
            return;
        }
        final String cmdName = args.get(0);
        Cmd cmd = commands.get(cmdName);
        if(cmd == null) {
            System.out.printf("Unknown command '%s'\n", cmdName);
            printHelp();
            return;
        }
        final List<String> cmdArgs;
        if(args.size() > 1) {
            cmdArgs = args.subList(1, args.size());
        }
        else {
            cmdArgs = Collections.emptyList();
        }
        cmd.run(cmdArgs);
    }

    private void printHelp() {
        System.out.printf("Available commands:\n");
        commands.forEach((name, cmd) -> System.out.printf("    %s    %s\n", name, cmd.getHelp()));
    }

    public static void main(String[] args) {
        WebProtegeCli cli = WebProtegeCli.create();
        cli.run(asList(args));
    }
}
