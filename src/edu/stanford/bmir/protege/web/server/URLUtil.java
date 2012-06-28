package edu.stanford.bmir.protege.web.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;

public class URLUtil {

    public static BufferedReader read(String url) throws Exception {
        return new BufferedReader(
                new InputStreamReader(
                        new URL(url).openStream()));
    }

    public static String getURLContent(String url) {
        StringBuffer urlString = new StringBuffer();

        try {
            BufferedReader reader = read(url);
            String line = reader.readLine();

            while (line != null) {
                urlString.append(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            if (Log.getLogger().isLoggable(Level.FINE)) {
                Log.getLogger().log(Level.FINE, "Could not access: " + url, e);
            } else {
                Log.getLogger().warning("Could not access: " + url + " Error: " + e.getMessage() + ". Enable fine logging for more.");
            }
        }
        return urlString.toString();
    }

    public static void httpPost(String urlStr, String encodedData) {
        try {
            URL url = new URL(urlStr);
            // Send data
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(encodedData);
            wr.flush();

            System.out.println("POST: " + url + " data:" + encodedData);
            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                // Process line...
                System.out.println(line);
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            if (Log.getLogger().isLoggable(Level.FINE)) {
                Log.getLogger().log(Level.FINE, "Could not make the HTTP POST: " + urlStr + " data: " + encodedData, e);
            } else {
                Log.getLogger().warning("Could not make the HTTP POST: " + urlStr + " data: " + encodedData);
            }
            throw new RuntimeException("HTTP POST to: " + urlStr + " failed. Message: " + e.getMessage());
        }
    }

    public static int httpPut(String urlStr) {
        return httpRequest(urlStr, "PUT");
    }

    public static int httpDelete(String urlStr) {
        return httpRequest(urlStr, "DELETE");
    }

    public static int httpRequest(String urlStr, String requestType) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestType);
            return con.getResponseCode();
        } catch (Exception e) {
            if (Log.getLogger().isLoggable(Level.FINE)) {
                Log.getLogger().log(Level.FINE, "Could not make the HTTP PUT: " + urlStr, e);
            } else {
                Log.getLogger().warning("Could not make the HTTP PUT: " + urlStr);
            }
            throw new RuntimeException("HTTP " + requestType +" to: " + urlStr + " failed. Message: " + e.getMessage());
        }
    }

    public static String encode(String urlToEncode) {
        if (urlToEncode == null) { return null; }

        String encodedString = null;
        try {
            encodedString = URLEncoder.encode(urlToEncode, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            Log.getLogger().log(Level.WARNING, "Error at encoding url: " + urlToEncode, e1);
            return "";
        }

        return encodedString;
    }

    /**
     * Encodes the argument. If the argument is null, it will return the empty string.
     *
     * @param urlToEncode
     * @return
     */
    public static String encodeNN(String urlToEncode) {
        return encode(urlToEncode == null ? "" : urlToEncode);
    }


    public static void main (String[] args) throws Exception{
        //BufferedReader reader = read(args[0]);
        String url = "http://rest.bioontology.org/bioportal/search/Thyroiditis";
        System.out.println(getURLContent(url));
    }

}
