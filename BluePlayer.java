import java.util.*;

//Sharleen Ravi Mahendra 
public class BluePlayer extends Player{   
    
    public BluePlayer(final Board board, final Collection<Move> yellowLegalMoves, final Collection<Move> blueLegalMoves){
        super(board, blueLegalMoves, yellowLegalMoves);   //from Player, it takes board, player legal moves, opponent legal moves
    }                                                     //so blueLegalMoves is the this player's legal move and yellow is the opponent's
    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBluePieces();     //get the blue player's active pieces
    }
    @Override
    public Alliance getAlliance(){
        return Alliance.BLUE;                  //this player is blue player
    }
    @Override
    public Player getOpponent(){
        return this.board.yellowPlayer();      //yellow player is the oppponent 
    }
}