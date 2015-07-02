package logic.commands.editor;

import logic.Player;
import logic.commands.Command;
import logic.commands.KeyboardCommand;

import java.util.Map;

/**
 * Created by feinte on 17/06/2015.
 * Switch 2 commands in a player map
 */
public class SwitchCommand extends KeyboardCommand {

    private Player player;
    private Map<Player, Map<Integer, Command>> playerCommandMap;
    private int firstKey, secondKey;
    public SwitchCommand(Player player, Map<Player, Map<Integer, Command>> mouseCommandMap, int firstKey, int secondKey){
        this.player = player;
        playerCommandMap = mouseCommandMap;
        this.firstKey = firstKey;
        this.secondKey = secondKey;
    }

    @Override
    public void execute() {
        Map<Integer, Command> commandMap = playerCommandMap.get(player);
        Command temp = commandMap.get(firstKey);
        commandMap.put(firstKey, commandMap.get(secondKey));
        commandMap.put(secondKey, temp);
    }

    @Override
    public boolean isOnlyOnKeyJustPressed() {
        return true;
    }
}
