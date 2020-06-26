package edu.stanford.bmir.protege.web.shared.filter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public class FilterSet {

    private Map<FilterId, FilterSetting> map = new HashMap<>();

    public FilterSet(Collection<Filter> filters) {
        for(Filter filter : filters) {
            map.put(filter.getFilterId(), filter.getFilterSetting());
        }
    }

    public boolean containsFilterSetting(FilterSetting filterSetting) {
        return map.containsValue(filterSetting);
    }

    public FilterSetting getFilterSetting(FilterId filterId, FilterSetting defaultValue) {
        FilterSetting result = map.get(filterId);
        if(result == null) {
            return defaultValue;
        }
        else {
            return result;
        }
    }

    public boolean hasSetting(FilterId filterId, FilterSetting value) {
        return value.equals(map.get(filterId));
    }

    public ImmutableList<FilterId> getOnFilters() {
        return map.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(FilterSetting.ON))
                .map(Map.Entry::getKey)
                .collect(toImmutableList());
    }
}
