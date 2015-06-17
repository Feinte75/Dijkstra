package logic.commands;

import entities.Entity;
import entities.Tile;
import graphic.opengl.Primitive;
import logic.Board;

import java.util.ArrayList;

/**
 * Created by feinte on 04/06/2015.
 * Command to add a tile to the board
 */
public class AddTileCommand extends MouseCommand {

    private Entity entity;
    private Board board;

    public AddTileCommand(Entity entity, Board board){
        this.entity = entity;
        this.board = board;
    }

    @Override
    public void execute(int xPos, int yPos) {
        board.addTile(new Tile(entity.getColor(), xPos, yPos, (ArrayList<Primitive>)entity.getPrimitives().clone()));
        System.out.println("Add tile");
    }

}
