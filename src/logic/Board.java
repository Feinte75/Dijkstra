package logic;

import graphic.opengl.Grid;
import graphic.opengl.Square;
import graphic.opengl.renderer.GridRenderer;
import graphic.opengl.renderer.PrimitiveRenderer;

import java.awt.*;

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

    public Board(int width, int height){
        grid = new Grid(width, height, 50);
        gridRenderer = new GridRenderer(grid.getEntityVertices(0,0), grid.getIndices());
        primitiveRenderer = new PrimitiveRenderer();
    }

    public void update(){
        gridRenderer.drawGrid();
    }

    public void redrawGrid(int width, int height){
        grid.generateGrid(width, height, 50);
        gridRenderer.updateVerticesIndices(grid.getEntityVertices(0 , 0), grid.getIndices());
        gridRenderer.updateBuffers();
    }

    public void drawSquare(int x, int y){
        primitiveRenderer.drawPrimitive(new Square(50), Color.GRAY, x, y);
    }


}
