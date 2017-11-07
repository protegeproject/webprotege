package edu.stanford.bmir.protege.web.server.cmdline;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Nov 2017
 */
public class RebuildPermissionsCmd extends Cmd {

    public RebuildPermissionsCmd() {
        super("rebuild-permissions", "Rebuilds the WebProtege permissions");
    }

    @Override
    public void run(@Nonnull List<String> args) {
        RebuildPermissions.main(args.toArray(new String [args.size()]));
    }
}

