package org.peekmoon.bomberman.asset.builder;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class DataConverter {

    private PolylistReader polylistReader;
    
    private float[] outPositions;
    private float[] outTexCoords;
    private short[] outIndices;
    
    public DataConverter(PolylistReader polylistReader) {
        this.polylistReader = polylistReader;
        convert();
    }
    
    public float[] getPositions() {
        return outPositions;
    }
    
    public float[] getTexCoords() {
        return outTexCoords;
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
        
        fillPositions(vertices);
        if (polylistReader.has("TEXCOORD")) {
            fillTexCoords(vertices);
        }
        fillIndices(indices);
        
    }

    private void fillPositions(List<Vertex> vertices) {
        outPositions = new float[vertices.size()*3];
        for (int i=0; i<vertices.size(); i++) {
            outPositions[3*i+0] = vertices.get(i).getPosition().x;
            outPositions[3*i+1] = vertices.get(i).getPosition().y;
            outPositions[3*i+2] = vertices.get(i).getPosition().z;
        }
    }

    private void fillTexCoords(List<Vertex> vertices) {
        outTexCoords = new float[vertices.size()*2];
        for (int i=0; i<vertices.size(); i++) {
            outTexCoords[2*i+0] = vertices.get(i).getTexCoord().x;
            outTexCoords[2*i+1] = vertices.get(i).getTexCoord().y;
        }        
    }

    private void fillIndices(List<Short> indices) {
        outIndices = new short[indices.size()];
        for (int i=0; i<indices.size(); i++) {
            outIndices[i] = indices.get(i);
        }        
    }

}
