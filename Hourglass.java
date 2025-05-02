import com.google.common.collect.ImmutableList;
import java.util.*; 

//Aida Maisarah Hisam
public class Hourglass extends Piece {  
    
    //the POSSIBLE move patterns on the board for Hourglass piece
    private final static int[] CANDIDATEMOVECOORDINATES = { -15, -13, -9, -5, 5, 9, 13, 15};
    
    public Hourglass(final Alliance pieceAlliance, final int piecePosition){
        super(PieceType.HOURGLASS, pieceAlliance, piecePosition, true);
    }
    
    public Hourglass(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove){
        super(PieceType.HOURGLASS, pieceAlliance, piecePosition, isFirstMove);
    }
    
    @Override
    public Collection<Move> calcLegalMoves(final Board board){ // calculating all possible moves that can be made
        final List<Move> legalMoves = new ArrayList<>();
        
        for (final int currentCandidateOffset : CANDIDATEMOVECOORDINATES){
            final  int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){ //for as long as the coordinate is within range
                //if it has these exclusions, skip the moving options. 
                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || 
                   isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                   isSixthColumnExclusion(this.piecePosition, currentCandidateOffset) || 
                   isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }
                
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                
                if(!candidateDestinationTile.isTileOccupied()){  //if the tile is empty, piece make a normal move. 
                    legalMoves.add(new Move.NormalMove(board, this, candidateDestinationCoordinate));
                } else { //if its not empty
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); 
                    //check for the color, if its enemy piece, piece will make an attackmove. 
                    if(this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new Move.AttackingMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    
    @Override
    public String toString(){
        return PieceType.HOURGLASS.toString();
    }
    
    @Override
    public Hourglass movePiece(final Move move){ //move the piece to the destination.
        return new Hourglass(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    //exclusions if the piece ever reach these positions. 
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRSTCOL[currentPosition] && (candidateOffset == -15 || candidateOffset == -9 || 
                                                        candidateOffset == 5 || candidateOffset == 13);
    }
    
    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SECONDCOL[currentPosition] && (candidateOffset == -9 || candidateOffset == -5);
    }
    
    private static boolean isSixthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SIXTHCOL[currentPosition] && (candidateOffset == -5 || candidateOffset == 9);
    }
    
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTHCOL[currentPosition] && (candidateOffset == -13 || candidateOffset == -5 || 
                                                          candidateOffset == 9 || candidateOffset == 15);
    }
}