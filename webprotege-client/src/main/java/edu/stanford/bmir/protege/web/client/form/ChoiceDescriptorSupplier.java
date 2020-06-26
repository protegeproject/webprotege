package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.DynamicChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FixedChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
public class ChoiceDescriptorSupplier {

    interface ChoicesHandler {
        void handleChoices(@Nonnull ImmutableList<ChoiceDescriptor> choices);
    }

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    private ListMultimap<ChoiceListSourceDescriptor, ChoiceDescriptor> choicesCache = ArrayListMultimap.create();

    @Inject
    public ChoiceDescriptorSupplier(@Nonnull ProjectId projectId,
                                    @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    public void getChoices(@Nonnull ChoiceListSourceDescriptor source, ChoicesHandler choicesHandler) {
        if(source instanceof FixedChoiceListSourceDescriptor) {
            choicesHandler.handleChoices(((FixedChoiceListSourceDescriptor) source).getChoices());
        }
        else {
            DynamicChoiceListSourceDescriptor dynamicChoiceListSourceDescriptor = (DynamicChoiceListSourceDescriptor) source;
            getDynamicChoices(dynamicChoiceListSourceDescriptor, choicesHandler);
        }
    }

    private void getDynamicChoices(DynamicChoiceListSourceDescriptor sourceDescriptor,
                                   ChoicesHandler handler) {
        List<ChoiceDescriptor> cachedChoices = choicesCache.get(sourceDescriptor);
        if(!cachedChoices.isEmpty()) {
            handler.handleChoices(ImmutableList.copyOf(cachedChoices));
            return;
        }
        dispatchServiceManager.execute(new GetMatchingEntitiesAction(sourceDescriptor.getCriteria(),
                                                                     projectId,
                                                                     PageRequest.requestPageWithSize(1, 200)),
                                       result -> {
                                           ImmutableList<ChoiceDescriptor> choiceDescriptors = getChoiceDescriptors(result);
                                           choicesCache.putAll(sourceDescriptor, choiceDescriptors);
                                           handler.handleChoices(choiceDescriptors);
                                       });
    }

    private ImmutableList<ChoiceDescriptor> getChoiceDescriptors(GetMatchingEntitiesResult result) {
        return result.getEntities().getPageElements().stream().map(this::toChoiceDescriptor).collect(toImmutableList());
    }

    private ChoiceDescriptor toChoiceDescriptor(EntityNode node) {
        return ChoiceDescriptor.choice(
                getLanguageMap(node),
                PrimitiveFormControlData.get(node.getEntity())
        );
    }

    private LanguageMap getLanguageMap(EntityNode node) {
        Map<String, String> map = new HashMap<>();
        node.getShortForms()
            .forEach((key, value) -> map.put(key.getLang(), value));
        return LanguageMap.get(map);
    }
}
