package logic.commands;

import entities.Entity;
import logic.Board;

/**
 * Created by feinte on 05/06/2015.
 * Command to remove a tile from the board
 */
public class RemoveTileCommand extends MouseCommand {

    private Entity entity;
    private Board board;

    public RemoveTileCommand(Entity entity, Board board){
        this.entity = entity;
        this.board = board;
    }

    @Override
    public void execute(int xPos, int yPos) {
        board.removeTile(xPos, yPos);
    }
}
