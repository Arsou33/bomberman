package org.peekmoon.bomberman;

import java.util.HashMap;
import java.util.Map;

import org.peekmoon.bomberman.board.BoardStage;
import org.peekmoon.bomberman.menu.CreateOrJoinStage;
import org.peekmoon.bomberman.menu.LobbyStage;
import org.peekmoon.bomberman.menu.WaitingRoomStage;
import org.peekmoon.bomberman.network.CommandSender;
import org.peekmoon.bomberman.network.StatusReceiver;

public class StageFactory {
    
    private final Map<Stage.Name, Stage> stages = new HashMap<>();
    
    public StageFactory(long window, CommandSender commandSender, StatusReceiver statusReceiver, TextRenderer textRenderer) {
        for (Stage.Name name : Stage.Name.values()) {
            switch (name) {
            case CREATE_OR_JOIN_MENU:
                stages.put(name, new CreateOrJoinStage(window, textRenderer));
                break;
            case LOBBY:
                stages.put(name, new LobbyStage(window, statusReceiver, textRenderer, commandSender));
                break;
            case WAITING_ROOM:
                stages.put(name, new WaitingRoomStage(window, statusReceiver, textRenderer, commandSender));
                break;                
            case BOARD:
                stages.put(name, new BoardStage(window, statusReceiver, commandSender));
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
