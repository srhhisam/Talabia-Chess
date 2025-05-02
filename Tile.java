import com.google.common.collect.ImmutableMap;
import java.util.*;

//Maliny A/P Thanaraj, Nor Aliah Syuhaidah
public abstract class Tile{     
    protected final int tileCoordinate;
    private static final Map<Integer, EmptyTile> EMPTYTILE = createAllPossibleEmptyTiles();
    
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() { //creating the tiles for the board
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        
        for(int i = 0; i < BoardUtils.NUMTILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }
    
    //if piece not null, returns occupied tile, if null returns empty tile
    public static Tile createTile(final int tileCoordinate, final Piece piece){  
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTYTILE.get(tileCoordinate);
    }   
    
    private Tile(final int tileCoordinate){  //constructer for the tile coordinate
        this.tileCoordinate = tileCoordinate;
    }
    
    public abstract boolean isTileOccupied(); //check if that tile is occupied 
    
    public abstract Piece getPiece();         //get the piece 
   
    public int getTileCoordinate(){         //get the tile coordinate
        return this.tileCoordinate;   
    }
    
    public static final class EmptyTile extends Tile {
        private EmptyTile(final  int coordinate){
            super(coordinate);
        }
        @Override
        public String toString(){   //toString method for printing
            return "-";  
        }
        @Override 
        public boolean isTileOccupied(){  //tile is empty, hence return false
            return false;  
        }
        @Override
        public Piece getPiece(){        //tile is empty means no piece on it, returns null
            return null;  
        }
    }
    
    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;
        
        private OccupiedTile(int tileCoordinate, final Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }
        @Override
        public String toString(){  //if its blue piece, print in lowcase, yellow piece is in capslock
            return getPiece().getPieceAlliance().isBlue() ? getPiece().toString().toLowerCase() :
                   getPiece().toString();
        }
        @Override
        public boolean isTileOccupied() {    //return true because tile is occupied
            return true; 
        }
        @Override
        public Piece getPiece(){            //get the piece on that occupied tile
            return this.pieceOnTile; 
        }
    }
}