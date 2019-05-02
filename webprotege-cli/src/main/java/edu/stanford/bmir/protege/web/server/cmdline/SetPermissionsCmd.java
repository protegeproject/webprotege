package edu.stanford.bmir.protege.web.server.cmdline;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-05-02
 */
public class SetPermissionsCmd extends Cmd {

    public SetPermissionsCmd() {
        super("set-permissions", "Sets the user permissions for a user and a project");
    }

    @Override
    public void run(@Nonnull List<String> args) {
        SetPermissions.main(args.toArray(new String[args.size()]));
    }
}
