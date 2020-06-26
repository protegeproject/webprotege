package edu.stanford.bmir.protege.web.server.crud.supplied;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.*;
import edu.stanford.bmir.protege.web.server.shortform.LocalNameExtractor;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.net.URLEncoder;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class SuppliedNameSuffixEntityCrudKitHandler implements EntityCrudKitHandler<SuppliedNameSuffixSettings,
        ChangeSetEntityCrudSession> {

    @Nonnull
    private final SuppliedNameSuffixSettings suffixSettings;

    @Nonnull
    private final EntityCrudKitPrefixSettings prefixSettings;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final EntityIriPrefixResolver entityIriPrefixResolver;

    @AutoFactory
    @Inject
    public SuppliedNameSuffixEntityCrudKitHandler(
            @Nonnull EntityCrudKitPrefixSettings prefixSettings,
            @Nonnull SuppliedNameSuffixSettings settings,
            @Provided @Nonnull OWLDataFactory dataFactory,
            @Provided @Nonnull EntityIriPrefixResolver entityIriPrefixResolver) {
        this.prefixSettings = checkNotNull(prefixSettings);
        this.suffixSettings = checkNotNull(settings);
        this.dataFactory = dataFactory;
        this.entityIriPrefixResolver = checkNotNull(entityIriPrefixResolver);
    }

    @Nonnull
    @Override
    public EntityCrudKitPrefixSettings getPrefixSettings() {
        return prefixSettings;
    }

    @Override
    public EntityCrudKitId getKitId() {
        return SuppliedNameSuffixKit.getId();
    }

    @Nonnull
    @Override
    public SuppliedNameSuffixSettings getSuffixSettings() {
        return suffixSettings;
    }

    @Override
    public EntityCrudKitSettings<SuppliedNameSuffixSettings> getSettings() {
        return EntityCrudKitSettings.get(prefixSettings, suffixSettings);
    }

    @Override
    public ChangeSetEntityCrudSession createChangeSetSession() {
        return EmptyChangeSetEntityCrudSession.get();
    }

    @Override
    public <E extends OWLEntity> E create(
            @Nonnull ChangeSetEntityCrudSession session,
            @Nonnull EntityType<E> entityType,
            @Nonnull EntityShortForm shortForm,
            @Nonnull Optional<String> langTag,
            @Nonnull ImmutableList<OWLEntity> parents,
            @Nonnull EntityCrudContext context,
            @Nonnull OntologyChangeList.Builder<E> changeListBuilder) {

        // The supplied name can either be a fully quoted IRI, a prefixed name or some other string
        final IRI iri;
        final String suppliedName = shortForm.getShortForm();
        final String label;
        var parsedIRI = new IRIParser().parseIRI(suppliedName);
        if(parsedIRI.isPresent()) {
            // IRI supplied as a quoted IRI
            iri = parsedIRI.get();
            LocalNameExtractor localNameExtractor = new LocalNameExtractor();
            // The label is the local name, if present, otherwise it's the IRI
            String localName = localNameExtractor.getLocalName(iri);
            if(localName.isEmpty()) {
                label = suppliedName.substring(1, suppliedName.length() - 1);
            }
            else {
                label = localName;
            }
        }
        else {
            // IRI supplied as a prefixed name
            var prefixedNameExpander = context.getPrefixedNameExpander();
            var expandedPrefixName = prefixedNameExpander.getExpandedPrefixName(shortForm.getShortForm());
            if(expandedPrefixName.isPresent()) {
                iri = expandedPrefixName.get();
                // The label is the local name
                int sepIndex = suppliedName.indexOf(":");
                if(sepIndex + 1 < suppliedName.length()) {
                    label = suppliedName.substring(sepIndex + 1);
                }
                else {
                    label = suppliedName;
                }
            }
            else {
                // Suffix of IRI
                iri = createEntityIRI(shortForm, parents);
                // Label is supplied name
                label = suppliedName;
            }
        }
        var targetOntologyId = context.getTargetOntologyId();
        var entity = dataFactory.getOWLEntity(entityType, iri);
        var declarationAxiom = dataFactory.getOWLDeclarationAxiom(entity);
        changeListBuilder.add(AddAxiomChange.of(targetOntologyId, declarationAxiom));
        var labellingAxiom = dataFactory.getOWLAnnotationAssertionAxiom(rdfsLabel(),
                                                                        iri,
                                                                        dataFactory.getOWLLiteral(
                                                                                label,
                                                                                langTag.orElse(
                                                                                        context.getDictionaryLanguage()
                                                                                               .getLang())));
        changeListBuilder.add(AddAxiomChange.of(targetOntologyId, labellingAxiom));
        return entity;
    }

    private IRI createEntityIRI(EntityShortForm shortForm, ImmutableList<OWLEntity> parents) {
        var whiteSpaceTreatment = suffixSettings.getWhiteSpaceTreatment();
        var transformedShortForm = whiteSpaceTreatment.transform(shortForm.getShortForm());
        var escapedShortForm = URLEncoder.encode(transformedShortForm, Charsets.UTF_8);
        var iriPrefix = entityIriPrefixResolver.getIriPrefix(prefixSettings, parents);
        return IRI.create(iriPrefix + escapedShortForm);
    }

    private OWLAnnotationProperty rdfsLabel() {
        return dataFactory.getRDFSLabel();
    }
}
