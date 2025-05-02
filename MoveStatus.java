//Sharleen Ravi Mahendra
//enum representing 3 move statuses
public enum MoveStatus { 
    DONE {                    // Move is successfully executed
        @Override
        public boolean isDone() {
            return true;       //return true as the move is successfully executed
        }
    },
    ILLEGALMOVE {            //move made is illegal
        @Override
        public boolean isDone() {
            return false;    //return false as the move is illegal
        }
    },
    LEAVESPLAYERINCHECK {     //a move which leaves the player in check
        @Override
        public boolean isDone() {
            return false;    //return false as the move leaves the player in check
        }
    };
    
    //check if the move status is done
    abstract boolean isDone();
}