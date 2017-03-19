package edu.stanford.bmir.protege.web.client.admin;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
enum SchemeValue {


    HTTPS(443),
    HTTP(80);


    private int defaultPort;

    SchemeValue(int defaultPort) {
        this.defaultPort = defaultPort;
    }

    public int getDefaultPort() {
        return defaultPort;
    }
}
