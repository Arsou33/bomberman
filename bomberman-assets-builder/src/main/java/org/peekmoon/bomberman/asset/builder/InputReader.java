package org.peekmoon.bomberman.asset.builder;

import org.peekmoon.bomberman.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class InputReader {
    
    private final static Logger log = LoggerFactory.getLogger(InputReader.class);

    
    private String semantic;
    private SourceReader source; // TODO : Replace by reference to source
    
    private int offset;
    
    public InputReader(MeshReader mesh, Element element) {
        semantic  = element.getAttribute("semantic");
        source = mesh.getSource(element.getAttribute("source").substring(1));
        if (element.hasAttribute("offset")) {
            offset = Integer.parseInt(element.getAttribute("offset"));
        }
        log.debug("Find an input " + semantic + " reference : " + source + " offset : " + offset );
    }

    public boolean isSemantic(String semantic) {
        return this.semantic.equals(semantic);
    }
    
    public int getOffset() {
        return offset;
    }

    public SourceReader getSource() {
        return source;
    }

}
