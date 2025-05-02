import java.util.*;

//Aida Maisarah Hisam, Farah Kamila Yahya
public abstract class Piece {  //an abstract class for the pieces
    
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode; 
    
    public Piece(final PieceType pieceType, final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove){
        this.pieceType = pieceType; 
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode(); 
    }
    
    private int computeHashCode(){     //multiplying by 31 is a common practice 
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0); 
        return result;
    }
    
    @Override                      //equals method to check if the two instances is the same
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
               pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();        
    }
    
    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }
    
    public int getPiecePosition(){  //returning the pieces' position
        return this.piecePosition;
    }
    
    public Alliance getPieceAlliance(){  //returning the pieces' color
        return this.pieceAlliance; 
    }
    
    public boolean isFirstMove(){  //checking if the pieces are attempting their first move
        return this.isFirstMove;
    }
    
    public PieceType getPieceType(){  //return the piece type
        return this.pieceType; 
    }
    
    public abstract Collection<Move> calcLegalMoves(final Board board);  //where each piece needs to calculate their own legal moves.
    
    public abstract Piece movePiece(final Move move);  //moving the piece
    
    // using enum again as byfault we only have 5 types of pieces
    public enum PieceType { 
        POINT("P"){ 
            @Override
            public boolean isSun(){
            return false;
            }
        },
        PLUS("T"){ 
            @Override
            public boolean isSun(){
            return false;
            }
        }, 
        HOURGLASS("H"){ 
            @Override
            public boolean isSun(){
            return false;
            }
        },
        TIME("X"){ 
            @Override
            public boolean isSun(){
            return false;
            }
        },
        SUN("S"){
            @Override
            public boolean isSun(){
            return true;
            }
        };
        
        private String pieceName;
        PieceType(final String pieceName){
               this.pieceName = pieceName;
        }
        
        @Override
        public String toString(){
            return this.pieceName;
        }
        public abstract boolean isSun(); //checking if the piece is the sun piece
    }
}