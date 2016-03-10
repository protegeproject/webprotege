package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.base.CaseFormat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/16
 */
public class UserIdInitialsExtractor {

    public static String getInitials(UserId userId) {
        String result;
        String userName = getCleanUserName(userId);
        String [] initials = userName.split("\\s|\\.|\\_|\\-");
        if(initials.length == 1) {
            result = getCharacter(initials[0]);
        }
        else {
            result = getCharacter(initials[0]) + getCharacter(initials[initials.length - 1]);
        }
        return result.toUpperCase();
    }

    private static String getCleanUserName(UserId userId) {
        String withoutQuotes = userId.getUserName().replaceAll("\"", "");
        String withoutAtSymbol = extractFromAtSymbol(withoutQuotes);
        return getConvertedUserName(withoutAtSymbol);
    }

    private static String extractFromAtSymbol(String userName) {
        int atIndex = userName.indexOf('@');
        // Check it's not like a twitter @matthew type thing
        if(atIndex == 0) {
            return userName.substring(1);
        }
        // Email address
        if(atIndex > 0) {
            return userName.substring(0, atIndex);
        }
        return userName;
    }


    private static String getConvertedUserName(String userName) {
        String convertedName = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(userName);
        if(convertedName == null) {
            return userName;
        }
        else {
            return convertedName;
        }
    }

    private static String getCharacter(String s) {
        for(int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if(Character.isLetter(ch)) {
                return String.valueOf(ch);
            }
        }
        for(int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if(Character.isDigit(ch)) {
                return String.valueOf(ch);
            }
        }
        return "";
    }

}
