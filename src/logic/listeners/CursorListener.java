package logic.listeners;

/**
 * Created by feinte on 18/06/2015.
 * Listener interface responsible for notifying
 * cursor position to registered classes
 */
public interface CursorListener {

    /**
     * Update the cursor position
     * @param xPos X coordinate
     * @param yPos Y coordinate
     */
    void update(int xPos, int yPos);
}
