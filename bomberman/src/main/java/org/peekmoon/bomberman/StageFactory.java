package org.peekmoon.bomberman;

import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

import org.peekmoon.bomberman.board.BoardStage;
import org.peekmoon.bomberman.menu.MenuStage;

public class StageFactory {
    
    private final Map<Stage.Name, Stage> stages = new HashMap<>();
    
    
    public StageFactory(long window, DatagramSocket socket, String server, int port, TextRenderer textRenderer) {
        for (Stage.Name name : Stage.Name.values()) {
            switch (name) {
            case BOARD:
                stages.put(name, new BoardStage(window, socket, server, port));
                break;
            case MENU:
                stages.put(name, new MenuStage(window, textRenderer));
                break;
            default:
                throw new IllegalStateException("Stage is unknow " + name);
            
            }
        }
    }

    public Stage get(Stage.Name stageName) {
        return stages.get(stageName);
    }
    
}
