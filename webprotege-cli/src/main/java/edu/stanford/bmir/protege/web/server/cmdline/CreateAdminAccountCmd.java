package edu.stanford.bmir.protege.web.server.cmdline;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Nov 2017
 */
public class CreateAdminAccountCmd extends Cmd {

    public CreateAdminAccountCmd() {
        super("create-admin-account", "Creates an admin account in WebProtege");
    }

    @Override
    public void run(@Nonnull List<String> args) {
        SetupTools.main(args.toArray(new String [args.size()]));
    }
}
