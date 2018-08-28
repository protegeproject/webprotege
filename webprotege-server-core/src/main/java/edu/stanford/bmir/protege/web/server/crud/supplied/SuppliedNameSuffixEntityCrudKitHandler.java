package edu.stanford.bmir.protege.web.server.crud.supplied;

import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.*;
import edu.stanford.bmir.protege.web.server.shortform.LocalNameExtractor;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class SuppliedNameSuffixEntityCrudKitHandler implements EntityCrudKitHandler<SuppliedNameSuffixSettings,
        ChangeSetEntityCrudSession> {

    private SuppliedNameSuffixSettings suffixSettings;

    private EntityCrudKitPrefixSettings prefixSettings;

    public SuppliedNameSuffixEntityCrudKitHandler() {
        this(new EntityCrudKitPrefixSettings(), new SuppliedNameSuffixSettings());
    }

    public SuppliedNameSuffixEntityCrudKitHandler(
            EntityCrudKitPrefixSettings prefixSettings,
            SuppliedNameSuffixSettings settings) {
        this.prefixSettings = prefixSettings;
        this.suffixSettings = settings;
    }

    @Override
    public EntityCrudKitPrefixSettings getPrefixSettings() {
        return prefixSettings;
    }

    @Override
    public EntityCrudKitId getKitId() {
        return SuppliedNameSuffixKit.getId();
    }

    @Override
    public SuppliedNameSuffixSettings getSuffixSettings() {
        return suffixSettings;
    }

    @Override
    public EntityCrudKitSettings<SuppliedNameSuffixSettings> getSettings() {
        return new EntityCrudKitSettings<>(prefixSettings, suffixSettings);
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
            @Nonnull Optional<String> langTag, @Nonnull EntityCrudContext context,
            @Nonnull OntologyChangeList.Builder<E> changeListBuilder) {

        // The supplied name can either be a fully quoted IRI, a prefixed name or some other string
        final OWLDataFactory dataFactory = context.getDataFactory();
        final IRI iri;
        final String suppliedName = shortForm.getShortForm();
        final String label;
        Optional<IRI> parsedIRI = new IRIParser().parseIRI(suppliedName);
        if (parsedIRI.isPresent()) {
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
            PrefixedNameExpander prefixedNameExpander = context.getPrefixedNameExpander();
            Optional<IRI> expandedPrefixName = prefixedNameExpander.getExpandedPrefixName(shortForm.getShortForm());
            if (expandedPrefixName.isPresent()) {
                iri = expandedPrefixName.get();
                // The label is the local name
                int sepIndex = suppliedName.indexOf(":");
                if (sepIndex + 1 < suppliedName.length()) {
                    label = suppliedName.substring(sepIndex + 1);
                }
                else {
                    label = suppliedName;
                }
            }
            else {
                // Suffix of IRI
                iri = createEntityIRI(shortForm);
                // Label is supplied name
                label = suppliedName;
            }
        }
        E entity = dataFactory.getOWLEntity(entityType, iri);
        OWLDeclarationAxiom ax = dataFactory.getOWLDeclarationAxiom(entity);
        changeListBuilder.addAxiom(context.getTargetOntology(), ax);
        changeListBuilder.addAxiom(context.getTargetOntology(),
                                   dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getRDFSLabel(),
                                                                              iri,
                                                                              dataFactory.getOWLLiteral(label,
                                                                                                        langTag.orElse(context.getDictionaryLanguage().getLang()))));
        return entity;
    }

    private IRI createEntityIRI(EntityShortForm shortForm) {
        try {
            WhiteSpaceTreatment whiteSpaceTreatment = suffixSettings.getWhiteSpaceTreatment();
            String transformedShortForm = whiteSpaceTreatment.transform(shortForm.getShortForm());
            String escapedShortForm = URLEncoder.encode(transformedShortForm, "UTF-8");
            return IRI.create(prefixSettings.getIRIPrefix() + escapedShortForm);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is not supported!");
        }
    }
}
