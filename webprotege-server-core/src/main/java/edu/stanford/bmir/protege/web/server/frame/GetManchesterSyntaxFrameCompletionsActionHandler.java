package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionChoice;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionResult;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.bmir.protege.web.server.renderer.ManchesterSyntaxKeywords;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameCompletionsAction;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameCompletionsResult;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchResult;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatcher;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static edu.stanford.bmir.protege.web.server.shortform.SearchString.parseSearchString;
import static edu.stanford.bmir.protege.web.server.shortform.ShortFormQuotingUtils.getQuotedShortForm;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;


/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public class GetManchesterSyntaxFrameCompletionsActionHandler
        extends AbstractProjectActionHandler<GetManchesterSyntaxFrameCompletionsAction, GetManchesterSyntaxFrameCompletionsResult> {

    private static final int SEARCH_LIMIT = 3000;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ManchesterSyntaxKeywords syntaxStyles = new ManchesterSyntaxKeywords();

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final WebProtegeOntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    @Nonnull
    private final Provider<ManchesterSyntaxFrameParser> manchesterSyntaxFrameParserProvider;

    @Inject
    public GetManchesterSyntaxFrameCompletionsActionHandler(@Nonnull AccessManager accessManager,
                                                            @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                            @Nonnull DictionaryManager dictionaryManager,
                                                            @Nonnull WebProtegeOntologyIRIShortFormProvider ontologyIRIShortFormProvider,
                                                            @Nonnull Provider<ManchesterSyntaxFrameParser> manchesterSyntaxFrameParserProvider) {
        super(accessManager);
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.dictionaryManager = dictionaryManager;
        this.ontologyIRIShortFormProvider = ontologyIRIShortFormProvider;
        this.manchesterSyntaxFrameParserProvider = manchesterSyntaxFrameParserProvider;
    }

    @Nonnull
    @Override
    public GetManchesterSyntaxFrameCompletionsResult execute(@Nonnull GetManchesterSyntaxFrameCompletionsAction action, @Nonnull ExecutionContext executionContext) {
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

    private List<AutoCompletionChoice> getEntityAutocompletionChoices(@Nonnull GetManchesterSyntaxFrameCompletionsAction action, ParserException e,
                                                                      @Nonnull EditorPosition fromPos,
                                                                      @Nonnull EditorPosition toPos,
                                                                      @Nonnull String lastWordPrefix) {
        Set<EntityType<?>> expectedEntityTypes = Sets.newHashSet(ManchesterSyntaxFrameParser.getExpectedEntityTypes(e));
        if(expectedEntityTypes.isEmpty()) {
            return Collections.emptyList();
        }
        EntityNameMatcher entityNameMatcher = new EntityNameMatcher(lastWordPrefix);

        Set<OWLEntity> candidateEntities = new HashSet<>();
        throw new RuntimeException();
//        return dictionaryManager.getShortFormsContaining(singletonList(parseSearchString(lastWordPrefix)),
//                                                         expectedEntityTypes)
//                                // Don't show duplicate entities with different short forms.
//                                .filter(match -> !candidateEntities.contains(match.getEntity()))
//                                .peek(match -> candidateEntities.add(match.getEntity()))
//                                // This is a bit arbitrary - however, the user will need to type more characters
//                                // to find the match they want in any case, because we only display around 20
//                                // choices in the auto completer box.  Note that we will process up to this
//                                // limit as we perform a sort further down in this pipeline.
//                                .limit(SEARCH_LIMIT)
//                                // Map to an AutoCompletionChoice because this allows proper sorting for
//                                // better results
//                                .map(match -> {
//                                    String shortForm = match.getShortForm();
//                                    Optional<EntityNameMatchResult> matchResult = entityNameMatcher.findIn(shortForm);
//                                    return matchResult.map(mr -> {
//                                        String quotedShortForm = getQuotedShortForm(shortForm);
//                                        AutoCompletionChoice choice = new AutoCompletionChoice(quotedShortForm,
//                                                                                               shortForm, "",
//                                                                                               fromPos, toPos);
//                                        return new AutoCompletionMatch(matchResult.get(), choice);
//                                    }).orElse(null);
//                                })
//                                .filter(Objects::nonNull)
//                                .sorted()
//                                .limit(action.getEntityTypeSuggestLimit())
//                                .map(AutoCompletionMatch::getAutoCompletionChoice)
//                                .collect(toList());
    }

    private List<AutoCompletionChoice> getNameOntologyAutocompletionChoices(ParserException e,
                                                                            EditorPosition fromPos,
                                                                            EditorPosition toPos,
                                                                            String lastWordPrefix) {
        if (e.isOntologyNameExpected()) {
            return projectOntologiesIndex.getOntologyIds()
                                  .map(ontologyIRIShortFormProvider::getShortForm)
                                  .filter(shortForm -> lastWordPrefix.isEmpty() || shortForm.toLowerCase().startsWith(lastWordPrefix))
                                  .map(shortForm -> new AutoCompletionChoice(shortForm, shortForm, "cm-ontology-list", fromPos, toPos))
                                  .collect(toList());
        }
        else {
            return Collections.emptyList();
        }
    }

    private List<AutoCompletionChoice> getKeywordAutoCompletionChoices(ParserException e, EditorPosition fromPos, EditorPosition toPos, String lastWordPrefix) {
        Set<String> expectedKeywords = e.getExpectedKeywords();
        List<AutoCompletionChoice> expectedKeywordChoices = Lists.newArrayList();
        for (String expectedKeyword : expectedKeywords) {
            if (lastWordPrefix.isEmpty() || expectedKeyword.toLowerCase().contains(lastWordPrefix)) {
                Optional<ManchesterOWLSyntax> kw = syntaxStyles.getKeyword(expectedKeyword);
                String style = "";
                if (kw.isPresent()) {
                    style = syntaxStyles.getStyleName(kw.get());
                }
                expectedKeywordChoices.add(new AutoCompletionChoice(expectedKeyword, expectedKeyword, style, fromPos, toPos));
            }
        }
        expectedKeywordChoices.sort(new Comparator<>() {

            private ManchesterSyntaxKeywords.KeywordComparator keywordComparator = new ManchesterSyntaxKeywords.KeywordComparator();

            @Override
            public int compare(AutoCompletionChoice autoCompletionChoice, AutoCompletionChoice autoCompletionChoice2) {
                return keywordComparator.compare(autoCompletionChoice.getDisplayText(),
                                                 autoCompletionChoice2.getDisplayText());

            }
        });
        return expectedKeywordChoices;
    }

    private int getLastWordIndex(String syntax, int from) {
        for (int i = from - 1; i > -1; i--) {
            char ch = syntax.charAt(i);
            if (isNonWordChar(ch)) {
                return i + 1;
            }
        }
        return -1;
    }

    private int getWordEnd(String syntax, int from) {
        for (int i = from; i < syntax.length(); i++) {
            char ch = syntax.charAt(from);
            if (isNonWordChar(ch)) {
                return from;
            }
        }
        return from;
    }

    private boolean isNonWordChar(char ch) {
        return ch == ' ' || ch == '{' || ch == '}' || ch == '[' || ch == ']' || ch == '(' || ch == ')' || ch == ',' || ch == '\'' || ch == '\t' || ch == '\n' || ch == '\r' || ch == '^';
    }


    @Nonnull
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
            if (diff != 0) {
                return diff;
            }
            return autoCompletionChoice.getDisplayText().compareToIgnoreCase(autoCompletionMatch.autoCompletionChoice.getDisplayText());
        }
    }


}
