package main;
import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;


import java.awt.Color;

public class ImmigrationSimulator implements Simulable  {
    int h , w , n ; /* height and width of the grid */
    ConwayAndImmigration conway ; /* the conway instance itself */
    int pix ; /* size of each cell in pixels */
    GUISimulator guiS ; /* the GUI simulator */

    public ImmigrationSimulator ( int height , int width , int n, int[][] grid , int pixelsize, GUISimulator guiS ){
        this.n = n ;
        this.h = height ; 
        this.w = width ; 
        this.conway = new ConwayAndImmigration(height, width,n, grid) ; 
        this.pix = pixelsize ;
        this.guiS = guiS ;
        this.reDisplay() ;
    }
    
    public void reDisplay() {
    this.guiS.reset();
    int[][] grid = this.conway.getgrid();

    for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
            Color color;
            switch (grid[i][j]) {
                case 1:
                    color = Color.decode("#c1c4d5"); // gris clair
                    break;
                case 2:
                    color = Color.decode("#3c3c3e"); // gris foncé
                    break;
                case 3:
                    color = Color.BLACK;
                    break;
                default:
                    color = Color.WHITE;
            }
            this.guiS.addGraphicalElement(
                new Rectangle(j * pix + pix / 2, i * pix + pix / 2, color, color, pix)
            );
        }
    }
}

    @Override
    public void next (){
        this.conway.update_grid() ;
        this.reDisplay() ;
    }
    @Override
    public void restart (){
        this.conway.reInit() ;
        this.reDisplay() ;
    }
    
}