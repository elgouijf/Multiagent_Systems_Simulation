package main;
import main.Boids.*;

import java.util.ArrayList;

@SuppressWarnings("unchecked") // To suppress generic array creation warning (ligne 18)
public class Grid {
    private int screen_width;
    private int screen_height;
    private int n_cols;
    private int n_rows;
    private double cell_width;
    private double cell_height;
    private ArrayList<Boid>[][] grid_cells;
    private GridType type;


    public Grid(int screen_width, int screen_height,double cell_width, double cell_height, GridType type) {
        this.screen_width = screen_width;
        this.screen_height = screen_height;
        this.n_cols = (int)Math.ceil((double)screen_width / cell_width);
        this.n_rows = (int)Math.ceil((double)screen_height / cell_height);
        this.cell_width = cell_width;
        this.cell_height = cell_height;
        this.grid_cells = new ArrayList[n_cols][n_rows];
        this.type = type;
        for (int i = 0; i < n_cols; i++) {
            for (int j = 0; j < n_rows; j++) {
                this.grid_cells[i][j] = new ArrayList<Boid>();
            }
        }
    }

    public Grid(int screen_width, int screen_height, double cell_size, GridType type) {
        // Uniform cell size
        this(screen_width, screen_height, cell_size, cell_size, type);
    }

    public void addBoid(Boid b){
        double x = b.getPosition().getX();
        double y = b.getPosition().getY();
        int column = Math.min(n_cols - 1, Math.max(0, (int)Math.floor(x / this.cell_width))); // ensure within bounds : boids could get out of screen, and maybe be become negative
        int row = Math.min(n_rows - 1, Math.max(0, (int)Math.floor(y / this.cell_height)));
        this.grid_cells[column][row].add(b);
        // Set the cell info in the boid
        if (this.type == GridType.SEPARATION)
            b.setCellSeparation(row, column);
        else
            b.setCellTogether(row, column);
    }

    public void removeBoid(Boid b){
        double x = b.getPosition().getX();
        double y = b.getPosition().getY();
        int column = Math.min(n_cols - 1, Math.max(0, (int)Math.floor(x / this.cell_width)));
        int row = Math.min(n_rows - 1, Math.max(0, (int)Math.floor(y / this.cell_height)));
        this.grid_cells[column][row].remove(b);
    }

    public void addBoidsGroup(Boids boids){
        ArrayList<Boid> listeBoids = boids.getlisteBoids();
        for (Boid b : listeBoids) {
            this.addBoid(b);
        }
    }

    public void clearGrid(){
        for (int i = 0; i < n_cols; i++) {
            for (int j = 0; j < n_rows; j++) {
                this.grid_cells[i][j].clear();
            }
        }
    }

    public ArrayList<Boid> getNeighbors(Boid b){
        // Get the cell of the boid
        double x = b.getPosition().getX();
        double y = b.getPosition().getY();
        int column = (int)Math.floor(x/this.cell_width);
        int row = (int)Math.floor(y/this.cell_height);
        ArrayList<Boid> neighbors = new ArrayList<Boid>();
        // Get the indexes of the neighboring cells (including the cell itself)
        int top_left_col = Math.max(0, column - 1);
        int top_left_row = Math.max(0, row - 1);
        int bottom_right_col = Math.min(this.n_cols - 1, column + 1);
        int bottom_right_row = Math.min(this.n_rows - 1, row + 1);
        // Iterate through the neighboring cells and collect boids
        for (int i = top_left_col; i <= bottom_right_col; i++) {
            for (int j = top_left_row; j <= bottom_right_row; j++) {
                neighbors.addAll(this.grid_cells[i][j]);
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
        int newCol = Math.min(n_cols - 1, Math.max(0, (int)Math.floor(x / cell_width)));
        int newRow = Math.min(n_rows - 1, Math.max(0, (int)Math.floor(y / cell_height)));

        // old cell
        int old_column;
        int old_row;
        if (this.type == GridType.SEPARATION) {
            old_column = b.getCellColSeparation();
            old_row = b.getCellRowSeparation();
        } else {
            old_column = b.getCellColTogether();
            old_row = b.getCellRowTogether();
        }

        // If boid changed cell, move it to the new one
        if (old_column != newCol || old_row != newRow) {
            // Safety checks
            if (old_column >= 0 && old_column < n_cols && old_row >= 0 && old_row < n_rows) {
                this.grid_cells[old_column][old_row].remove(b);
            }
            this.grid_cells[newCol][newRow].add(b);

            // Update the boid’s recorded cell
            if (this.type == GridType.SEPARATION) {
                b.setCellSeparation(newRow, newCol);
            } else {
                b.setCellTogether(newRow, newCol);
            }
        }
    }
}
