package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public enum EntityLookupRequestEntityMatchType implements Serializable {

    MATCH_CLASSES,

    MATCH_OBJECT_PROPERTIES,

    MATCH_DATA_PROPERTIES,

    MATCH_ANNOTATION_PROPERTIES,

    MATCH_NAMED_INDIVIDUALS,

    MATCH_DATATYPES;
}
