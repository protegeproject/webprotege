package edu.stanford.bmir.protege.web.server.chgpwd;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordActionHandler implements ActionHandler<ResetPasswordAction, ResetPasswordResult> {

    private final WebProtegeLogger logger;

    private final ResetPasswordMailer mailer;

    @Inject
    public ResetPasswordActionHandler(
            ResetPasswordMailer mailer,
            WebProtegeLogger logger) {
        this.mailer = mailer;
        this.logger = logger;
    }

    @Override
    public Class<ResetPasswordAction> getActionClass() {
        return ResetPasswordAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(
            ResetPasswordAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public ResetPasswordResult execute(
            ResetPasswordAction action, ExecutionContext executionContext) {
//        final String emailAddress = action.getResetPasswordData().getEmailAddress();
//        try {
//            Optional<User> user = userDetailsManager.getUserByUserIdOrEmail(emailAddress);
//            if(!user.isPresent()) {
//                return new ResetPasswordResult(INVALID_EMAIL_ADDRESS);
//            }
//            if(user.get().getEmail() == null) {
//                return new ResetPasswordResult(INVALID_EMAIL_ADDRESS);
//            }
//            if(user.get().getEmail().compareToIgnoreCase(emailAddress) != 0) {
//                return new ResetPasswordResult(INVALID_EMAIL_ADDRESS);
//            }
//            String pwd = IdUtil.getBase62UUID();
//            user.get().setPassword(pwd);
//            mailer.sendEmail(executionContext.getUserId(), emailAddress, pwd);
//            logger.info("The password for %s has been reset.  " +
//                            "An email has been sent to %s that contains the new password.",
//                    executionContext.getUserId().getUserName(),
//                    emailAddress
//            );
//            return new ResetPasswordResult(SUCCESS);
//        } catch (Exception e) {
//            logger.info("Could not reset the user password " +
//                                "associated with the email " +
//                                "address %s.  The following " +
//                                "error occurred: %s.", emailAddress, e.getMessage());
//            return new ResetPasswordResult(INTERNAL_ERROR);
//        }
        throw new RuntimeException();
    }
}
