package logic.commands;

/**
 * Created by feinte on 17/06/2015.
 *
 */
public abstract class KeyboardCommand implements Command {

    public abstract void execute();

    @Override
    public abstract boolean isOnlyOnKeyJustPressed();
}
