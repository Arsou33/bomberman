package org.peekmoon.bomberman.asset.builder;

import java.nio.FloatBuffer;

import org.joml.Vector3f;

public class AccessorVector3f extends Accessor<Vector3f> {

    public AccessorVector3f(FloatBuffer datas) {
        super(datas);
    }

    @Override
    public Vector3f getNextItem(FloatBuffer data) {
        return new Vector3f(data.get(), data.get(), data.get());
    }

   
}
