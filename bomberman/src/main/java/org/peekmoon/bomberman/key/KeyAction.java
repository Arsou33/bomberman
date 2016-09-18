package org.peekmoon.bomberman.key;

@FunctionalInterface
public interface KeyAction {
    void fire(float elapsedTime);
}
