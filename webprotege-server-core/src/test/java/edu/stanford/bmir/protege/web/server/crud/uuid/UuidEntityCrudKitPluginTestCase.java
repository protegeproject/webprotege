package edu.stanford.bmir.protege.web.server.crud.uuid;

import edu.stanford.bmir.protege.web.server.crud.EntityIriPrefixResolver;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixKit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@RunWith(MockitoJUnitRunner.class)
public class UuidEntityCrudKitPluginTestCase {


    private UuidEntityCrudKitPlugin plugin;

    @Mock
    private UuidSuffixKit suffixKit;

    private UuidEntityCrudKitHandlerFactory handlerFactory;

    @Mock
    private EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureIndex;

    @Mock
    private EntityIriPrefixResolver entityIriPrefixResolver;

    @Before
    public void setUp() {
        handlerFactory = new UuidEntityCrudKitHandlerFactory(OWLDataFactoryImpl::new,
                                                             () -> entitiesInProjectSignatureIndex,
                                                             () -> entityIriPrefixResolver);
        plugin = new UuidEntityCrudKitPlugin(suffixKit, handlerFactory);
    }

    @Test
    public void shouldGetEntityCrudKit() {
        var crudKit = plugin.getEntityCrudKit();
        assertThat(crudKit, is(suffixKit));
    }

    @Test
    public void shouldGetEntityCrudKitHandler() {
        var crudKitHandler = plugin.getEntityCrudKitHandler();
        assertThat(crudKitHandler, is(not(nullValue())));
    }

    @Test
    public void shouldGetDefaultSettings() {
        var defaultSettings = plugin.getDefaultSettings();
        assertThat(defaultSettings, is(not(nullValue())));
    }
}
