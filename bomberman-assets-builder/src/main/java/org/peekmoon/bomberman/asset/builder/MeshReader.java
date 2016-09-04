package org.peekmoon.bomberman.asset.builder;

import java.util.ArrayList;
import java.util.List;

import org.peekmoon.bomberman.Mesh;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MeshReader {
    
    private List<SourceReader> sourceReaders;
    private VerticesReader verticesReader;
    private PolylistReader polylistReader;

    public MeshReader(Element element) {
        // source
        sourceReaders = new ArrayList<>();
        NodeList sourceNodes = element.getElementsByTagName("source");
        for(int i = 0 ; i < sourceNodes.getLength(); i++) {
            sourceReaders.add(new SourceReader((Element) sourceNodes.item(i)));
        }
        
        // vertices
        verticesReader = new VerticesReader(this, (Element)element.getElementsByTagName("vertices").item(0));
        
        // polylist
        polylistReader = new PolylistReader(this, (Element) element.getElementsByTagName("polylist").item(0));
    }

    public SourceReader getSource(String sourceName) {
        for (SourceReader source : sourceReaders) {
            if (source.is(sourceName)) {
                return source;
            }
        }
        if (verticesReader.is(sourceName)) {
            return verticesReader.getPositionSource();
        }
        throw new IllegalStateException("Unable to find source " + sourceName);
    }
    
    public Mesh getMesh() {
        
        DataConverter dataConverter = new DataConverter(polylistReader);
        
        Mesh mesh = new Mesh();
        mesh.setVertices(dataConverter.getVertices());
        mesh.setIndices(dataConverter.getIndices());
        return mesh;
    }


    

}
