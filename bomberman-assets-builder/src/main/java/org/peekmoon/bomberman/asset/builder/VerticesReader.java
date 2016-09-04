package org.peekmoon.bomberman.asset.builder;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class VerticesReader {
    
    private String id;
    private List<InputReader> inputReaders = new ArrayList<>();

    public VerticesReader(MeshReader meshReader, Element element) {
        id = element.getAttribute("id");
        
        // TODO : Factorize code with polylistReader or with SourceReader
        inputReaders = new ArrayList<>();
        NodeList inputNodes = element.getElementsByTagName("input");
        for(int i = 0 ; i < inputNodes.getLength(); i++) {
            inputReaders.add(new InputReader(meshReader, (Element) inputNodes.item(i)));
        }
    }
    
    public SourceReader getPositionSource() {
        for (InputReader input : inputReaders) {
            if (input.isSemantic("POSITION")) {
                return input.getSource();
            }
        }
        throw new IllegalStateException("Unable to extract position");
    }

    public boolean is(String name) {
        return id.equals(name);
    }

}
