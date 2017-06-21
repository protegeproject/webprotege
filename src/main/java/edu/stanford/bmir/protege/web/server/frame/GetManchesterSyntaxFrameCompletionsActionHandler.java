package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionChoice;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionResult;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.shortform.EscapingShortFormProvider;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameCompletionsAction;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameCompletionsResult;
import edu.stanford.bmir.protege.web.shared.renderer.ManchesterSyntaxKeywords;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchResult;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatcher;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;


/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public class GetManchesterSyntaxFrameCompletionsActionHandler
        extends AbstractHasProjectActionHandler<GetManchesterSyntaxFrameCompletionsAction, GetManchesterSyntaxFrameCompletionsResult> {

    private final ManchesterSyntaxKeywords syntaxStyles = new ManchesterSyntaxKeywords();

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final WebProtegeOntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final Provider<ManchesterSyntaxFrameParser> manchesterSyntaxFrameParserProvider;

    @Inject
    public GetManchesterSyntaxFrameCompletionsActionHandler(@Nonnull AccessManager accessManager,
                                                            @Nonnull RenderingManager renderingManager,
                                                            @Nonnull WebProtegeOntologyIRIShortFormProvider ontologyIRIShortFormProvider,
                                                            @Nonnull @RootOntology OWLOntology rootOntology,
                                                            @Nonnull Provider<ManchesterSyntaxFrameParser> manchesterSyntaxFrameParserProvider) {
        super(accessManager);
        this.renderingManager = renderingManager;
        this.ontologyIRIShortFormProvider = ontologyIRIShortFormProvider;
        this.rootOntology = rootOntology;
        this.manchesterSyntaxFrameParserProvider = manchesterSyntaxFrameParserProvider;
    }

    @Override
    public GetManchesterSyntaxFrameCompletionsResult execute(GetManchesterSyntaxFrameCompletionsAction action, ExecutionContext executionContext) {
        String syntax = action.getSyntax();
        int from = action.getFrom();
        String triggerText = syntax.substring(0, from) + "\u0000";
        ManchesterSyntaxFrameParser parser = manchesterSyntaxFrameParserProvider.get();
        try {
            parser.parse(triggerText, action);
        } catch (ParserException e) {
//            ManchesterOWLSyntaxTokenizer tokenizer = new ManchesterOWLSyntaxTokenizer(syntax);
//            List<ManchesterOWLSyntaxTokenizer.Token> tokens = tokenizer.tokenize();
//            ManchesterOWLSyntaxTokenizer.Token intersectingToken;
//            for(ManchesterOWLSyntaxTokenizer.Token token : tokens) {
//                int tokenPos = token.getPos();
//                if(tokenPos <= from && from <= tokenPos + token.getToken().length()) {
//                    intersectingToken = token;
//                    break;
//                }
//            }
            int lastWordStartIndex = getLastWordIndex(syntax, from);
            int lastWordEndIndex = getWordEnd(syntax, from);

            int fromLineNumber = action.getFromPos().getLineNumber();
            int fromColumnNumber = (action.getFromPos().getColumnNumber() - (from - lastWordStartIndex));
            EditorPosition fromPos = new EditorPosition(fromLineNumber, fromColumnNumber);
            EditorPosition toPos = new EditorPosition(fromLineNumber, action.getFromPos().getColumnNumber() + (lastWordEndIndex - from));
            String lastWordPrefix = syntax.substring(lastWordStartIndex, from).toLowerCase();
            List<AutoCompletionChoice> choices = Lists.newArrayList();

            List<AutoCompletionChoice> entityChoices = getEntityAutocompletionChoices(action, e, fromPos, toPos, lastWordPrefix);
            choices.addAll(entityChoices);

            List<AutoCompletionChoice> expectedKeywordChoices = getKeywordAutoCompletionChoices(e, fromPos, toPos, lastWordPrefix);
            choices.addAll(expectedKeywordChoices);

            List<AutoCompletionChoice> ontologyNameChoices = getNameOntologyAutocompletionChoices(e, fromPos, toPos, lastWordPrefix);
            choices.addAll(ontologyNameChoices);


            return new GetManchesterSyntaxFrameCompletionsResult(new AutoCompletionResult(choices, fromPos));
        }
        return new GetManchesterSyntaxFrameCompletionsResult(AutoCompletionResult.emptyResult());
    }

    private List<AutoCompletionChoice> getEntityAutocompletionChoices(GetManchesterSyntaxFrameCompletionsAction action, ParserException e, EditorPosition fromPos, EditorPosition toPos, String lastWordPrefix) {
        List<AutoCompletionMatch> matches = Lists.newArrayList();
        Set<EntityType<?>> expectedEntityTypes = Sets.newHashSet(ManchesterSyntaxFrameParser.getExpectedEntityTypes(e));
        if(!expectedEntityTypes.isEmpty()) {
            BidirectionalShortFormProvider shortFormProvider = renderingManager.getShortFormProvider();
            for(String shortForm : shortFormProvider.getShortForms()) {
                EntityNameMatcher entityNameMatcher = new EntityNameMatcher(lastWordPrefix);
                Optional<EntityNameMatchResult> match = entityNameMatcher.findIn(shortForm);
                if(match.isPresent()) {
                    Set<OWLEntity> entities = shortFormProvider.getEntities(shortForm);
                    for(OWLEntity entity : entities) {
                        if(expectedEntityTypes.contains(entity.getEntityType())) {
                            EscapingShortFormProvider escapingShortFormProvider = new EscapingShortFormProvider(shortFormProvider);
                            AutoCompletionChoice choice = new AutoCompletionChoice(escapingShortFormProvider.getShortForm(entity), shortForm, "", fromPos, toPos);
                            AutoCompletionMatch autoCompletionMatch = new AutoCompletionMatch(
                                    match.get(),
                                        choice
                            );
                            matches.add(autoCompletionMatch);
                        }
                    }

                }
            }
        }
        Collections.sort(matches);
        List<AutoCompletionChoice> result = Lists.newArrayList();
        for(AutoCompletionMatch match : matches) {
            result.add(match.getAutoCompletionChoice());
            if(result.size() == action.getEntityTypeSuggestLimit()) {
                break;
            }
        }
        return result;

    }

    private List<AutoCompletionChoice> getNameOntologyAutocompletionChoices(ParserException e, EditorPosition fromPos, EditorPosition toPos, String lastWordPrefix) {
        List<AutoCompletionChoice> choices = Lists.newArrayList();
        if(e.isOntologyNameExpected()) {
            for(OWLOntology ont : rootOntology.getImportsClosure()) {
                String ontologyName = ontologyIRIShortFormProvider.getShortForm(ont);
                if(lastWordPrefix.isEmpty() || ontologyName.toLowerCase().startsWith(lastWordPrefix)) {
                    choices.add(new AutoCompletionChoice(ontologyName, ontologyName, "cm-ontology-list", fromPos, toPos));
                }
            }
        }
        return choices;
    }

    private List<AutoCompletionChoice> getKeywordAutoCompletionChoices(ParserException e, EditorPosition fromPos, EditorPosition toPos, String lastWordPrefix) {
        Set<String> expectedKeywords = e.getExpectedKeywords();
        List<AutoCompletionChoice> expectedKeywordChoices = Lists.newArrayList();
        for(String expectedKeyword : expectedKeywords) {
            if(lastWordPrefix.isEmpty() || expectedKeyword.toLowerCase().contains(lastWordPrefix)) {
                Optional<ManchesterOWLSyntax> kw = syntaxStyles.getKeyword(expectedKeyword);
                String style = "";
                if(kw.isPresent()) {
                    style = syntaxStyles.getStyleName(kw.get());
                }
                expectedKeywordChoices.add(new AutoCompletionChoice(expectedKeyword, expectedKeyword, style, fromPos, toPos));
            }
        }
        Collections.sort(expectedKeywordChoices, new Comparator<AutoCompletionChoice>() {

            private ManchesterSyntaxKeywords.KeywordComparator keywordComparator = new ManchesterSyntaxKeywords.KeywordComparator();

            @Override
            public int compare(AutoCompletionChoice autoCompletionChoice, AutoCompletionChoice autoCompletionChoice2) {
                return keywordComparator.compare(autoCompletionChoice.getDisplayText(), autoCompletionChoice2.getDisplayText());

            }
        });
        return expectedKeywordChoices;
    }

    private int getLastWordIndex(String syntax, int from) {
        for(int i = from - 1; i > -1; i--) {
            char ch = syntax.charAt(i);
            if(isNonWordChar(ch)) {
                return i + 1;
            }
        }
        return -1;
    }

    private int getWordEnd(String syntax, int from) {
        for(int i = from; i < syntax.length(); i++) {
            char ch = syntax.charAt(from);
            if(isNonWordChar(ch)) {
                return from;
            }
        }
        return from;
    }

    private boolean isNonWordChar(char ch) {
        return ch == ' ' || ch == '{' || ch == '}' || ch == '[' || ch == ']' || ch == '(' || ch == ')' || ch == ',' || ch == '\'' || ch == '\t' || ch == '\n' || ch == '\r' || ch == '^';
    }


    @Override
    public Class<GetManchesterSyntaxFrameCompletionsAction> getActionClass() {
        return GetManchesterSyntaxFrameCompletionsAction.class;
    }


    private static class AutoCompletionMatch implements Comparable<AutoCompletionMatch> {

        private EntityNameMatchResult matchResult;

        private AutoCompletionChoice autoCompletionChoice;

        private AutoCompletionMatch(EntityNameMatchResult matchResult, AutoCompletionChoice autoCompletionChoice) {
            this.matchResult = matchResult;
            this.autoCompletionChoice = autoCompletionChoice;
        }

        public EntityNameMatchResult getMatchResult() {
            return matchResult;
        }

        public AutoCompletionChoice getAutoCompletionChoice() {
            return autoCompletionChoice;
        }

        @Override
        public int compareTo(@Nonnull AutoCompletionMatch autoCompletionMatch) {
            int diff = this.matchResult.compareTo(autoCompletionMatch.matchResult);
            if(diff != 0) {
                return diff;
            }
            return autoCompletionChoice.getDisplayText().compareToIgnoreCase(autoCompletionMatch.autoCompletionChoice.getDisplayText());
        }
    }


}
