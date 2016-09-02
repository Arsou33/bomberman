package org.peekmoon.bomberman.asset.builder;

import org.w3c.dom.Element;

public class InputReader {
    
    private String semantic;
    private String source; // TODO : Replace by reference to source
    private int offset;
    
    public InputReader(Element element) {
        semantic  = element.getAttribute("semantic");
        source = element.getAttribute("source");
        if (element.hasAttribute("offset")) {
            offset = Integer.parseInt(element.getAttribute("offset"));
        }
        System.out.println("Find an input " + semantic + " reference : " + source + " offset : " + offset );
    }

    public boolean isSemantic(String semantic) {
        return this.semantic.equals(semantic);
    }

    public String getSource() {
        return source;
    }

    public int getOffset() {
        return offset;
    }

}
