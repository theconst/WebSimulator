/*
 * GenericAssignmentSerializer.java
 */
package ua.kpi.atep.services.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import ua.kpi.atep.model.entity.Assignment;
import ua.kpi.atep.services.serialization.assignment.AssignmentHandler;

/**
 * Creates assignment from XML
 *
 * Method are considered to be called once in a bluemoon, so no 
 * attempts to improve efficiency were made
 * 
 * //TODO: make this parser validating
 * 
 * @author Home
 */
@Service
public class GenericXMLAssignmentSerializer
        implements Serializer<Assignment, Reader, Writer> {

    @Override
    public Assignment deserialize(Reader xml) throws SerializationException {
        
        /* TODO: validate */
        try {
//            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
//            parserFactory.setValidating(true);
//            Schema.
//            parserFactory.setSchema(schema);
//            
//            SAXParser parser = parserFactory.newSAXParser();
            
            XMLReader reader = XMLReaderFactory.createXMLReader();      ///parser.getXMLReader();
            AssignmentHandler handler = new AssignmentHandler(reader);

            reader.setContentHandler(handler);
            reader.parse(new InputSource(xml));
            
            return handler.getAssignment();

        } catch (SAXException | IOException ex) {
            throw new SerializationException(ex);
        } 
       //  ParserConfigurationException ex
    }

}
