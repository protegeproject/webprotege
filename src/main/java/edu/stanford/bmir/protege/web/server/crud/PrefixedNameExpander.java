package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.Namespaces;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 16/04/2014
 */
public class PrefixedNameExpander {

    private ImmutableMap<String, String> prefixName2PrefixMap;

    private PrefixedNameExpander(ImmutableMap<String, String> prefixName2PrefixMap) {
        this.prefixName2PrefixMap = checkNotNull(prefixName2PrefixMap);
    }

    public Optional<IRI> getExpandedPrefixName(String suppliedName) {
        checkNotNull(suppliedName);
        int prefixNameSeparatorIndex = suppliedName.indexOf(':');
        if(prefixNameSeparatorIndex == -1) {
            return Optional.absent();
        }
        String suppliedPrefixName = suppliedName.substring(0, prefixNameSeparatorIndex + 1);
        if(suppliedPrefixName.isEmpty()) {
            return Optional.absent();
        }
        if(prefixNameSeparatorIndex + 1 >= suppliedName.length()) {
            return Optional.absent();
        }
        String prefix = prefixName2PrefixMap.get(suppliedPrefixName);
        if(prefix == null) {
            return Optional.absent();
        }
        String suffix = suppliedName.substring(suppliedPrefixName.length());
        String fullIRI = prefix + suffix;
        return Optional.of(IRI.create(fullIRI));
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Map<String, String> prefixName2PrefixMap = Maps.newHashMap();

        public Builder() {
        }

        public PrefixedNameExpander build() {
            return new PrefixedNameExpander(ImmutableMap.copyOf(prefixName2PrefixMap));
        }

        public Builder withPrefixNamePrefix(String prefixName, String prefix) {
            checkNotNull(prefixName);
            checkNotNull(prefix);
            checkPrefixNameEndsWithColon(prefixName);
            checkPrefixNameContainsExactlyOneColon(prefixName, prefix);
            prefixName2PrefixMap.put(prefixName, prefix);
            return this;
        }

        public Builder withNamespaces(Namespaces [] namespaces) {
            checkNotNull(namespaces);
            for(Namespaces ns : namespaces) {
                withPrefixNamePrefix(ns.getPrefixName() + ":", ns.getPrefixIRI());
            }
            return this;
        }

        private void checkPrefixNameContainsExactlyOneColon(String prefixName, String prefix) {
            checkArgument(prefixName.indexOf(":") == prefixName.length() - 1, "prefixName must only contain one colon");
        }

        private void checkPrefixNameEndsWithColon(String prefixName) {
            checkArgument(prefixName.endsWith(":"), "prefixName must end with colon");
        }

    }

}
