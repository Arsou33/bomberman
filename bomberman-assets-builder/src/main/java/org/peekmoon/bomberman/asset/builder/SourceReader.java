package org.peekmoon.bomberman.asset.builder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.w3c.dom.Element;

public class SourceReader {
    
    private String id;
    private List<Float> datas;

    public SourceReader(Element element) {
        id = element.getAttribute("id");
        Element datasElement = (Element) element.getElementsByTagName("float_array").item(0);
        datas = Arrays.stream(datasElement.getTextContent().split(" ")).map(Float::parseFloat).collect(Collectors.toList());
    }
    
    public List<Float> getDatas() {
        return datas;
    }

    public boolean is(String positionSourceId) {
        return id.equals(positionSourceId);
    }

}
