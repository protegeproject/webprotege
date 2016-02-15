package edu.stanford.bmir.protege.web.client.place;

import com.google.common.collect.Lists;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.Iterator;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/05/2014
 */
public class ItemTokenizer {

    public static final char DEFAULT_SEPARATOR = ',';

    private static final String REGEX = "^([^\\(]+)\\((.+)\\)$";

    private static final int TYPE_GROUP = 1;

    private static final int CONTENT_GROUP = 2;


    private final char separator;

    public ItemTokenizer() {
        this(DEFAULT_SEPARATOR);
    }

    public ItemTokenizer(char separator) {
        this.separator = separator;
    }

    public String renderTokens(List<ItemToken> tokens) {
        StringBuilder sb = new StringBuilder();
        for(Iterator<ItemToken> it = tokens.iterator(); it.hasNext(); ) {
            ItemToken token = it.next();
            sb.append(token.getTypeName());
            sb.append("(");
            sb.append(token.getItemContent());
            sb.append(")");
            if(it.hasNext()) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public List<ItemToken> parseTokens(String buffer) {
        List<ItemToken> result = Lists.newArrayList();
        RegExp regExp = RegExp.compile(REGEX);
        List<String> tokens = getSeparatedTokens(buffer);
        for(String token : tokens) {
            MatchResult matchResult = regExp.exec(token);
            if(matchResult != null) {
                String type = matchResult.getGroup(TYPE_GROUP);
                String content = matchResult.getGroup(CONTENT_GROUP);
                result.add(new ItemToken(type, content));
            }
        }
        return result;
    }


    private List<String> getSeparatedTokens(String buffer) {
        int bracketCount = 0;
        int index = 0;
        List<String> result = Lists.newArrayList();
        for(int i = 0; i < buffer.length(); i++) {
            char ch = buffer.charAt(i);
            if(ch == '(') {
                bracketCount++;
            }
            else if(ch == ')') {
                bracketCount--;
            }
            else if(bracketCount == 0 && ch == separator) {
                result.add(buffer.substring(index, i));
                index = i + 1;
            }
        }
        result.add(buffer.substring(index));
        return result;
    }

}
