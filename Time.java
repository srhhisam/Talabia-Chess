import com.google.common.collect.ImmutableList;
import java.util.*;

//Aida Maisarah Hisam
public class Time extends Piece {  

    //the POSSIBLE move patterns for Time piece 
    private final static int[] CANDIDATEMOVEVECTORCOORDINATE = {-8, -6, 6, 8};
    
    public Time(final Alliance pieceAlliance, final int piecePosition){
        super(PieceType.TIME, pieceAlliance, piecePosition, true);
    }
    
    public Time(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove){
        super(PieceType.TIME, pieceAlliance, piecePosition, isFirstMove);
    }
    
    @Override
    public Collection<Move> calcLegalMoves(final Board board){
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset : CANDIDATEMOVEVECTORCOORDINATE){
            int candidateDestinationCoordinate = this.piecePosition;
            
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){  //for as long as its within the range
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
                    } else { //if its not empty
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); 
                        //check is its the enemy piece
                        if(this.pieceAlliance != pieceAlliance){ //if yes, piece make an attackmove
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
        return PieceType.TIME.toString();
    }
    
    @Override
    public Time movePiece(final Move move){  //make the move to the destination
        return new Time(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    //exclusions if the piece ever reach these positions. 
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRSTCOL[currentPosition] && (candidateOffset == -8 || candidateOffset == 6);
    }
    
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTHCOL[currentPosition] && (candidateOffset == -6 || candidateOffset == 8);
    }
}