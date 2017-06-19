package edu.stanford.bmir.protege.web.shared.renderer;

import com.google.common.collect.Maps;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 *
 * This class is thread safe
 */
public class ManchesterSyntaxKeywords {

    private static final String FRAME_KEYWORD_STYLE = "cm-frame-kw";

    private static final String SECTION_KEYWORD_STYLE = "cm-section-kw";

    private static final String QUANTIFIER_KEYWORD_STYLE = "cm-quantifier-kw";

    private static final String CONNECTIVE_KEYWORD_STYLE = "cm-connective-kw";

    private static final String AXIOM_KEYWORD_STYLE = "cm-axiom-kw";

    private Map<String, ManchesterOWLSyntax> keywordMap = Maps.newHashMap();

    private Map<String, String> rawKeyword2StyleNameMap = Maps.newHashMap();

    private Map<ManchesterOWLSyntax, String> keywordStyleMap = Maps.newHashMap();

    public ManchesterSyntaxKeywords() {
        for(ManchesterOWLSyntax syntax : ManchesterOWLSyntax.values()) {
            keywordMap.put(syntax.keyword(), syntax);
            if(syntax.isFrameKeyword()) {
                keywordStyleMap.put(syntax, FRAME_KEYWORD_STYLE);
                rawKeyword2StyleNameMap.put(syntax.keyword(), FRAME_KEYWORD_STYLE);
                rawKeyword2StyleNameMap.put(syntax.keyword() + ":", FRAME_KEYWORD_STYLE);
                keywordMap.put(syntax.keyword() + ":", syntax);
            }
            else if(syntax.isSectionKeyword()) {
                keywordStyleMap.put(syntax, SECTION_KEYWORD_STYLE);
                rawKeyword2StyleNameMap.put(syntax.keyword(), SECTION_KEYWORD_STYLE);
                rawKeyword2StyleNameMap.put(syntax.keyword() + ":", SECTION_KEYWORD_STYLE);
                keywordMap.put(syntax.keyword() + ":", syntax);
            }
            else if(syntax.isClassExpressionQuantiferKeyword()) {
                keywordStyleMap.put(syntax, QUANTIFIER_KEYWORD_STYLE);
                rawKeyword2StyleNameMap.put(syntax.keyword(), QUANTIFIER_KEYWORD_STYLE);
            }
            else if(syntax.isClassExpressionConnectiveKeyword()) {
                keywordStyleMap.put(syntax, CONNECTIVE_KEYWORD_STYLE);
                rawKeyword2StyleNameMap.put(syntax.keyword(), CONNECTIVE_KEYWORD_STYLE);
            }
            else if(syntax.isAxiomKeyword()) {
                keywordStyleMap.put(syntax, AXIOM_KEYWORD_STYLE);
                rawKeyword2StyleNameMap.put(syntax.keyword(), AXIOM_KEYWORD_STYLE);
            }
        }
    }

    public Optional<ManchesterOWLSyntax> getKeyword(String manchesterSyntaxKeyword) {
        ManchesterOWLSyntax keyword = keywordMap.get(manchesterSyntaxKeyword);
        if(keyword == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(keyword);
        }
    }

    public String getStyleName(String manchesterSyntaxKeyword) {
        String result = rawKeyword2StyleNameMap.get(manchesterSyntaxKeyword);
        if(result != null) {
            return result;
        }
        else {
            return "";
        }
    }

    public String getStyleName(ManchesterOWLSyntax keyword) {
        String result = keywordStyleMap.get(keyword);
        if(result != null) {
            return result;
        }
        else {
            return "";
        }
    }

    public static class KeywordComparator implements Comparator<String> {

        private ManchesterSyntaxKeywords keywords = new ManchesterSyntaxKeywords();

        private ManchesterSyntaxKeywordComparator comparator = new ManchesterSyntaxKeywordComparator();

        @Override
        public int compare(String s, String s2) {
            Optional<ManchesterOWLSyntax> kw = keywords.getKeyword(s);
            Optional<ManchesterOWLSyntax> kw2 = keywords.getKeyword(s2);
            if(kw.isPresent()) {
                if(kw2.isPresent()) {
                    return comparator.compare(kw.get(), kw2.get());
                }
                else {
                    return 1;
                }
            }
            else {
                if(kw2.isPresent()) {
                    return -1;
                }
                else {
                    return s.compareToIgnoreCase(s2);
                }
            }
        }
    }


    public static class ManchesterSyntaxKeywordComparator implements Comparator<ManchesterOWLSyntax> {

        private static final EnumMap<ManchesterOWLSyntax, Integer> keywordOrdinalMap;

        static {
            Map<ManchesterOWLSyntax, Integer> map = Maps.newHashMap();

            add(ManchesterOWLSyntax.AND, map);
            add(ManchesterOWLSyntax.OR, map);
            add(ManchesterOWLSyntax.NOT, map);

            add(ManchesterOWLSyntax.SOME, map);
            add(ManchesterOWLSyntax.VALUE, map);
            add(ManchesterOWLSyntax.ONLY, map);
            add(ManchesterOWLSyntax.MIN, map);
            add(ManchesterOWLSyntax.MAX, map);
            add(ManchesterOWLSyntax.EXACTLY, map);

            add(ManchesterOWLSyntax.SUBCLASS_OF, map);
            add(ManchesterOWLSyntax.EQUIVALENT_TO, map);
            add(ManchesterOWLSyntax.DISJOINT_WITH, map);
            add(ManchesterOWLSyntax.INDIVIDUALS, map);
            add(ManchesterOWLSyntax.DISJOINT_UNION_OF, map);

            add(ManchesterOWLSyntax.SUB_PROPERTY_OF, map);
            add(ManchesterOWLSyntax.DOMAIN, map);
            add(ManchesterOWLSyntax.RANGE, map);
            add(ManchesterOWLSyntax.CHARACTERISTICS, map);
            add(ManchesterOWLSyntax.INVERSE_OF, map);
            add(ManchesterOWLSyntax.EQUIVALENT_TO, map);

            add(ManchesterOWLSyntax.TYPES, map);
            add(ManchesterOWLSyntax.FACTS, map);
            add(ManchesterOWLSyntax.SAME_AS, map);
            add(ManchesterOWLSyntax.DIFFERENT_FROM, map);

            for(ManchesterOWLSyntax keyword : ManchesterOWLSyntax.values()) {
                if(!map.containsKey(keyword)) {
                    add(keyword, map);
                }
            }

            keywordOrdinalMap = new EnumMap<ManchesterOWLSyntax, Integer>(map);

        }

        private static void add(ManchesterOWLSyntax keyword, Map<ManchesterOWLSyntax, Integer> map) {
            int ordinal = map.size();
            map.put(keyword, ordinal);
        }

        @Override
        public int compare(ManchesterOWLSyntax manchesterOWLSyntax, ManchesterOWLSyntax manchesterOWLSyntax2) {
            return keywordOrdinalMap.get(manchesterOWLSyntax) - keywordOrdinalMap.get(manchesterOWLSyntax2);
        }
    }
}
