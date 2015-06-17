package logic;

import entities.Entity;
import entities.Tile;
import graphic.opengl.Grid;
import graphic.opengl.Primitive;
import graphic.opengl.Square;
import graphic.opengl.renderer.GridRenderer;
import graphic.opengl.renderer.PrimitiveRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by feinte on 31/05/2015.
 * Responsible of board logic
 * Grid and Tiles handling
 * Entities positioning
 */
public class Board {

    private Grid grid;
    private GridRenderer gridRenderer;
    private PrimitiveRenderer primitiveRenderer;
    private static int span = 24;
    private Entity[][] tileMap;
    private int width, height;

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        grid = new Grid(width, height, span);
        gridRenderer = new GridRenderer(grid.getEntityVertices(0,0), grid.getIndices());
        primitiveRenderer = new PrimitiveRenderer();
        tileMap = new Tile[width/span][height/span];
    }

    public void drawGrid(){
        gridRenderer.drawGrid();
    }

    public void redrawGrid(int width, int height){
        this.width = width;
        this.height = height;
        grid.generateGrid(width, height, span);
        gridRenderer.updateVerticesIndices(grid.getEntityVertices(0, 0), grid.getIndices());
        gridRenderer.updateBuffers();
        Entity[][] temp = new Tile[width/span][height/span];
        for(int i = 0; i < tileMap.length && i < temp.length; i++){

            temp[i] = Arrays.copyOf(tileMap[i], height/span);
            /*for(int j = 0; j < tileMap[0].length; j++) {
                temp[i][j] = tileMap[i][j];
            }*/
        }
        tileMap = temp;
    }

    public void addSquare(int xPos, int yPos, Color color){
        // check bounds
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
            ArrayList<Primitive> primitives = new ArrayList<Primitive>(1);
            primitives.add(new Square((float)halfSpan));
            tileMap[x][y] = new Tile(color, xPos, yPos, primitives);
        }
    }

    public void addTile(Entity tile){

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
        }
    }

    public void deleteSquare(int xPos, int yPos){

        // check bounds
        if(xPos > width || xPos < 0 || yPos > height || yPos < 0)return;
        int halfSpan = span/2;
        if(xPos%span>=halfSpan)xPos-=halfSpan;
        else xPos+=halfSpan;
        if(yPos%span>=halfSpan)yPos-=halfSpan;
        else yPos+=halfSpan;

        tileMap[xPos/span][yPos/span] = null;

    }

    public void removeTile(int xPos, int yPos){

        if(xPos > width || xPos < 0 || yPos > height || yPos < 0)return;
        int halfSpan = span/2;
        if(xPos%span>=halfSpan)xPos-=halfSpan;
        else xPos+=halfSpan;
        if(yPos%span>=halfSpan)yPos-=halfSpan;
        else yPos+=halfSpan;

        tileMap[xPos/span][yPos/span] = null;
    }

    public void resize(int width, int height){
        this.width = width;
        this.height = height;
        redrawGrid(width, height);
    }
    public Entity[][] getTileMap(){
        return tileMap;
    }


}
