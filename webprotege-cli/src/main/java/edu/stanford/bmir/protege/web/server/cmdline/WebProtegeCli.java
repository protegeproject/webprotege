package edu.stanford.bmir.protege.web.server.cmdline;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.logging.LogManager;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Nov 2017
 */
public class WebProtegeCli {

    private static Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    static {
        root.setLevel(Level.ERROR);
        LogManager.getLogManager().reset();
    }

    private Map<String, Cmd> commands = new TreeMap<>();

    public WebProtegeCli(@Nonnull Collection<Cmd> commands) {
        commands.forEach(cmd -> this.commands.put(cmd.getName(), cmd));
    }

    public static WebProtegeCli create() {
        return new WebProtegeCli(asList(
                new CreateAdminAccountCmd(),
                new RebuildPermissionsCmd(),
                new GenerateApiKeyCmd(),
                new SetPermissionsCmd()
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
        disableWarning();
        WebProtegeCli cli = WebProtegeCli.create();
        cli.run(asList(args));
    }

    public static void disableWarning() {
        System.err.close();
        System.setErr(System.out);
    }
}
