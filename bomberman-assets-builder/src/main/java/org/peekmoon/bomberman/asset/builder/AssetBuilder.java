package org.peekmoon.bomberman.asset.builder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class AssetBuilder {

    public static void main(String[] args) {
        try {
            new AssetBuilder().build();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        
    }

    private void build() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        XPathFactory xpf = XPathFactory.newInstance();
        XPath path = xpf.newXPath();
        
        Document xml = builder.parse(getClass().getResourceAsStream("/cube.dae"));
        //Document xml = builder.parse(new File("C:/Users/j.lelong/Downloads/splash-pokedstudio.dae"));
        MeshReader meshReader = new MeshReader((Element) path.evaluate("//geometry[@name='Cube']/mesh", xml, XPathConstants.NODE));
        
        FileOutputStream fos = new FileOutputStream("C:/Users/j.lelong/Documents/Perso/dev/bomberman-project/bomberman/src/main/resources/mesh/cube.mesh");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(meshReader.getMesh());
        fos.close();
    }

    /*
    private void build() throws XMLStreamException, IOException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(getClass().getResourceAsStream("/cube.dae"));
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals("geometry")) {
                    String name = startElement.getAttributeByName(new QName("name")).getValue();
                    System.out.println("Start of geometry for " + name);
                    Mesh mesh = new Mesh();
                    
                    FileOutputStream fos = new FileOutputStream("C:/Users/j.lelong/Documents/Perso/dev/bomberman/src/main/resources/mesh/cube.mesh");
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(mesh);
                    fos.close();
                }
            }
        }
    }
    */

}
