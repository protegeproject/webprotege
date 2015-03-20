package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetUIConfigurationResult implements Result {

    private ProjectLayoutConfiguration configuration;

    /**
     * For serialization purposes only
     */
    private GetUIConfigurationResult() {
    }

    public GetUIConfigurationResult(ProjectLayoutConfiguration configuration) {
        this.configuration = checkNotNull(configuration);
    }

    /**
     * Gets the project configuration.
     * @return The project configuration.  Not {@code null}.
     */
    public ProjectLayoutConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(configuration);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetUIConfigurationResult)) {
            return false;
        }
        GetUIConfigurationResult other = (GetUIConfigurationResult) obj;
        return this.configuration.equals(other.configuration);
    }


    @Override
    public String toString() {
        return toStringHelper("GetUIConfigurationResult")
                .addValue(configuration)
                .toString();
    }
}
