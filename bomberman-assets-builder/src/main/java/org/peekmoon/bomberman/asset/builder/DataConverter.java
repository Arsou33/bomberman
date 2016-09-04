package org.peekmoon.bomberman.asset.builder;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class DataConverter {

    private PolylistReader polylistReader;
    
    private float[] outVertices;
    private short[] outIndices;
    
    public DataConverter(PolylistReader polylistReader) {
        this.polylistReader = polylistReader;
        convert();
    }
    
    public float[] getVertices() {
        return outVertices;
    }
    
    public short[] getIndices() {
        return outIndices;
    }

    private void convert() {
        List<Vector3f> positions = polylistReader.getSource("VERTEX").getVectors();
        short[] positionsIndices = polylistReader.getIndices("VERTEX");
        
        List<Vector2f> texCoords = null;
        short[] textCoordsIndices = null;
        
        if (polylistReader.has("TEXCOORD")) {
            texCoords = polylistReader.getSource("TEXCOORD").getVectors();
            textCoordsIndices = polylistReader.getIndices("TEXCOORD");
        }
        
        List<Vertex> vertices = new ArrayList<>();
        List<Short> indices = new ArrayList<>(positions.size());

        for (int i=0; i<positionsIndices.length; i++) {
            // Construct vertex at position 
            Vertex vertex;
            if (polylistReader.has("TEXCOORD")) {
                vertex = new Vertex(positions.get(positionsIndices[i]), texCoords.get(textCoordsIndices[i]));
            } else {
                vertex = new Vertex(positions.get(positionsIndices[i]));
            }
            // Search vertex in vertices if not add the new one
            int indice;
            if (vertices.contains(vertex)) {
                indice = vertices.indexOf(vertex);
            } else {
                vertices.add(vertex);
                indice = vertices.size()-1;
            }
            indices.add((short) indice);
        }
        
        fillVertices(vertices);
        fillIndices(indices);
        
    }

    private void fillVertices(List<Vertex> vertices) {
        // TODO : Error if TEXCOORD NOT PRESENT
        outVertices = new float[vertices.size()*5];
        for (int i=0; i<vertices.size(); i++) {
            outVertices[5*i+0] = vertices.get(i).getPosition().x;
            outVertices[5*i+1] = vertices.get(i).getPosition().y;
            outVertices[5*i+2] = vertices.get(i).getPosition().z;
            outVertices[5*i+3] = vertices.get(i).getTexCoord().x;
            outVertices[5*i+4] = vertices.get(i).getTexCoord().y;
        }
    }

    private void fillIndices(List<Short> indices) {
        outIndices = new short[indices.size()];
        for (int i=0; i<indices.size(); i++) {
            outIndices[i] = indices.get(i);
        }        
    }

}
