import com.google.common.collect.ImmutableList;
import java.util.*;

//Aida Maisarah Hisam
public class Point extends Piece {  

    //the POSSIBLE move patterns for Point piece 
    private final static int[] CANDIDATEMOVECOORDINATES = {7, 14};
    
    public Point(final Alliance pieceAlliance, final int piecePosition){
        super(PieceType.POINT, pieceAlliance, piecePosition, true);
    }
    
    public Point(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove){
        super(PieceType.POINT, pieceAlliance, piecePosition, isFirstMove);
    }
    
    @Override
    public Collection<Move> calcLegalMoves(final Board board){
        final List<Move> legalMoves = new ArrayList<>();
        
        for (final int currentCandidateOffset : CANDIDATEMOVECOORDINATES){
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset); 
            
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            
            if(currentCandidateOffset == 7 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                //supposedly also where the point is reaching the edge and turning
                //make a normal move is the tile is empty
                legalMoves.add(new Move.NormalMove(board, this, candidateDestinationCoordinate));
            } else if (currentCandidateOffset == 14 && this.isFirstMove() && 
                      ((BoardUtils.FIFTHRANK[this.piecePosition] && this.getPieceAlliance().isBlue()) ||
                      (BoardUtils.SECONDRANK[this.piecePosition] && this.getPieceAlliance().isYellow()))){
                      final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 7);
                      if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                         !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                         legalMoves.add(new Move.PointJump(board, this, candidateDestinationCoordinate));
                      } else if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                                 board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                                 final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                                 legalMoves.add(new Move.PointAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)); 
                      }
            } else if (currentCandidateOffset == 7){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){ 
                      legalMoves.add(new Move.PointAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    
    @Override
    public String toString(){
        return PieceType.POINT.toString();
    }
    
    @Override
    public Point movePiece(final Move move){ //move the piece
        return new Point(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate()); 
    }
}