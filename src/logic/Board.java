package logic;

import entities.Entity;
import entities.EntityFactory;
import entities.EntityType;
import entities.Tile;
import graphic.opengl.Grid;
import graphic.opengl.renderer.GridRenderer;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by feinte on 31/05/2015.
 * Responsible of board logic
 * Grid and Tiles handling
 * Entities positioning
 */
public class Board {

    private static int span = 24;
    private final Grid grid;
    private final GridRenderer gridRenderer;
    private final EntityFactory factory;
    private Entity[][] tileMap;
    private int nbTiles = 0;
    private int width, height;

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        grid = new Grid(width, height, span);
        gridRenderer = new GridRenderer(grid.getEntityVertices(0,0), grid.getIndices());
        tileMap = new Tile[width/span][height/span];
        factory = EntityFactory.getEntityFactory();
    }

    public void drawGrid(){
        gridRenderer.drawGrid();
    }

    /**
     * Resize the grid to the new width and height
     * Also generate a new Tilemap with more Tiles
     *
     * @param width
     * @param height
     */
    public void redrawGrid(int width, int height){
        this.width = width;
        this.height = height;
        // TODO generate a bigger Tilemap to avoid crashes when drawing at the edges of the window
        grid.generateGrid(width, height, span);
        gridRenderer.updateVerticesIndices(grid.getEntityVertices(0, 0), grid.getIndices());
        gridRenderer.updateBuffers();

        Entity[][] temp = new Tile[width/span][height/span];
        for(int i = 0; i < tileMap.length && i < temp.length; i++){

            temp[i] = Arrays.copyOf(tileMap[i], height/span);
        }
        tileMap = temp;
    }

    /**
     * Add a new tile to the grid
     * @param color Color of the new tile
     * @param xCoor X position
     * @param yCoor Y position
     */
    public void addTile(Color color, float xCoor, float yCoor) {
        Entity tile = factory.createEntity(EntityType.TILE, color, xCoor, yCoor);

        int xPos = (int)tile.getX();
        int yPos = (int)tile.getY();
        if(xPos > width || xPos < 0 || yPos > height || yPos < 0)return;
        int x, y;
        int halfSpan = span/2;
        if(xPos%span>=halfSpan)xPos-=halfSpan;
        else xPos+=halfSpan;
        if(yPos%span>=halfSpan)yPos-=halfSpan;
        else yPos+=halfSpan;
        // Round to the inferior
        x = xPos / span;
        y = yPos / span;
        xPos = (x*span) + halfSpan;
        yPos = (y*span) + halfSpan;
        if(tileMap[x][y] == null){
            tile.setX(xPos);
            tile.setY(yPos);
            tileMap[x][y] = tile;
            nbTiles++;
        }
    }

    public void removeTile(int xPos, int yPos){

        if(xPos > width || xPos < 0 || yPos > height || yPos < 0)return;
        int halfSpan = span/2;
        if(xPos%span>=halfSpan)xPos-=halfSpan;
        else xPos+=halfSpan;
        if(yPos%span>=halfSpan)yPos-=halfSpan;
        else yPos+=halfSpan;

        if (tileMap[xPos / span][yPos / span] != null) {
            tileMap[xPos / span][yPos / span] = null;
            nbTiles--;
        }
    }

    public void resize(int width, int height){
        this.width = width;
        this.height = height;
        redrawGrid(width, height);
    }

    public Entity[][] getTileMap(){
        return tileMap;
    }

    public int getNbTiles() {
        return nbTiles;
    }
}
