package main;

public class Conway {
    private int h , w ; /* height and width of the grid */
    private boolean[][] curgrid ; /* the current grid itself */
    private boolean[][] initgrid ; /* the intial grid  */

    public  Conway(int height , int width , boolean[][] initg ){
        this.h = height ; 
        this.w = width ;
        this.initgrid = new boolean[height][width] ;
        this.curgrid = new boolean[height][width] ;
        for ( int i = 0 ; i < height ; i++ ){
            for ( int j = 0 ; j < width ; j++ ){

                this.initgrid[i][j] = initg[i][j] ;
                this.curgrid[i][j] = initg[i][j] ;
            }
    }
    }

    public int n_neighbors (int x , int y ){ /* returns the number of alive neighbors of cell (x,y) */
        int count = 0 ; 
        for ( int i = -1 ; i<= 1 ; i++ ){
            for ( int j =-1 ; j<=1 ; j++ ){
                if ( i==j && i==0 ) continue ; // ignore the cell itself
                if ( curgrid[(x+i +h)%h][(y+j +w)%w]) count +=1;
            }
        }
        return count ;
    }

    public void update_grid (){
        boolean[][] newgrid = new boolean[h][w] ;
        for ( int i = 0 ; i < h ; i++ ){
            for ( int j = 0 ; j < w ; j++ ){
                int n = n_neighbors(i,j) ;
                if ( curgrid[i][j] ){ // cell is alive
                    if ( n == 2 || n == 3 ) newgrid[i][j] = true ;
                    else newgrid[i][j] = false ;
                } else { // cell is dead
                    if ( n == 3 ) newgrid[i][j] = true ;
                    else newgrid[i][j] = false ;
                }
            }
        }
        this.curgrid = newgrid ;
    }
    public boolean[][] getgrid (){
        return this.curgrid ;
    }
    
    public void reInit (){
        for ( int i = 0 ; i < h ; i++ ){
            for ( int j = 0 ; j < w ; j++ ){
                this.curgrid[i][j] = this.initgrid[i][j] ;
            }
        }
    }
}