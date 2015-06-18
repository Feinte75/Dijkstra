package logic.commands;

import logic.listeners.CursorListener;

/**
 * Created by feinte on 05/06/2015.
 *
 */
public abstract class MouseCommand implements Command, CursorListener{

    protected int xPos, yPos;

    public MouseCommand(){

    }

    public abstract void execute();

    @Override
    public void update(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
