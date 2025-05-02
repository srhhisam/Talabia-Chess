//an enum for a constant value, in this case, there are only two alliances, yellow and blue. 
//Aida Maisarah Hisam
public enum Alliance {   
    YELLOW {
        @Override
        public int getDirection(){
            return -1;  //byfault the initial board will have the yellow pieces on the first rank, hence moving up for the point piece 
        }               //will need them  to have negative value
        @Override
        public boolean isYellow(){
            return true;
        }
        @Override
        public boolean isBlue(){
            return false;
        }
        @Override
        public Player choosePlayer(final YellowPlayer yellowPlayer, final BluePlayer bluePlayer){
            return yellowPlayer; 
        }
    },
    BLUE {
        @Override
        public int getDirection(){
            return 1;
        }
        @Override
        public boolean isYellow(){
            return false;
        }
        @Override
        public boolean isBlue(){
            return true;
        }
        @Override
        public Player choosePlayer(final YellowPlayer yellowPlayer, final BluePlayer bluePlayer){
            return bluePlayer; 
        }
    };
    
    public abstract int getDirection(); //set the player's direction
    public abstract boolean isYellow(); //check if the player is yellow player
    public abstract boolean isBlue();   //check if the player is the blue player
    public abstract Player choosePlayer(YellowPlayer yellowPlayer, BluePlayer bluePlayer); 
}