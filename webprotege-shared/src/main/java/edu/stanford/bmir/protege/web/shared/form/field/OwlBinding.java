package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRelationshipValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-24
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = OwlPropertyBinding.class, name = OwlPropertyBinding.TYPE),
                      @JsonSubTypes.Type(value = OwlClassBinding.class, name = OwlClassBinding.TYPE),
                      @JsonSubTypes.Type(value = OwlInstanceBinding.class, name = OwlInstanceBinding.TYPE),
                      @JsonSubTypes.Type(value = OwlSubClassBinding.class, name = OwlSubClassBinding.TYPE)
              })
public interface OwlBinding extends IsSerializable {

    String VALUES_CRITERIA = "valuesCriteria";

    @JsonIgnore
    @Nonnull
    Optional<OWLProperty> getOwlProperty();
}
