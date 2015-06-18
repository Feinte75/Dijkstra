package logic;

import entities.Entity;
import entities.Tile;
import graphic.opengl.Primitive;
import graphic.opengl.Square;
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

    private Board board;
    private Map<Player, Map<Integer, Command>> playersCommandMaps;
    private ArrayList<CursorListener> cursorListeners;
    private long window;
    private int xCursPos, yCursPos;
    private DoubleBuffer xPosBuffer, yPosBuffer;
    private int width, height;

    private Map<Integer, KeyStatus> keyStatusMap;
    // Need to have 2 arrays because of 2 separate methods to poll
    private static int[] keyCodes = {
            GLFW_KEY_SPACE,
    };
    private static int[] mouseCodes = {
            GLFW_MOUSE_BUTTON_1,
            GLFW_MOUSE_BUTTON_2
    };
    private int[] combinedCodes;

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
    }

    public void loadPlayerCommandMap(Player player){
        // TODO load config from file instead of hard coded
        ArrayList<Primitive> primitives = new ArrayList<Primitive>(1);
        primitives.add(new Square(24));
        Entity wallTile = new Tile(Color.CYAN, 0, 0, primitives);
        Map<Integer, Command> commandMap = new HashMap<Integer, Command>(3);
        
        MouseCommand addTileCommand = new AddTileCommand(wallTile, board);
        cursorListeners.add(addTileCommand);
        commandMap.put(GLFW_MOUSE_BUTTON_1, addTileCommand);

        MouseCommand removeTileCommand = new RemoveTileCommand(null, board);
        cursorListeners.add(removeTileCommand);
        commandMap.put(GLFW_MOUSE_BUTTON_2, removeTileCommand);

        // We can keep the same primitives, they will be cloned when command fire
        Entity goalTile = new Tile(Color.BLACK, 0, 0, primitives);
        addTileCommand = new AddTileCommand(goalTile, board);
        cursorListeners.add(addTileCommand);
        // Temporary storage of the command untill the Switch command is called
        commandMap.put(GLFW_KEY_0, addTileCommand);

        commandMap.put(GLFW_KEY_SPACE, new SwitchCommand(player, playersCommandMaps, GLFW_KEY_0, GLFW_MOUSE_BUTTON_1));

        playersCommandMaps.put(player, commandMap);
    }


    private void getCursorPosition(){
        glfwGetCursorPos(window, xPosBuffer, yPosBuffer);
        xCursPos = ((int) xPosBuffer.get(0));
        yCursPos = height - ((int) yPosBuffer.get(0));
        //System.out.println("xPos : " + xCursPos + "  yPos : " + yCursPos);
    }

    private void notifyListeners(){
        for(CursorListener cursorListener : cursorListeners){
            cursorListener.update(xCursPos, yCursPos);
        }
    }

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

    public Command getCommand(Player player, int glCode){
        return playersCommandMaps.get(player).get(glCode);
    }

    public void resize(int width, int height){
        this.width = width;
        this.height = height;
    }

}
