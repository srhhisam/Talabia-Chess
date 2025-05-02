import com.google.common.collect.ImmutableList;
import java.util.*;

//Aida Maisarah Hisam
public class Plus extends Piece {  
    
    //the POSSIBLE move patterns for Plus piece 
    private final static int[] CANDIDATEMOVEVECTORCOORDINATE = {-7, -1, 1, 7};

    public Plus(final Alliance pieceAlliance, final int piecePosition){
        super(PieceType.PLUS, pieceAlliance, piecePosition, true); 
    }
    
    public Plus(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove){
        super(PieceType.PLUS, pieceAlliance, piecePosition, isFirstMove);
    }
    
    @Override
    public Collection<Move> calcLegalMoves(final Board board){   //calculating Plus legal Moves
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset : CANDIDATEMOVEVECTORCOORDINATE){
            int candidateDestinationCoordinate = this.piecePosition;
            
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){ //for as long as the coordinate is within range
                //if it has these exclusions, skip the moving options. 
                if(isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset) ||
                   isSeventhColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset)){
                       break;
                }
                
                candidateDestinationCoordinate += currentCandidateOffset;
                
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                
                    if(!candidateDestinationTile.isTileOccupied()){ //if the tile is empty, piece make a normal move
                        legalMoves.add(new Move.NormalMove(board, this, candidateDestinationCoordinate));
                    } else { //if it is not empty
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); 
                        //check if its an enemy piece
                        if(this.pieceAlliance != pieceAlliance){ //if its an enemy piece, piece will make an attack move
                            legalMoves.add(new Move.AttackingMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    
    @Override
    public String toString(){
        return PieceType.PLUS.toString();
    }
    
    @Override
    public Plus movePiece(final Move move){  //make the move to the destination 
        return new Plus(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    //exclusions if the piece ever reach these positions. 
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRSTCOL[currentPosition] && (candidateOffset == -1);
    }
    
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTHCOL[currentPosition] && (candidateOffset == 1);
    }
}