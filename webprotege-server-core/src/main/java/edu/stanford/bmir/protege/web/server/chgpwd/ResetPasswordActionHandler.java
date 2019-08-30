package edu.stanford.bmir.protege.web.server.chgpwd;

import edu.stanford.bmir.protege.web.server.auth.AuthenticationManager;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.server.util.IdUtil;
import edu.stanford.bmir.protege.web.shared.auth.PasswordDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltProvider;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResultCode.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordActionHandler implements ApplicationActionHandler<ResetPasswordAction, ResetPasswordResult> {


    private final UserDetailsManager userDetailsManager;

    private final AuthenticationManager authenticationManager;

    private final SaltProvider saltProvider;

    private final PasswordDigestAlgorithm passwordDigestAlgorithm;

    private final ResetPasswordMailer mailer;

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordActionHandler.class);


    @Inject
    public ResetPasswordActionHandler(UserDetailsManager userDetailsManager,
                                      AuthenticationManager authenticationManager,
                                      SaltProvider saltProvider,
                                      PasswordDigestAlgorithm passwordDigestAlgorithm,
                                      ResetPasswordMailer mailer) {
        this.userDetailsManager = userDetailsManager;
        this.authenticationManager = authenticationManager;
        this.saltProvider = saltProvider;
        this.passwordDigestAlgorithm = passwordDigestAlgorithm;
        this.mailer = mailer;
    }

    @Nonnull
    @Override
    public Class<ResetPasswordAction> getActionClass() {
        return ResetPasswordAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(
            @Nonnull ResetPasswordAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public ResetPasswordResult execute(
            @Nonnull ResetPasswordAction action, @Nonnull ExecutionContext executionContext) {
        final String emailAddress = action.getResetPasswordData().getEmailAddress();
        try {
            Optional<UserId> userId = userDetailsManager.getUserByUserIdOrEmail(emailAddress);
            if(userId.isEmpty()) {
                return new ResetPasswordResult(INVALID_EMAIL_ADDRESS);
            }
            Optional<UserDetails> userDetails = userDetailsManager.getUserDetails(userId.get());
            if(userDetails.isEmpty()) {
                return new ResetPasswordResult(INVALID_EMAIL_ADDRESS);
            }
            if(userDetails.get().getEmailAddress().isEmpty()) {
                return new ResetPasswordResult(INVALID_EMAIL_ADDRESS);
            }
            if(!userDetails.get().getEmailAddress().get().equalsIgnoreCase(emailAddress)) {
                return new ResetPasswordResult(INVALID_EMAIL_ADDRESS);
            }
            String pwd = IdUtil.getBase62UUID();
            Salt salt = saltProvider.get();
            SaltedPasswordDigest saltedPasswordDigest = passwordDigestAlgorithm.getDigestOfSaltedPassword(pwd, salt);
            authenticationManager.setDigestedPassword(userId.get(), saltedPasswordDigest, salt);
            mailer.sendEmail(userId.get(), emailAddress, pwd, ex -> {
                throw new RuntimeException(ex);
            });
            logger.info("The password for {} has been reset.  " +
                            "An email has been sent to {} that contains the new password.",
                    userId.get().getUserName(),
                    emailAddress
            );
            return new ResetPasswordResult(SUCCESS);
        } catch (Exception e) {
            logger.error("Could not reset the user password " +
                                "associated with the email " +
                                "address {}.  The following " +
                                "error occurred: {}.", emailAddress, e.getMessage(), e);
            return new ResetPasswordResult(INTERNAL_ERROR);
        }
    }
}
