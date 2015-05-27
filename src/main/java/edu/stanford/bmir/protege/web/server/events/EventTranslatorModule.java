package edu.stanford.bmir.protege.web.server.events;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/05/15
 */
public class EventTranslatorModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<EventTranslator> multibinder = Multibinder.newSetBinder(binder(), EventTranslator.class);
        multibinder.addBinding().to(BrowserTextChangedEventComputer.class);
        multibinder.addBinding().to(HighLevelEventGenerator.class);
        multibinder.addBinding().to(OWLClassHierarchyChangeComputer.class);
        multibinder.addBinding().to(OWLObjectPropertyHierarchyChangeComputer.class);
        multibinder.addBinding().to(OWLDataPropertyHierarchyChangeComputer.class);
        multibinder.addBinding().to(OWLAnnotationPropertyHierarchyChangeComputer.class);
        multibinder.addBinding().to(EntityDeprecatedChangedEventTranslator.class);
    }
}
