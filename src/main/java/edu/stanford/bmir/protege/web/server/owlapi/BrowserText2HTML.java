package edu.stanford.bmir.protege.web.server.owlapi;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2012
 */
public class BrowserText2HTML {
    
    private static Map<String, Color> KEYWORD_COLOR_MAP;

    public static final Color RESTRICTION_COLOR = new Color(178, 0, 178);

    public static final Color BOOLEAN_OPERAND_COLOR = new Color(0, 178, 178);

    public static final Color AXIOM_KEYWORD_COLOR = new Color(10, 94, 168);

    public static final Color SECTION_KEYWORD_COLOR = new Color(114, 137, 176);

    public static final Color HIGHLIGHT_COLOR = new Color(255, 215, 0);

    static {
        Map<String, Color> keywordColorMap = new HashMap<String, Color>();
        for (ManchesterOWLSyntax keyword : ManchesterOWLSyntax.values()){
            if (keyword.isAxiomKeyword()){
                keywordColorMap.put(keyword.toString(), AXIOM_KEYWORD_COLOR);
                keywordColorMap.put(keyword.toString() + ":", AXIOM_KEYWORD_COLOR);
            }
            else if (keyword.isClassExpressionConnectiveKeyword()){
                keywordColorMap.put(keyword.toString(), BOOLEAN_OPERAND_COLOR);
            }
            else if (keyword.isClassExpressionQuantiferKeyword()){
                keywordColorMap.put(keyword.toString(), RESTRICTION_COLOR);
            }
            else if (keyword.isSectionKeyword()){
                keywordColorMap.put(keyword.toString(), SECTION_KEYWORD_COLOR);
                keywordColorMap.put(keyword.toString() + ":", SECTION_KEYWORD_COLOR);
            }
        }
        KEYWORD_COLOR_MAP = Collections.unmodifiableMap(keywordColorMap);
    }


    private Pattern highlightedPhraseRegExp;


    public BrowserText2HTML() {
    }

    public BrowserText2HTML(Set<String> highlightedPhrases) {
        StringBuilder patternBuilder = new StringBuilder();
        for(Iterator<String> it = highlightedPhrases.iterator(); it.hasNext(); ) {
            patternBuilder.append("\\b");
            final String phrase = it.next();
            patternBuilder.append(Pattern.quote(phrase));
            patternBuilder.append("\\b");
            if(it.hasNext()) {
                patternBuilder.append("|");
            }
        }
        highlightedPhraseRegExp = Pattern.compile(patternBuilder.toString());
    }

    public String getHTML(String browserText) {
        if(highlightedPhraseRegExp != null) {
            Matcher matcher = highlightedPhraseRegExp.matcher(browserText);
            browserText = matcher.replaceAll("<span style=\"background: #F0C800;\">$0</span>");
        }
        StringBuilder htmlBuilder = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(browserText, " '()[]{}.,", true);
        boolean inName = false;
        while(tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            if("'".equals(token)) {
                inName = !inName;
            }

            Color color = KEYWORD_COLOR_MAP.get(token);
            if(color != null && !inName) {
                String hexValue = Integer.toHexString(color.getRGB());
                htmlBuilder.append("<span style=\"");
                htmlBuilder.append("color: #");
                htmlBuilder.append(hexValue.substring(2));
                htmlBuilder.append("; font-weight: bold;");
                htmlBuilder.append("\">");
                htmlBuilder.append(token);
                htmlBuilder.append("</span>");
            }
            else {
                htmlBuilder.append(token);
            }
        }
        return htmlBuilder.toString();
    }
    
}
