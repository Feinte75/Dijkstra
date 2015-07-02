package logic;

import entities.Entity;
import entities.EntityFactory;
import entities.EntityType;
import logic.commands.*;
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

    // Need to have 2 arrays because of 2 separate methods to poll
    private static final int[] keyCodes = {
            GLFW_KEY_SPACE,
    };
    private static final int[] mouseCodes = {
            GLFW_MOUSE_BUTTON_1,
            GLFW_MOUSE_BUTTON_2
    };
    private final Board board;
    private Map<Player, Map<Integer, Command>> playersCommandMaps;
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
        playersCommandMaps = new HashMap<Player, Map<Integer, Command>>(3);
        cursorListeners = new ArrayList<CursorListener>(2);
        keyStatusMap = new HashMap<Integer, KeyStatus>(5);
        for(int keyCode : keyCodes){
            keyStatusMap.put(keyCode, KeyStatus.KEY_RELEASED);
        }
        entityFactory = EntityFactory.getEntityFactory();
    }

    public void loadPlayerCommandMap(Player player){
        // TODO load config from file instead of hard coded

        Entity wallTile = entityFactory.createEntity(EntityType.TILE, Color.CYAN, 0, 0);
        Map<Integer, Command> commandMap = new HashMap<Integer, Command>(3);
        
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
        // Temporary storage of the command untill the Switch command is called
        commandMap.put(GLFW_KEY_0, addTileCommand);

        commandMap.put(GLFW_KEY_SPACE, new SwitchCommand(player, playersCommandMaps, GLFW_KEY_0, GLFW_MOUSE_BUTTON_1));

        playersCommandMaps.put(player, commandMap);
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

        for(int keyCode : keyCodes){

            if(glfwGetKey(window, keyCode) == GLFW_PRESS){
                if(keyStatusMap.get(keyCode) == KeyStatus.KEY_RELEASED)
                    keyStatusMap.put(keyCode, KeyStatus.KEY_JUST_PRESSED);
                else
                    keyStatusMap.put(keyCode, KeyStatus.KEY_PRESSED);
            }
            else
                keyStatusMap.put(keyCode, KeyStatus.KEY_RELEASED);
        }

        for(int mouseCode : mouseCodes){

            if(glfwGetMouseButton(window, mouseCode) == GLFW_PRESS){
                if(keyStatusMap.get(mouseCode) == KeyStatus.KEY_RELEASED)
                    keyStatusMap.put(mouseCode, KeyStatus.KEY_JUST_PRESSED);
                else
                    keyStatusMap.put(mouseCode, KeyStatus.KEY_PRESSED);
            }
            else
                keyStatusMap.put(mouseCode, KeyStatus.KEY_RELEASED);
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
        for(int keyCode : keyCodes){
            //System.out.println("Keycode : "+keyCode+ " Corresponding state : "+keyStatusMap.get(keyCode)+" keyJustPressed " + getCommand(player, keyCode).isOnlyOnKeyJustPressed());
            if((keyStatusMap.get(keyCode) == KeyStatus.KEY_JUST_PRESSED) && getCommand(player, keyCode).isOnlyOnKeyJustPressed())
                getCommand(player, keyCode).execute();
            else if(((keyStatusMap.get(keyCode) == KeyStatus.KEY_PRESSED) && !getCommand(player, keyCode).isOnlyOnKeyJustPressed()))
                getCommand(player, keyCode).execute();
        }
        for(int mouseCode : mouseCodes){
            //System.out.println("mouseCode : "+mouseCode+ " Corresponding state : "+keyStatusMap.get(mouseCode)+" keyJustPressed " + getCommand(player, mouseCode).isOnlyOnKeyJustPressed());
            if((keyStatusMap.get(mouseCode) == KeyStatus.KEY_JUST_PRESSED) && getCommand(player, mouseCode).isOnlyOnKeyJustPressed())
                getCommand(player, mouseCode).execute();
            else if(((keyStatusMap.get(mouseCode) == KeyStatus.KEY_PRESSED) && !getCommand(player, mouseCode).isOnlyOnKeyJustPressed()))
                getCommand(player, mouseCode).execute();
        }
    }

    /**
     * Get the Command corresponding to the code associated to the player
     *
     * @param player Player to recover the command from
     * @param glCode Input code (Key or Mouse)
     * @return The appropriate command mapped to the input code
     */
    public Command getCommand(Player player, int glCode){
        return playersCommandMaps.get(player).get(glCode);
    }

    public void resize(int width, int height){
        this.width = width;
        this.height = height;
    }

}
