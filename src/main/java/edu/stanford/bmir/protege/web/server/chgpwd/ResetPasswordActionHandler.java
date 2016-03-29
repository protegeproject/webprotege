package edu.stanford.bmir.protege.web.server.chgpwd;

import edu.stanford.bmir.protege.web.server.IdUtil;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationManager;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.auth.PasswordDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltProvider;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResultCode;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResultCode.*;
import static edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResultCode.INVALID_EMAIL_ADDRESS;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordActionHandler implements ActionHandler<ResetPasswordAction, ResetPasswordResult> {


    private final UserDetailsManager userDetailsManager;

    private final AuthenticationManager authenticationManager;

    private final SaltProvider saltProvider;

    private final PasswordDigestAlgorithm passwordDigestAlgorithm;

    private final ResetPasswordMailer mailer;

    private final WebProtegeLogger logger;


    @Inject
    public ResetPasswordActionHandler(UserDetailsManager userDetailsManager,
                                      AuthenticationManager authenticationManager,
                                      SaltProvider saltProvider,
                                      PasswordDigestAlgorithm passwordDigestAlgorithm,
                                      ResetPasswordMailer mailer,
                                      WebProtegeLogger logger) {
        this.userDetailsManager = userDetailsManager;
        this.authenticationManager = authenticationManager;
        this.saltProvider = saltProvider;
        this.passwordDigestAlgorithm = passwordDigestAlgorithm;
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
        final String emailAddress = action.getResetPasswordData().getEmailAddress();
        try {
            Optional<UserId> userId = userDetailsManager.getUserByUserIdOrEmail(emailAddress);
            if(!userId.isPresent()) {
                return new ResetPasswordResult(INVALID_EMAIL_ADDRESS);
            }
            Optional<UserDetails> userDetails = userDetailsManager.getUserDetails(userId.get());
            if(!userDetails.isPresent()) {
                return new ResetPasswordResult(INVALID_EMAIL_ADDRESS);
            }
            if(!userDetails.get().getEmailAddress().isPresent()) {
                return new ResetPasswordResult(INVALID_EMAIL_ADDRESS);
            }
            if(!userDetails.get().getEmailAddress().get().equalsIgnoreCase(emailAddress)) {
                return new ResetPasswordResult(INVALID_EMAIL_ADDRESS);
            }
            String pwd = IdUtil.getBase62UUID();
            Salt salt = saltProvider.get();
            SaltedPasswordDigest saltedPasswordDigest = passwordDigestAlgorithm.getDigestOfSaltedPassword(pwd, salt);
            authenticationManager.setDigestedPassword(userId.get(), saltedPasswordDigest, salt);
            mailer.sendEmail(userId.get(), emailAddress, pwd);
            logger.info("The password for %s has been reset.  " +
                            "An email has been sent to %s that contains the new password.",
                    executionContext.getUserId().getUserName(),
                    emailAddress
            );
            return new ResetPasswordResult(SUCCESS);
        } catch (Exception e) {
            logger.info("Could not reset the user password " +
                                "associated with the email " +
                                "address %s.  The following " +
                                "error occurred: %s.", emailAddress, e.getMessage());
            return new ResetPasswordResult(INTERNAL_ERROR);
        }
    }
}
