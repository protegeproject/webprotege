package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.change.FindAndReplaceIRIPrefixChangeGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectAdminPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.crud.IRIPrefixUpdateStrategy;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsResult;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class SetEntityCrudKitSettingsActionHandler extends AbstractHasProjectActionHandler<SetEntityCrudKitSettingsAction, SetEntityCrudKitSettingsResult> {

    @Override
    public Class<SetEntityCrudKitSettingsAction> getActionClass() {
        return SetEntityCrudKitSettingsAction.class;
    }

    @Override
    protected RequestValidator<SetEntityCrudKitSettingsAction> getAdditionalRequestValidator(SetEntityCrudKitSettingsAction action, RequestContext requestContext) {
        return UserHasProjectAdminPermissionValidator.get();
    }

    @Override
    protected SetEntityCrudKitSettingsResult execute(SetEntityCrudKitSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        project.setEntityCrudKitSettings(action.getToSettings());
        if(action.getPrefixUpdateStrategy() == IRIPrefixUpdateStrategy.FIND_AND_REPLACE) {
            String fromPrefix = action.getFromSettings().getPrefixSettings().getIRIPrefix();
            String toPrefix = action.getToSettings().getPrefixSettings().getIRIPrefix();
            FindAndReplaceIRIPrefixChangeGenerator changeGenerator = new FindAndReplaceIRIPrefixChangeGenerator(fromPrefix, toPrefix);
            project.applyChanges(executionContext.getUserId(), changeGenerator, new FixedMessageChangeDescriptionGenerator<Void>("Replaced IRI prefix &lt;" + fromPrefix + "&gt; with &lt;" + toPrefix + "&gt;"));
        }
        return new SetEntityCrudKitSettingsResult();
    }

}
