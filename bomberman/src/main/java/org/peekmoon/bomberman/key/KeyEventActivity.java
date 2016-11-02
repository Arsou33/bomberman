package org.peekmoon.bomberman.key;


public class KeyEventActivity {

    private final int action;
    private final KeyActivity activity;
    
    
    public KeyEventActivity(int action, KeyActivity activity) {
        this.action = action;
        this.activity = activity;
    }
    
    public void fire() {
        activity.fire();
    }

    public boolean isTriggerState(int action) {
        return this.action == action;
    }

    
    

}
