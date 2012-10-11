package edu.stanford.bmir.protege.web.server.rest;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class RestCall<T> {

    private String restServiceBase;
    
    private String restCallName;
    
    private Map<String, Set<String>> paramMap = new HashMap<String, Set<String>>();
    
    private Class<T> responseObjectClass;

    public RestCall(String restServiceBase, String restCallName, Class<T> responseObjectClass) {
        this.restServiceBase = restServiceBase;
        this.restCallName = restCallName;
        this.responseObjectClass = responseObjectClass;
    }
    
    public void addParameterValue(String parameterName, String parameterValue) {
        Set<String> values = paramMap.get(parameterName);
        if(values == null) {
            values = new HashSet<String>();
            paramMap.put(parameterName, values);
        }
        values.add(parameterValue);
    }
    
    
    public String formatCallURL() {
        StringBuilder sb = new StringBuilder();
        sb.append(restServiceBase);
        if(!restServiceBase.endsWith("/")) {
            sb.append("/");
        }
        sb.append(restCallName);
        sb.append("?");
        for(Iterator<String> it = paramMap.keySet().iterator(); it.hasNext(); ) {
            String parameterName = it.next();
            Set<String> values = paramMap.get(parameterName);
            for(Iterator<String> valIt = values.iterator(); valIt.hasNext(); ) {
                String value = valIt.next();
                sb.append(parameterName);
                sb.append("=");
                sb.append(value);
                if(valIt.hasNext()) {
                    sb.append("&");
                }
            }
            if(it.hasNext()) {
                sb.append("&");
            }
        }

        return sb.toString();
    }

    public InputStream doCall() throws IOException {
        URL url = new URL(formatCallURL());
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        InputStream is = connection.getInputStream();
        return is;
    }
    
    public T doCallForObject() throws IOException {
        try {
            XmlRootElement element = responseObjectClass.getAnnotation(XmlRootElement.class);
            if(element == null) {
                throw new RuntimeException("Class does not have an XmlRootElement annotation");
            }
            String elementName = element.name();
            XMLInputFactory inf = XMLInputFactory.newFactory();
            InputStream inputStream = doCall();
            XMLStreamReader reader = inf.createXMLStreamReader(new InputStreamReader(new BufferedInputStream(inputStream)));
            reader.nextTag();
            while(reader.getEventType() != XMLStreamConstants.START_ELEMENT || !reader.getLocalName().equals(elementName)) {
                reader.next();
            }
            JAXBContext context = JAXBContext.newInstance(responseObjectClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return responseObjectClass.cast(unmarshaller.unmarshal(reader));
        }
        catch (XMLStreamException e) {
            throw new IOException(e);
        }
        catch (JAXBException e) {
            throw new IOException(e);
        }
    }
}
