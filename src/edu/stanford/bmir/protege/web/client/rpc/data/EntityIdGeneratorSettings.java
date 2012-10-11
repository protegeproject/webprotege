package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class EntityIdGeneratorSettings implements Serializable {

    public static final String DEFAULT_BASE = "http://protege.stanford.edu/ontologies";

    public static final int DEFAULT_ID_LENGTH = 7;

    private String base;
    
    private String idPrefix;
    
    private int idLength;

    private Map<UserId, EntityIdUserRange> rangeMap = new HashMap<UserId, EntityIdUserRange>();

    private EntityIdGeneratorSettings() {
    }

    public EntityIdGeneratorSettings(String base, String idPrefix, int idLength, Collection<EntityIdUserRange> ranges) {
        this.base = base;
        this.idPrefix = idPrefix;
        this.idLength = idLength;
        for(EntityIdUserRange range : ranges) {
            rangeMap.put(range.getUserId(), range);
        }
    }
    
    public static EntityIdGeneratorSettings createBlankSettings(ProjectId projectId) {
        List<EntityIdUserRange> ranges = Collections.<EntityIdUserRange>emptyList();
        String projectBase = DEFAULT_BASE + "/" + projectId.getSuggestedURLPathElementName();
        return new EntityIdGeneratorSettings(projectBase, projectId.getSuggestedAcronym(), DEFAULT_ID_LENGTH, ranges);
    }

    public boolean containsSettingForUserId(UserId userId) {
        return rangeMap.containsKey(userId);
    }

    public String getBase() {
        return base;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public int getIdLength() {
        return idLength;
    }

    public Collection<EntityIdUserRange> getUserRanges() {
        ArrayList<EntityIdUserRange> result = new ArrayList<EntityIdUserRange>(rangeMap.values());
        Collections.sort(result);
        return result;
    }

    public EntityIdGeneratorSettings ensureRangeForUser(UserId userId) {
        if(rangeMap.containsKey(userId)) {
            return this;
        }
        else {
            Set<EntityIdUserRange> newRanges = new HashSet<EntityIdUserRange>(rangeMap.values());
            newRanges.add(new EntityIdUserRange(userId));
            return new EntityIdGeneratorSettings(base, idPrefix, idLength, newRanges);
        }
    }
}
