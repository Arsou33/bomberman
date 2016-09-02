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
        verticesReader = new VerticesReader((Element)element.getElementsByTagName("vertices").item(0));
        
        // polylist
        polylistReader = new PolylistReader((Element) element.getElementsByTagName("polylist").item(0));
        
        
    }

    public Mesh getMesh() {
        Mesh mesh = new Mesh();
        mesh.setIndices(polylistReader.getIndices("VERTEX"));
        mesh.setPositions(getPositions());
        return mesh;
    }

    private float[] getPositions() {
        String positionSourceId = verticesReader.getPositionSourceId();
        for (SourceReader sourceReader : sourceReaders) {
            if (sourceReader.is(positionSourceId)) {
                float[] result = new float[sourceReader.getDatas().size()];
                for (int i = 0; i<result.length; i++) {
                    result[i] = sourceReader.getDatas().get(i);
                }
                return result;
            }
        }
        throw new IllegalStateException();
    }
    
    

}
