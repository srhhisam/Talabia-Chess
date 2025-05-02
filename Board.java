import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables; 
import java.util.*;

//Aida Maisarah Hisam
public class Board {
    private final List<Tile> gameBoard;
    private final Collection<Piece> yellowPieces;
    private final Collection<Piece> bluePieces;
    
    private final YellowPlayer yellowPlayer;
    private final BluePlayer bluePlayer;
    private final Player currentPlayer;
    
    //immutable board, for when the builder has construct the board, the board cant be change. 
    private Board(final Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.yellowPieces = calcActivePieces(this.gameBoard, Alliance.YELLOW);
        this.bluePieces = calcActivePieces(this.gameBoard, Alliance.BLUE);
        
        final Collection<Move> yellowLegalMoves = calcLegalMoves(this.yellowPieces); 
        final Collection<Move> blueLegalMoves = calcLegalMoves(this.bluePieces); 
        
        this.yellowPlayer = new YellowPlayer(this, yellowLegalMoves, blueLegalMoves); 
        this.bluePlayer = new BluePlayer(this, yellowLegalMoves, blueLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.yellowPlayer, this.bluePlayer); 
    }
    
    @Override 
    public String toString(){  //using toString methods to print the initial board conditions 
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BoardUtils.NUMTILES; i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i + 1) % BoardUtils.NUMROWS == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    
    //returning the current player
    public Player currentPlayer(){
        return this.currentPlayer;
    }
    
    //returning the yellow player
    public Player yellowPlayer(){
        return this.yellowPlayer; 
    }
    
    //returning the blue player
    public Player bluePlayer(){
        return this.bluePlayer; 
    }
    
    //returning the yellow pieces
    public Collection<Piece> getYellowPieces(){
        return this.yellowPieces;
    }
    
    //returning the blue pieces
    public Collection<Piece> getBluePieces(){
        return this.bluePieces;
    }
    
    //collection of the legalmoves
    private Collection<Move> calcLegalMoves(final Collection<Piece> pieces){
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final Piece piece : pieces){
            legalMoves.addAll(piece.calcLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }
    
    //collection of all the active pieces
    private static Collection<Piece> calcActivePieces(final List<Tile> gameBoard, final Alliance alliance ){
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile : gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }
    
    //returning the tile coordinate
    public Tile getTile(final int tileCoordinate){ 
        return gameBoard.get(tileCoordinate); 
    }
    
    //creating the board with 42 tiles. 
    private static List<Tile> createGameBoard(final Builder builder){  
        final Tile[] tiles = new Tile[BoardUtils.NUMTILES];
        for (int i = 0; i < BoardUtils.NUMTILES; i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }
    
    //using the buillder to construct the talabia board together with the pieces initial placements
    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        
        //BLUE
        builder.setPiece(new Plus(Alliance.BLUE, 0));
        builder.setPiece(new Hourglass(Alliance.BLUE, 1));
        builder.setPiece(new Time(Alliance.BLUE, 2));
        builder.setPiece(new Sun(Alliance.BLUE, 3));
        builder.setPiece(new Time(Alliance.BLUE, 4));
        builder.setPiece(new Hourglass(Alliance.BLUE, 5));
        builder.setPiece(new Plus(Alliance.BLUE, 6));
        builder.setPiece(new Point(Alliance.BLUE, 7));
        builder.setPiece(new Point(Alliance.BLUE, 8));
        builder.setPiece(new Point(Alliance.BLUE, 9));
        builder.setPiece(new Point(Alliance.BLUE, 10));
        builder.setPiece(new Point(Alliance.BLUE, 11));
        builder.setPiece(new Point(Alliance.BLUE, 12));
        builder.setPiece(new Point(Alliance.BLUE, 13));
        
        //YELLOW
        builder.setPiece(new Point(Alliance.YELLOW, 28));
        builder.setPiece(new Point(Alliance.YELLOW, 29));
        builder.setPiece(new Point(Alliance.YELLOW, 30));
        builder.setPiece(new Point(Alliance.YELLOW, 31));
        builder.setPiece(new Point(Alliance.YELLOW, 32));
        builder.setPiece(new Point(Alliance.YELLOW, 33));
        builder.setPiece(new Point(Alliance.YELLOW, 34));
        builder.setPiece(new Plus(Alliance.YELLOW, 35));
        builder.setPiece(new Hourglass(Alliance.YELLOW, 36));
        builder.setPiece(new Time(Alliance.YELLOW, 37));
        builder.setPiece(new Sun(Alliance.YELLOW, 38));
        builder.setPiece(new Time(Alliance.YELLOW, 39));
        builder.setPiece(new Hourglass(Alliance.YELLOW, 40));
        builder.setPiece(new Plus(Alliance.YELLOW, 41));
        
        //by fault, yellow will move first.
        builder.setMoveMaker(Alliance.YELLOW);
        return builder.build();
    }
    
    //concatting both yellow's and blue's all legal moves to return all legal moves
    public Iterable<Move> getAllLegalMoves(){  
        return Iterables.unmodifiableIterable(Iterables.concat(this.yellowPlayer.getLegalMoves(), this.bluePlayer.getLegalMoves()));  
    }
    
    //Builder design pattern to contruct the board
    public static class Builder {
        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        
        public Builder(){
            this.boardConfig = new HashMap<>();
        }
        
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        
        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this; 
        }

        public Board build(){
            return new Board(this);
        }
    }
}