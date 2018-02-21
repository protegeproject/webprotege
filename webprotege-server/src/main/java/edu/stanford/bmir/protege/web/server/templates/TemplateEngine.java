package edu.stanford.bmir.protege.web.server.templates;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
public class TemplateEngine {

    private final Provider<MustacheFactory> mustacheFactoryProvider;

    @Inject
    public TemplateEngine(@Nonnull Provider<MustacheFactory> mustacheFactory) {
        this.mustacheFactoryProvider = checkNotNull(mustacheFactory);
    }

    public String populateTemplate(@Nonnull String template,
                                   @Nonnull Map<String, Object> objects) {
        StringWriter templateWriter = new StringWriter();
        StringReader templateReader = new StringReader(template);
        Mustache mustache = mustacheFactoryProvider.get().compile(templateReader, "Template");
        mustache.execute(templateWriter, objects);
        templateWriter.flush();
        return templateWriter.toString();
    }
}
