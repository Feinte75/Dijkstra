package logic.commands;

import logic.Board;

/**
 * Created by feinte on 05/06/2015.
 * Command to remove a tile from the board
 */
public class RemoveTileCommand extends MouseCommand {

    private Board board;

    public RemoveTileCommand(Board board) {
        this.board = board;
    }

    @Override
    public void execute() {
        board.removeTile(xPos, yPos);
    }

    @Override
    public boolean isOnlyOnKeyJustPressed() {
        return false;
    }
}
