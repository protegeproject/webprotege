package edu.stanford.bmir.protege.web.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class URLUtil {

    public static BufferedReader read(String url) throws Exception {
        return new BufferedReader(
                new InputStreamReader(
                        new URL(url).openStream()));
    }


    public static String encode(String urlToEncode) {
        if (urlToEncode == null) { return null; }

        String encodedString = null;
        try {
            encodedString = URLEncoder.encode(urlToEncode, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            return "";
        }

        return encodedString;
    }

}
