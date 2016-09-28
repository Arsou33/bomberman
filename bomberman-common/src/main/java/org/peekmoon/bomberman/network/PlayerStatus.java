package org.peekmoon.bomberman.network;

import java.nio.ByteBuffer;
// TODO : Check if synchronized are necessary
public class PlayerStatus {
    
    private float x,y;
    private boolean changed = true;

    public PlayerStatus(ByteBuffer buffer) {
        x = buffer.getFloat();
        y = buffer.getFloat();
    }
    
    public PlayerStatus() {
        x = 1;
        y = 1;
    }

    public synchronized void fill(ByteBuffer buffer) {
        buffer.putFloat(x);
        buffer.putFloat(y);
        changed = false;
    }

    public synchronized void deltaX(float d) {
        changed = true;
        x += d;
    }
    
    public synchronized void deltaY(float d) {
        changed = true;
        y += d;
    }
    
    public float getX() {
        return x;
    }

    public synchronized void setX(float x) {
        changed = true;
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public synchronized void setY(float y) {
        changed = true;
        this.y = y;
    }
    
    public boolean isChanged() {
        return changed;
    }



}
