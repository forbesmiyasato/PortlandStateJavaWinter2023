/**
 * The {code XmlParser} class
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

/**
 * <code>XmlParser</code> class for Project 4.
 * Validates XML file according to provided DTD:
 * http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd
 * File will only be parsed when it is valid.
 * An airline object is produced from the airline and flight details within the xml file.
 */
public class XmlParser implements AirlineParser<Airline> {
    private Airline airline;
    final String DTD = "";
    private String filepath ="";
    private File airlineXML;
    private Document xml;

    /**
     * Constructor for XmlParser
     * @param filepath - only accepts a string filepath
     * */
    public XmlParser(String filepath){
        this.filepath=filepath;
    }

    public XmlParser(File airlineXML){this.airlineXML = airlineXML;}

    /**
     * parse() method
     * @return Airline object when the XML file is successfully parsed
     * @return null when any flight information is invalid
     * @throws ParserException if the XML file path is invalid
     * */
    public Airline parse() throws ParserException{
        try {
            //if(filepath.isEmpty()) throw new ParserException("File path is empty");
            if(airlineXML==null) throw new ParserException("Airline XML is null");
            AirlineXmlHelper helper = new AirlineXmlHelper();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = buildDoc(factory);
            builder.setErrorHandler(helper);
            builder.setEntityResolver(helper);
            //xml = builder.parse(new File(filepath));
            xml = builder.parse(airlineXML);
        } catch (SAXException | IOException | ParserException e) {
            throw new ParserException("Airline XML file is null",e);
        }

        Element root = xml.getDocumentElement();
        String airlineName = root.getElementsByTagName("name").item(0).getTextContent();
        this.airline = new Airline(airlineName);

        NodeList innerFlights = root.getElementsByTagName("flight");
        Flight fl;
        for(int i=0; i<innerFlights.getLength(); i++){
            try {
                fl = buildFlight(innerFlights.item(i));
            } catch (Exception e) {
                // User must of modified the system file directly, so I think it's reasonable to
                // ignore erroneous entries.
                System.out.println("Flight information for the number " + i +
                        " of XML file does not conform to the DTD");
                System.out.println(e);
                continue;
            }
            this.airline.addFlight(fl);
        }

        return airline;
    }
/*
    public List<Airline> parseXMLAirlineList(){
        List<Airline> listOfAirlines = new ArrayList<>();

        try {
            if(filepath.isEmpty()) throw new ParserException("File path is empty");
            AirlineXmlHelper helper = new AirlineXmlHelper();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = buildDoc(factory);
            builder.setErrorHandler(helper);
            builder.setEntityResolver(helper);
            xml = builder.parse(new File(filepath));
        } catch (SAXException | IOException | ParserException e) {
            err.append(e.getMessage());
            //throw new ParserException("Please enter a valid XML file path.",e);
        }

        return null;
    }
*/
    /**
     * buildDoc method
     * @param factory DocumentBuilderFactor
     * @return a DocumentBuilder object
     * */
    private DocumentBuilder buildDoc(DocumentBuilderFactory factory){
        DocumentBuilder builder =
                null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("DocumentBuilderFactory could not produce a builder.");
            throw new RuntimeException(e);
        }
        return builder;
    }

    /**
     * createDate method
     * @param date accepts an Element object
     * @return String object with the date in MM/DD/YYYY format
     * */
    private String createDate(Element date){
        String day = date.getAttribute("day");
        String month = date.getAttribute("month");
        String year = date.getAttribute("year");
        StringBuilder sb = new StringBuilder(month + "/" + day + "/" + year);
        return sb.toString();
    }

    /**
     * createTime method
     * @param time accepts an Element object
     * @return String object with the time in 24hr HH:MM format
     * */
    private String createTime(Element time){
        String hour = time.getAttribute("hour");
        int minute = Integer.parseInt(time.getAttribute("minute"));
        StringBuilder sb = new StringBuilder(hour+":");
        if(minute<10) sb.append("0");
        sb.append(Integer.toString(minute));
        return sb.toString();
    }

    /**
     * datetime method
     * @param flElement accepts an Element that is a flight
     * @param type accepts a String object that declares that the flight is for depart or arrive
     * @return new String[]{date, time} object
     * calls createDate and createTime methods
     * */
    private String[] datetime(Element flElement, String type){
        Node n = flElement.getElementsByTagName(type).item(0);
        Element datetime = (Element)n;
        Node dateNode = datetime.getElementsByTagName("date").item(0);
        Element dateElement = (Element) dateNode;
        Node timeNode = datetime.getElementsByTagName("time").item(0);
        Element timeElement = (Element) timeNode;
        String date = createDate(dateElement);
        String time = createTime(timeElement);
        return new String[]{date,time};
    }

    /**
     * buildFlight method
     * Constructs a Flight object with the flight number, departure & arrival airport,
     * date & time information
     * @param fl accepts a Node object
     * @return Flight object
     * */
    private Flight buildFlight(Node fl){
        if(fl==null)return null;
        Element flElement = (Element)fl;
        String number = flElement.getElementsByTagName("number").item(0).getTextContent();
        String src = flElement.getElementsByTagName("src").item(0).getTextContent();
        String dest = flElement.getElementsByTagName("dest").item(0).getTextContent();

        String[] depart = datetime(flElement,"depart");
        String[] arrive = datetime(flElement, "arrive");

        return new Flight(number,src,depart[0],depart[1],dest,arrive[0],arrive[1],true);
    }
}
