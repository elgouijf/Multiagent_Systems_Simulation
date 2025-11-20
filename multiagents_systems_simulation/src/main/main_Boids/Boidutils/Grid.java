package main.main_Boids.Boidutils;
import main.main_Boids.Boids.*;

import java.util.ArrayList;

@SuppressWarnings("unchecked") // To suppress generic array creation warning (ligne 18)
public class Grid {
    private int screenWidth;
    private int screenHeight;
    private int nCols;
    private int nRows;
    private double cellWidth;
    private double cellHeight;
    private ArrayList<Boid>[][] gridCells;
    private GridType type;


    public int getScreenWidth(){
        return screenWidth;
    }
    public int getScreenHeight(){
        return screenHeight;
    }



    public Grid(int screenWidth, int screenHeight,double cellWidth, double cellHeight, GridType type) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.nCols = (int)Math.ceil((double)screenWidth / cellWidth);
        this.nRows = (int)Math.ceil((double)screenHeight / cellHeight);
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.gridCells = new ArrayList[nCols][nRows];
        this.type = type;
        for (int i = 0; i < nCols; i++) {
            for (int j = 0; j < nRows; j++) {
                this.gridCells[i][j] = new ArrayList<Boid>();
            }
        }
    }

    public Grid(int screenWidth, int screenHeight, double cellSize, GridType type) {
        // Uniform cell size
        this(screenWidth, screenHeight, cellSize, cellSize, type);
    }

    public void addBoid(Boid b){
        double x = b.getPosition().getX();
        double y = b.getPosition().getY();
        int column = Math.min(nCols - 1, Math.max(0, (int)Math.floor(x / this.cellWidth))); // ensure within bounds : boids could get out of screen, and maybe be become negative
        int row = Math.min(nRows - 1, Math.max(0, (int)Math.floor(y / this.cellHeight)));
        this.gridCells[column][row].add(b);
        // Set the cell info in the boid
        if (this.type == GridType.SEPARATION)
            b.setCellSeparation(row, column);
        else
            b.setCellTogether(row, column);
    }

    public void removeBoid(Boid b){
        double x = b.getPosition().getX();
        double y = b.getPosition().getY();
        int column = Math.min(nCols - 1, Math.max(0, (int)Math.floor(x / this.cellWidth)));
        int row = Math.min(nRows - 1, Math.max(0, (int)Math.floor(y / this.cellHeight)));
        this.gridCells[column][row].remove(b);
    }

    public void addBoidsGroup(Boids boids){
        ArrayList<Boid> listeBoids = boids.getlisteBoids();
        for (Boid b : listeBoids) {
            this.addBoid(b);
        }
    }

    public void clearGrid(){
        for (int i = 0; i < nCols; i++) {
            for (int j = 0; j < nRows; j++) {
                this.gridCells[i][j].clear();
            }
        }
    }

    public ArrayList<Boid> getNeighbors(Boid b){
        // Get the cell of the boid
        double x = b.getPosition().getX();
        double y = b.getPosition().getY();
        int column = (int)Math.floor(x/this.cellWidth);
        int row = (int)Math.floor(y/this.cellHeight);
        ArrayList<Boid> neighbors = new ArrayList<Boid>();
        // Get the indexes of the neighboring cells (including the cell itself)
        int topLeftCol = Math.max(0, column - 1);
        int topLeftRow = Math.max(0, row - 1);
        int bottomRightCol = Math.min(this.nCols - 1, column + 1);
        int bottomRightRow = Math.min(this.nRows - 1, row + 1);
        // Iterate through the neighboring cells and collect boids
        for (int i = topLeftCol; i <= bottomRightCol; i++) {
            for (int j = topLeftRow; j <= bottomRightRow; j++) {
                neighbors.addAll(this.gridCells[i][j]);
            }
            }
        // Remove the boid itself from the list
        neighbors.remove(b);
        return neighbors;
    }

    public void updateBoidCell(Boid b) {
        //new cell 
        double x = b.getPosition().getX();
        double y = b.getPosition().getY();
        int newCol = Math.min(nCols - 1, Math.max(0, (int)Math.floor(x / cellWidth)));
        int newRow = Math.min(nRows - 1, Math.max(0, (int)Math.floor(y / cellHeight)));

        // old cell
        int oldColumn;
        int oldRow;
        if (this.type == GridType.SEPARATION) {
            oldColumn = b.getCellColSeparation();
            oldRow = b.getCellRowSeparation();
        } else {
            oldColumn = b.getCellColTogether();
            oldRow = b.getCellRowTogether();
        }

        // If boid changed cell, move it to the new one
        if (oldColumn != newCol || oldRow != newRow) {
            // Safety checks
            if (oldColumn >= 0 && oldColumn < nCols && oldRow >= 0 && oldRow < nRows) {
                this.gridCells[oldColumn][oldRow].remove(b);
            }
            this.gridCells[newCol][newRow].add(b);

            // Update the boid’s recorded cell
            if (this.type == GridType.SEPARATION) {
                b.setCellSeparation(newRow, newCol);
            } else {
                b.setCellTogether(newRow, newCol);
            }
        }
    }
}
