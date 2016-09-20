package org.peekmoon.bomberman.asset.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PolylistReader {
    
    private final static Logger log = LoggerFactory.getLogger(PolylistReader.class);

    
    private List<InputReader> inputReaders = new ArrayList<>();
    private List<Short> indices;
    
    public PolylistReader(MeshReader meshReader, Element element) {
        inputReaders = new ArrayList<>();
        
        NodeList inputNodes = element.getElementsByTagName("input");
        for(int i = 0 ; i < inputNodes.getLength(); i++) {
            inputReaders.add(new InputReader(meshReader, (Element) inputNodes.item(i)));
        }
        
        Element pElement = (Element) element.getElementsByTagName("p").item(0);
        indices = Arrays.stream(pElement.getTextContent().split(" ")).map(Short::parseShort).collect(Collectors.toList());
        log.debug("Indices : {} ", indices.size());    
    }
    
    
    public SourceReader getSource(String semantic) {
        return getInputBySemantic(semantic).getSource();
    }
    
    public boolean has(String semantic) {
        for (InputReader input : inputReaders) {
            if (input.isSemantic(semantic)) {
                return true;
            }
        }
        return false;
    }


    public short[] getIndices(String semantic) {
        int offset = getInputBySemantic(semantic).getOffset();
        short[] result = new short[indices.size()/inputReaders.size()];
        for (int i=0; i<result.length; i++) {
            result[i] = indices.get(i*inputReaders.size()+offset);
        }
        return result;
    }
    
    private InputReader getInputBySemantic(String semantic) {
        for (InputReader input : inputReaders) {
            if (input.isSemantic(semantic)) {
                return input;
            }
        }
        throw new IllegalStateException("Unknown semantic : " + semantic);
    }

}
