package logic.commands;

import entities.Entity;
import logic.Board;

/**
 * Created by feinte on 04/06/2015.
 * Command to add a tile to the board
 */
public class AddTileCommand extends MouseCommand {

    private final Entity entity;
    private final Board board;

    public AddTileCommand(Entity entity, Board board){
        this.entity = entity;
        this.board = board;
    }

    @Override
    public void execute() {
        board.addTile(entity.getColor(), xPos, yPos);
    }

    @Override
    public boolean isOnlyOnKeyJustPressed() {
        return false;
    }

}
