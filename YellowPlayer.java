import java.util.*;

//Sharleen Ravi Mahendra
public class YellowPlayer extends Player{
    
    public YellowPlayer(final Board board, final Collection<Move> yellowLegalMoves, final Collection<Move> blueLegalMoves){
        super(board, yellowLegalMoves, blueLegalMoves);    //from Player, it takes board, player legal moves, opponent legal moves
    }                                                      //so yellowLegalMoves is the this player's legal move and blue is the opponent
     
    @Override 
    public Collection<Piece> getActivePieces() {
        return this.board.getYellowPieces();  //get the yellow player's active pieces
    } 
    @Override
    public Alliance getAlliance(){
        return Alliance.YELLOW;            //this player is the yellow player
    }
    @Override
    public Player getOpponent(){
        return this.board.bluePlayer();   //return the blue player as the opponent
    }
}