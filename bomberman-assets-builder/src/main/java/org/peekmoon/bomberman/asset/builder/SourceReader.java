package org.peekmoon.bomberman.asset.builder;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Element;


public class SourceReader {
    
    private String id;
    private Accessor<?> accessor;
    private FloatBuffer datas;

    public SourceReader(Element element) {
        id = element.getAttribute("id");
        Element datasElement = (Element) element.getElementsByTagName("float_array").item(0);
        
        String[] datasStringArray = datasElement.getTextContent().split(" ");
        datas = FloatBuffer.allocate(datasStringArray.length);
        Arrays.stream(datasStringArray).map(Float::parseFloat).forEach(value->datas.put(value));
        
        
        int stride = element.getElementsByTagName("param").getLength();
        
        accessor = Accessor.instance(datas, stride);
    }
    
    public FloatBuffer getDatas() {
        return datas;
    }

    public boolean is(String positionSourceId) {
        return id.equals(positionSourceId);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getVectors() {
        return (List<T>) accessor.getVectorList();
    }

}
