package logic.commands;

import logic.Player;
import org.lwjgl.glfw.GLFW;

import java.util.Map;

/**
 * Created by feinte on 17/06/2015.
 */
public class SwitchCommand extends KeyboardCommand{

    private Player player;
    private Map<Player, Map<Integer, MouseCommand>> playerToMouseCommandMap;

    public SwitchCommand(Player player, Map<Player, Map<Integer, MouseCommand>> mouseCommandMap){
        this.player = player;
        playerToMouseCommandMap = mouseCommandMap;
    }

    @Override
    public void execute() {
        Map<Integer, MouseCommand> commandMap = playerToMouseCommandMap.get(player);
        MouseCommand temp = commandMap.get(GLFW.GLFW_MOUSE_BUTTON_1);
        commandMap.replace(GLFW.GLFW_MOUSE_BUTTON_1, temp, commandMap.get(GLFW.GLFW_KEY_0));
        commandMap.replace(GLFW.GLFW_KEY_0, commandMap.get(GLFW.GLFW_MOUSE_BUTTON_1), temp);
    }
}
