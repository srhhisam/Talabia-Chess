//Nor Aliah Syuhaidah, Maliny A/P Thanaraj
public class BoardUtils {   
    public static final boolean[] FIRSTCOL = initColumn(0); 
    public static final boolean[] SECONDCOL = initColumn(1); 
    public static final boolean[] SIXTHCOL = initColumn(5); 
    public static final boolean[] SEVENTHCOL = initColumn(6); 
    
    public static final boolean[] SIXTHRANK = initRow(0);   //in chess we call it rank, which the tiles on the white side
    public static final boolean[] FIFTHRANK = initRow(7);   //is the first rank and we go up. in this task, its the yellow's. 
    public static final boolean[] FOURTHRANK = initRow(14);
    public static final boolean[] THIRDRANK = initRow(21);
    public static final boolean[] SECONDRANK = initRow(28);
    public static final boolean[] FIRSTRANK = initRow(35); 
    
    public static final int NUMTILES = 42; //initialise the total tiles on the board
    public static final int NUMROWS = 7;   //initialise how many tiles are there on each row. 
    
    private BoardUtils(){
        throw new RuntimeException("you cannot!!");
    }
    
    private static boolean[] initColumn(int columnNumber){ //making the columns
        final boolean[] column = new boolean[NUMTILES];
        do{
            column[columnNumber] = true;
            columnNumber += NUMROWS;
        } while(columnNumber < NUMTILES);
        return column;
    }
    
    public static boolean[] initRow(int rowNumber){  //making the rows
        final boolean[] row = new boolean[NUMTILES];
        do{
            row[rowNumber] = true;
            rowNumber++;
        } while(rowNumber % NUMROWS != 0);
        return row;
    }
    
    public static boolean isValidTileCoordinate(final int coordinate){ //making sure the tile is within range
        return coordinate >= 0 && coordinate < NUMTILES; 
    }
}