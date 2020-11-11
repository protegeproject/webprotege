package edu.stanford.bmir.protege.web.server.attestation;

import ch.unifr.digits.webprotege.attestation.server.AttestationService;
import ch.unifr.digits.webprotege.attestation.shared.VerifyAction;
import ch.unifr.digits.webprotege.attestation.shared.VerifyResult;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class AttestationVerifyActionHandler implements ProjectActionHandler<VerifyAction, VerifyResult> {

    private static final Logger log = LoggerFactory.getLogger(AttestationVerifyActionHandler.class);


    @Inject
    public AttestationVerifyActionHandler() {}

    /**
     * Gets the class of {@link Action} handled by this handler.
     *
     * @return The class of {@link Action}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public Class<VerifyAction> getActionClass() {
        return VerifyAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull VerifyAction action, @Nonnull RequestContext requestContext) {
        return new NullValidator();
    }

    @Nonnull
    @Override
    public VerifyResult execute(@Nonnull VerifyAction action, @Nonnull ExecutionContext executionContext) {
        try {
            VerifyResult result = AttestationService.verifyEntity(action.getIri(), action.getVersionIri(),
                    Integer.parseInt(action.getHash()));
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new VerifyResult(false, "", "", -1);
    }
}
