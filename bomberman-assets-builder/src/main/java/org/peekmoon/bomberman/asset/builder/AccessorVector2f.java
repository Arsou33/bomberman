package org.peekmoon.bomberman.asset.builder;

import java.nio.FloatBuffer;

import org.joml.Vector2f;

public class AccessorVector2f extends Accessor<Vector2f> {

    public AccessorVector2f(FloatBuffer datas2) {
        super(datas2);
    }

    @Override
    public Vector2f getNextItem(FloatBuffer data) {
        return new Vector2f(data.get(), data.get());
    }

}
