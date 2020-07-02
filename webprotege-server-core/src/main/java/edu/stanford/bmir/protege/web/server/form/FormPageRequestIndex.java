package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-22
 */
public class FormPageRequestIndex {

    @Nonnull
    private final ImmutableMap<Key, FormPageRequest> indexMap;

    public FormPageRequestIndex(@Nonnull ImmutableMap<Key, FormPageRequest> indexMap) {
        this.indexMap = checkNotNull(indexMap);
    }

    @Nonnull
    public static FormPageRequestIndex create(@Nonnull ImmutableList<FormPageRequest> pageRequests) {
        checkNotNull(pageRequests);
        Map<Key, FormPageRequest> map = new HashMap<>();
        for(FormPageRequest pageRequest : pageRequests) {
            map.put(Key.get(pageRequest.getSubject(),
                            pageRequest.getFieldId(),
                            pageRequest.getSourceType()),
                    pageRequest);
        }
        return new FormPageRequestIndex(ImmutableMap.copyOf(map));
    }

    @Nonnull
    public PageRequest getPageRequest(FormSubject formSubject, FormRegionId id, FormPageRequest.SourceType sourceType) {
        var formPageRequest = indexMap.get(Key.get(formSubject, id, sourceType));
        if(formPageRequest != null) {
            return formPageRequest.getPageRequest();
        }
        else {
            return PageRequest.requestPageWithSize(1, FormPageRequest.DEFAULT_PAGE_SIZE);
        }
    }

    @AutoValue
    public static abstract class Key {

        public static Key get(@Nonnull FormSubject subject,
                              @Nonnull FormRegionId formRegionId,
                              @Nonnull FormPageRequest.SourceType sourceType) {
            return new AutoValue_FormPageRequestIndex_Key(subject, formRegionId, sourceType);
        }

        @Nonnull
        public abstract FormSubject getFormSubject();

        @Nonnull
        public abstract FormRegionId getFormRegionId();

        @Nonnull
        public abstract FormPageRequest.SourceType getSourceType();
    }


}
