package logic;

import entities.Entity;
import entities.Tile;
import graphic.opengl.Primitive;
import graphic.opengl.Square;
import logic.commands.AddTileCommand;
import logic.commands.MouseCommand;
import logic.commands.RemoveTileCommand;
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
    private Map<Player, Map<Integer, MouseCommand>> playerToMouseCommandMap;
    private long window;
    private int xCursPos, yCursPos;
    private DoubleBuffer xPosBuffer, yPosBuffer;
    private int width, height;

    public InputHandler(Board board, long window, int height){
        this.board = board;
        this.window = window;
        xPosBuffer = BufferUtils.createDoubleBuffer(1);
        yPosBuffer = BufferUtils.createDoubleBuffer(1);
        this.height = height;
        playerToMouseCommandMap = new HashMap<Player, Map<Integer, MouseCommand>>(2);
    }

    public void loadPlayerCommandMap(Player player){
        // TODO load config from file instead of hard coded
        ArrayList<Primitive> primitives = new ArrayList<Primitive>(1);
        primitives.add(new Square(24));
        Entity entity = new Tile(Color.CYAN, 0, 0, primitives);
        Map<Integer, MouseCommand> mouseCommandMap = new HashMap<Integer, MouseCommand>(3);
        mouseCommandMap.put(GLFW_MOUSE_BUTTON_1, new AddTileCommand(entity, board));
        mouseCommandMap.put(GLFW_MOUSE_BUTTON_2, new RemoveTileCommand(null, board));
        playerToMouseCommandMap.put(player, mouseCommandMap);
    }


    public void getCursorPosition(){
        glfwGetCursorPos(window, xPosBuffer, yPosBuffer);
        xCursPos = ((int) xPosBuffer.get(0));
        yCursPos = height - ((int) yPosBuffer.get(0));
        System.out.println("xPos : " + xCursPos + "  yPos : " + yCursPos);
    }

    public void getPlayerMouseEvent(Player player){

        getCursorPosition();
        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1) {
            getPlayerMouseCommand(player, GLFW_MOUSE_BUTTON_1).execute(xCursPos, yCursPos);
        }
        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_2) == 1) {
            getPlayerMouseCommand(player, GLFW_MOUSE_BUTTON_2).execute(xCursPos, yCursPos);
        }
    }

    public MouseCommand getPlayerMouseCommand(Player player, int glCode){
        return playerToMouseCommandMap.get(player).get(glCode);
    }

    public void resize(int width, int height){
        this.width = width;
        this.height = height;
    }

}
