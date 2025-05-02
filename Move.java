import java.util.*; 

//Aida Maisarah Hisam, Farah Kamila Yahya
public abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate; 
    protected final boolean isFirstMove;
    
    public static final Move NULLMOVE = new NullMove(); 
    
    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove(); 
    }
    
    private Move(final Board board, final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }
    
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }
    
    @Override
    public boolean equals(final Object other){
        if(this == other) {
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
               getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
               getMovedPiece().equals(otherMove.getMovedPiece()); 
    }
    
    //return the piece current coordinate
    public int getCurrentCoordinate(){ 
        return this.getMovedPiece().getPiecePosition();
    }
    
    //return where the piece is going
    public int getDestinationCoordinate(){ 
        return this.destinationCoordinate; 
    }
    
    //the moved piece
    public Piece getMovedPiece(){ 
        return this.movedPiece;  
    }
    
    //check if the piece is under attack
    public boolean isAttack(){ 
        return false; 
    }
    
    //if the piece is attacked, return the attacked piece
    public Piece getAttackedPiece(){ 
        return null;
    }
         
    //the board has the Builder to construct new board to place the newly position pieces after moving. 
    public Board execute(){
            final Board.Builder builder = new Board.Builder();
             
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece); 
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
    }
    
    //where attacking moves happen
    public static class AttackingMove extends AttackMove{
        public AttackingMove(final Board board, final Piece pieceMoved, final int destinationCoordinate, final Piece pieceAttacked){
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }
        @Override
        public boolean equals(final Object other){ 
            return this == other || other instanceof AttackingMove && super.equals(other);
        }
    }
    
    //normal move is like just a move, without attacking
    public static final class NormalMove extends Move {
        public NormalMove(final Board board, final Piece movedPiece, final int destinationCoordinate){
            super(board, movedPiece, destinationCoordinate);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof NormalMove && super.equals(other);
        }
    }
    
    public static class AttackMove extends Move {
        final Piece attackedPiece;
        
        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece; 
        }
        
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode(); 
        }
        
        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
        @Override
        public boolean isAttack(){         //if the piece is attacked by other piece, return true
            return true;  
        }
        @Override   
        public Piece getAttackedPiece() {  //and return the piece that is attacked.
            return this.attackedPiece;  
        }
    }
    
    //how point piece will move
    public static final class PointMove extends Move {
        public PointMove(final Board board, final Piece movedPiece, final int destinationCoordinate){
            super(board, movedPiece, destinationCoordinate);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PointMove && super.equals(other);
        }
    }
    
    public static final class PointAttackMove extends AttackMove {
        public PointAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PointAttackMove && super.equals(other);
        }
    }
    
    //in a case where the point is attempting their first move, the point can choose to move 2 tiles ahead. 
    public static final class PointJump extends Move {
        public PointJump(final Board board, final Piece movedPiece, final int destinationCoordinate){
            super(board, movedPiece, destinationCoordinate);
        }
        @Override
        public Board execute(){
            final Board.Builder builder = new Board.Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            } 
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            } 
            final Point movedPoint = (Point)this.movedPiece.movePiece(this);
            builder.setPiece(movedPoint);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build(); 
        }
    }
    
    //where the piece is not actually making a move
    public static final class NullMove extends Move {
        public NullMove(){
            super(null, 43);
        }
        @Override
        public Board execute(){
            throw new RuntimeException("takboleh!");
        }
        @Override
        public int getCurrentCoordinate() {
            return -1; 
        }
    }

    //the factory method
    public static class MoveFactory {
        private MoveFactory(){
            throw new RuntimeException("kenottt");
        }
        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate){
            for(final Move move : board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate){
                    return move; 
                }
            }
            return NULLMOVE;
        }
    }
}