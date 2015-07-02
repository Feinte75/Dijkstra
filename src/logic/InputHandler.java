package logic;

import entities.Entity;
import entities.EntityFactory;
import entities.EntityType;
import logic.commands.Command;
import logic.commands.MouseCommand;
import logic.commands.PlayerControlledEntityAction;
import logic.commands.editor.AddTileCommand;
import logic.commands.editor.RemoveTileCommand;
import logic.commands.editor.SwitchCommand;
import logic.commands.game.MovementAction;
import logic.listeners.CursorListener;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by feinte on 05/06/2015.
 * Take care of inputs and execute the appropriate commands
 */
public class InputHandler {

    private static final int[] inputCodes = {

            GLFW_KEY_SPACE,
            GLFW_KEY_RIGHT,
            GLFW_KEY_LEFT,
            GLFW_KEY_UP,
            GLFW_KEY_DOWN,

            GLFW_MOUSE_BUTTON_1,
            GLFW_MOUSE_BUTTON_2
    };
    private final Board board;
    private Map<Player, Map<Integer, Command>> playersCommandMap;
    private Map<Player, Map<Integer, PlayerControlledEntityAction>> playersActionsMap;
    private ArrayList<CursorListener> cursorListeners;
    private long window;
    private int xCursPos, yCursPos;
    private DoubleBuffer xPosBuffer, yPosBuffer;
    private int width, height;
    private EntityFactory entityFactory;
    private Map<Integer, KeyStatus> keyStatusMap;

    public InputHandler(Board board, long window, int height){
        this.board = board;
        this.window = window;
        xPosBuffer = BufferUtils.createDoubleBuffer(1);
        yPosBuffer = BufferUtils.createDoubleBuffer(1);
        this.height = height;
        playersCommandMap = new HashMap<Player, Map<Integer, Command>>(3);
        playersActionsMap = new HashMap<Player, Map<Integer, PlayerControlledEntityAction>>(4);
        cursorListeners = new ArrayList<CursorListener>(2);
        keyStatusMap = new HashMap<Integer, KeyStatus>(5);
        for (int inputCode : inputCodes) {
            keyStatusMap.put(inputCode, KeyStatus.KEY_RELEASED);
        }
        entityFactory = EntityFactory.getEntityFactory();
    }

    public void loadPlayerCommandMap(Player player){
        // TODO load config from file instead of hard coded

        Map<Integer, Command> commandMap = new HashMap<Integer, Command>(3);

        Entity wallTile = entityFactory.createEntity(EntityType.TILE, Color.CYAN, 0, 0);
        MouseCommand addTileCommand = new AddTileCommand(wallTile, board);
        cursorListeners.add(addTileCommand);
        commandMap.put(GLFW_MOUSE_BUTTON_1, addTileCommand);

        MouseCommand removeTileCommand = new RemoveTileCommand(board);
        cursorListeners.add(removeTileCommand);
        commandMap.put(GLFW_MOUSE_BUTTON_2, removeTileCommand);

        // We can keep the same primitives, they will be cloned when command fire
        Entity goalTile = entityFactory.createEntity(EntityType.TILE, Color.BLACK, 0, 0);
        addTileCommand = new AddTileCommand(goalTile, board);
        cursorListeners.add(addTileCommand);
        // Temporary storage of the command until the Switch command is called
        commandMap.put(GLFW_KEY_0, addTileCommand);

        commandMap.put(GLFW_KEY_SPACE, new SwitchCommand(player, playersCommandMap, GLFW_KEY_0, GLFW_MOUSE_BUTTON_1));

        playersCommandMap.put(player, commandMap);

        Map<Integer, PlayerControlledEntityAction> actionMap = new HashMap<Integer, PlayerControlledEntityAction>(3);
        actionMap.put(GLFW_KEY_RIGHT, new MovementAction(+5, 0));
        actionMap.put(GLFW_KEY_LEFT, new MovementAction(-5, 0));
        actionMap.put(GLFW_KEY_UP, new MovementAction(0, +5));
        actionMap.put(GLFW_KEY_DOWN, new MovementAction(0, -5));
        playersActionsMap.put(player, actionMap);

    }

    /**
     * Recover user cursor position and store it
     */
    private void getCursorPosition(){
        glfwGetCursorPos(window, xPosBuffer, yPosBuffer);
        xCursPos = ((int) xPosBuffer.get(0));
        yCursPos = height - ((int) yPosBuffer.get(0));
    }

    private void notifyListeners(){
        for(CursorListener cursorListener : cursorListeners){
            cursorListener.update(xCursPos, yCursPos);
        }
    }

    /**
     * Check the input status and change them according to the current and past states
     */
    private void updateKeyStatus(){

        for (int inputCode : inputCodes) {

            if ((inputCode <= 7) && (inputCode >= 0)) { //Mouse Input

                if (glfwGetMouseButton(window, inputCode) == GLFW_PRESS) {
                    if (keyStatusMap.get(inputCode) == KeyStatus.KEY_RELEASED)
                        keyStatusMap.put(inputCode, KeyStatus.KEY_JUST_PRESSED);
                    else
                        keyStatusMap.put(inputCode, KeyStatus.KEY_PRESSED);
                }
                else
                    keyStatusMap.put(inputCode, KeyStatus.KEY_RELEASED);
            } else { // Keyboard input
                if (glfwGetKey(window, inputCode) == GLFW_PRESS) {
                    if (keyStatusMap.get(inputCode) == KeyStatus.KEY_RELEASED)
                        keyStatusMap.put(inputCode, KeyStatus.KEY_JUST_PRESSED);
                    else
                        keyStatusMap.put(inputCode, KeyStatus.KEY_PRESSED);
                } else
                    keyStatusMap.put(inputCode, KeyStatus.KEY_RELEASED);
            }
        }
    }

    /**
     * Check for new input and execute associated Command
     *
     * @param player Player's inputs checked
     */
    public void handlePlayerInput(Player player){

        getCursorPosition();
        notifyListeners();
        updateKeyStatus();

        // TODO find a way to iterate only once over the codes (use combined codes array)
        for (int inputCode : inputCodes) {
            //System.out.println("Keycode : "+keyCode+ " Corresponding state : "+keyStatusMap.get(keyCode)+" keyJustPressed " + getCommand(player, keyCode).isOnlyOnKeyJustPressed());
            if (executeCommand(player, inputCode))
                getCommand(player, inputCode).execute();

            if (executeAction(player, inputCode)) {
                getAction(player, inputCode).execute(player.getHero());
                System.out.println("Action !");
            }
        }
    }

    public boolean executeCommand(Player player, int inputCode) {
        if (keyStatusMap.get(inputCode) == KeyStatus.KEY_RELEASED || getCommand(player, inputCode) == null)
            return false;

        return !(keyStatusMap.get(inputCode) == KeyStatus.KEY_PRESSED && getCommand(player, inputCode).isOnlyOnKeyJustPressed());

    }

    public boolean executeAction(Player player, int inputCode) {
        return !(keyStatusMap.get(inputCode) == KeyStatus.KEY_RELEASED || getAction(player, inputCode) == null);

    }

    /**
     * Get the Command corresponding to the code associated to the player
     *
     * @param player Player to recover the command from
     * @param glCode Input code (Key or Mouse)
     * @return The appropriate command mapped to the input code
     */
    public Command getCommand(Player player, int glCode){
        return playersCommandMap.get(player).get(glCode);
    }

    public PlayerControlledEntityAction getAction(Player player, int glCode) {
        return playersActionsMap.get(player).get(glCode);
    }

    public void resize(int width, int height){
        this.width = width;
        this.height = height;
    }

}
