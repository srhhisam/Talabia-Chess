import com.google.common.collect.ImmutableList;
import java.util.*;

//Aida Maisarah Hisam
public class Sun extends Piece {  
    
    //the POSSIBLE move patterns for Sun piece 
    private final static int[] CANDIDATEMOVECOORDINATES = {-8, -7, -6, -1, 1, 6, 7, 8};

    public Sun(final Alliance pieceAlliance, final int piecePosition){
        super(PieceType.SUN, pieceAlliance, piecePosition, true);
    }
    
    public Sun(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove){
        super(PieceType.SUN, pieceAlliance, piecePosition, isFirstMove);
    }
    
    @Override
    public Collection<Move> calcLegalMoves(final Board board){  //calculating the legal moves
        final List<Move> legalMoves = new ArrayList<>();
        
        for (final int currentCandidateOffset : CANDIDATEMOVECOORDINATES){
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset; 
            
            //skipping moving options if has these exclusions
            if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
               isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset)){
                continue;
            }
            
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){  //for as long the destination is within the range
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                 
                if(!candidateDestinationTile.isTileOccupied()){ //if empty, make a normal move
                    legalMoves.add(new Move.NormalMove(board, this, candidateDestinationCoordinate)); 
                } else { //if not empty, 
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); 
                    //check is its the enemy piece
                    if(this.pieceAlliance != pieceAlliance){  //if yes, piece make the attack move
                        legalMoves.add(new Move.AttackingMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            } 
        }
        return ImmutableList.copyOf(legalMoves);
    }
    
    @Override
    public String toString(){
        return PieceType.SUN.toString();
    }
    
    @Override
    public Sun movePiece(final Move move){  //move the piece
        return new Sun(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate()); 
    }
    
    //exclusions if the piece ever reach these positions. 
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRSTCOL[currentPosition] && (candidateOffset == -8 || candidateOffset == -1 || 
                                                        candidateOffset == 6);
    }
    
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTHCOL[currentPosition] && (candidateOffset == -6 || candidateOffset == 1 || 
                                                          candidateOffset == 8);
    }
}