import com.google.common.collect.ImmutableList;
import java.util.*;

//Sharleen Ravi Mahendra, Nor Aliah Syuhaidah, Maliny A/P Thanaraj
public abstract class Player{       //abstract class for the players
    
    protected final Board board;
    protected final Sun playerSun;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    
    public Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves){
        this.board = board;
        this.playerSun = makeSun();
        this.legalMoves = legalMoves;
        this.isInCheck = !Player.calcAttacksOnTile(this.playerSun.getPiecePosition(), opponentMoves).isEmpty(); 
    }
    
    public Sun getPlayerSun(){     //return the sun player
        return this.playerSun; 
    }
    
    //return all possible legal moves of pieces
    public Collection<Move> getLegalMoves(){ 
        return this.legalMoves;  
    }
    
    //calculate the attack that is possible from the piece's legal moves and postion
    protected static Collection<Move> calcAttacksOnTile(int piecePosition, Collection<Move> moves){
        final List<Move> attackMoves = new ArrayList<>();
    
        for(final Move move : moves){ //if the piece go to the destination where there is an enemy piece, attack moves happen
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }
    
    private Sun makeSun(){
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType().isSun()){
                return (Sun) piece; 
            }
        }
        throw new RuntimeException("aikkkkk");  //by fault, the game needs both players' sun to run 
    }
    
    public boolean isMoveLegal(Move move){     //checking if the move made by the piece is following their legal moves
        return this.legalMoves.contains(move); 
    }
    
    public boolean isInCheck(){     //same in chess, when the king is in check, means that the king is exposed to a danger and need to move
        return this.isInCheck;      //to another tile, but in this task, rather than blocking other pieces to move just so you can protect 
    }                               //the sun, we will just inform that "oh the sun is in danger" and you choose your own destiny :)
 
    public boolean isInStaleMate(){  //the sun is not in check, but there is no legalmoves can be made by the sun to escape because any move
                                     //that is made will result in exposing the sun to danger, hence it will be a stalemate(draw).
        return !this.isInCheck && !hasEscapeMoves();
    }
    
    public boolean isCaptured(){    //in chess, the game ends with several reasons and the most solid one is because the player checkmate
        return false;               //the opponent's king, but in this task, instead of checkmate the sun, we need to capture it to win.  
    }
    
    //checking if the piece has any escape move to escape from the possible attacks
    protected boolean hasEscapeMoves(){  
        for(final Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }
    
    //handling where the movement is happening
    public MoveTransition makeMove(final Move move){
        if(!isMoveLegal(move)){             //if the move made by the piece is not legal, returns illegal move
            return new MoveTransition(this.board, move, MoveStatus.ILLEGALMOVE);  
        }
        
        final Board transitionBoard = move.execute(); 
        final Collection<Move> SunAttacks = Player.calcAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerSun().getPiecePosition(),
                                                                     transitionBoard.currentPlayer().getLegalMoves()); 
                                                                     
        if(!SunAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVESPLAYERINCHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE); 
    }
    
    public abstract Collection<Piece> getActivePieces();   //get active pieces from both players 
    public abstract Alliance getAlliance();                //get the color for both players
    public abstract Player getOpponent();                  //get the players' respective opponent
}