package org.peekmoon.bomberman.asset.builder;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class VerticesReader {
    
    private List<InputReader> inputReaders = new ArrayList<>();

    public VerticesReader(Element element) {
        inputReaders = new ArrayList<>();
        
        // TODO : Factorize code with polylistReader
        NodeList inputNodes = element.getElementsByTagName("input");
        for(int i = 0 ; i < inputNodes.getLength(); i++) {
            inputReaders.add(new InputReader((Element) inputNodes.item(i)));
        }
    }
    
    public String getPositionSourceId() {
        for (InputReader input : inputReaders) {
            if (input.isSemantic("POSITION")) {
                return input.getSource().substring(1);
            }
        }
        throw new IllegalStateException("Unable to extract position");
    }

}
