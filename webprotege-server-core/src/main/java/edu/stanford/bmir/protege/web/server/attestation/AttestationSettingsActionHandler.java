package edu.stanford.bmir.protege.web.server.attestation;

import ch.unifr.digits.webprotege.attestation.server.SettingsManager;
import ch.unifr.digits.webprotege.attestation.shared.GetAttestationSettingsAction;
import ch.unifr.digits.webprotege.attestation.shared.GetAttestationSettingsActionResult;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class AttestationSettingsActionHandler implements ApplicationActionHandler<GetAttestationSettingsAction, GetAttestationSettingsActionResult> {

    @Inject
    public AttestationSettingsActionHandler() {
    }

    /**
     * Gets the class of {@link Action} handled by this handler.
     *
     * @return The class of {@link Action}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public Class<GetAttestationSettingsAction> getActionClass() {
        return GetAttestationSettingsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetAttestationSettingsAction action, @Nonnull RequestContext requestContext) {
        return new NullValidator();
    }

    @Nonnull
    @Override
    public GetAttestationSettingsActionResult execute(@Nonnull GetAttestationSettingsAction action, @Nonnull ExecutionContext executionContext) {
        return new GetAttestationSettingsActionResult(SettingsManager.ADDRESS_FILE, SettingsManager.ADDRESS_ONTOLOGY);
    }
}
