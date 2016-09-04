package org.peekmoon.bomberman.asset.builder;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class Accessor<T> {

    static Accessor<?> instance(FloatBuffer datas2, int stride) {
        switch (stride) {
        case 2 : return new AccessorVector2f(datas2);
        case 3 : return new AccessorVector3f(datas2);
        }
        throw new IllegalArgumentException("Unable to find accessor for stride " + stride);
    }
     
    protected FloatBuffer datas;

    public Accessor(FloatBuffer datas) {
        this.datas = datas;
    }
    
    public abstract T getNextItem(FloatBuffer data);
    
    public List<T> getVectorList() {
        List<T> result = new ArrayList<>();
        datas.rewind();
        while (datas.hasRemaining()) {
            result.add(getNextItem(datas));
        }
        return result;
    }

}
