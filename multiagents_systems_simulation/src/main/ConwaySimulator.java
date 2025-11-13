package main;
import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;

import java.awt.Color;

public class ConwaySimulator implements Simulable  {
    int h , w ; /* height and width of the grid */
    ConwayAndImmigration conway ; /* the conway instance itself */
    int pix ; /* size of each cell in pixels */
    GUISimulator guiS ; /* the GUI simulator */

    public ConwaySimulator ( int height , int width , int[][] grid , int pixelsize, GUISimulator guiS ){
        this.h = height ; 
        this.w = width ; 
        this.conway = new ConwayAndImmigration(height, width, 2, grid) ; 
        this.pix = pixelsize ;
        this.guiS = guiS ;
        this.reDisplay() ;
    }
    
    public void reDisplay (){
        this.guiS.reset(); 
        int[][] grid = this.conway.getgrid() ;
        for ( int i = 0 ; i < h ; i++ ){
            for ( int j = 0 ; j < w ; j++ ){
                if ( grid[i][j]==1 ){ // cell is alive
                    this.guiS.addGraphicalElement( new Rectangle(i * pix + pix / 2, j * pix + pix / 2, Color.decode("#1f77b4"), Color.decode("#1f77b4"), pix) );
                }
                else {
                    this.guiS.addGraphicalElement( new Rectangle(i * pix + pix / 2, j * pix + pix / 2, Color.WHITE, Color.WHITE, pix) );
                }
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
