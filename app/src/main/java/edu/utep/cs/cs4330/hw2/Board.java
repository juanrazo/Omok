package edu.utep.cs.cs4330.hw2;

/**Board class is represented as the model in our Omok
 *applicaiton by contianing all of the Omok's boardgame
 *information. This class initalizes and updates the board which
 *is based off of the players and intersection locations.
 *The Board class also determines who is the current player and
 *which stones that correspond to them.
 *Finally this class also determine where a winner or draw as *been discovered durning the game or not.
 *@authors : Juan Razo and Genesis Bejarano
 */

public class Board {
    final private int boardSize = 10;
    private Place[][] board = new Place[boardSize][boardSize];
    private String playerTurn = "W"; //default player
    private boolean winner = false;
    private int blackScore = 0;
    private int whiteScore = 0;

    //inialize board with Places
    public Board(){
        for(int i=0; i<board.length;i++){
            for(int j=0; j<board[i].length;j++)
                board[i][j] = new Place();
        }
    }

    /**setStone method checks whether the current location
     *player selected is vaild.
     @param x and y, are player's touch coordinates
     @param player, is the current player
     @return true, if vaild
     @return false, otherwise
     */
    public boolean setStone(int x, int y, String player){
        if(!board[x][y].isTaken()){
            board[x][y].setPlace(player, true);
            return true;
        }
        return false;
    }

    //Player switch
    public void flipTurn(){
        playerTurn = playerTurn.equals("B") ? "W" : "B";
    }

    /*getPlayerTurn method
     *return playerTurn, the current player
     */
    public String getPlayerTurn(){
        return playerTurn;
    }

    /*PlayerName method displays the current
     *players name based on the char character.
     *@return "White" if char is w
     *@return "Black" if char is b
     */
    public String getPlayerName(){
        if(playerTurn.equals("B"))
            return "Black";
        if(playerTurn.equals("W"))
            return "White";
        return "X";
    }

    /*eraseBoard is one of the restart method
     *This method clears the board.
     **/
    public void eraseBoard(){
        for(int i=0; i<boardSize;i++){
            for(int j=0; j<boardSize;j++)
                board[i][j].erasePlace();
        }
        winner=false;
    }

    /*getPlace returns the board for
     * the view to replicate
     *@return board, which is the current board.
     **/
    public Place[][] getPlace(){
        return board;
    }

    /*
     * Check if there is a draw in the game.
     *This is done by checking if winner is false and
     *iterate through the board to check if there are any empty places
     *@return true,if there is a draw
     */
    public boolean gameDraw(){
        boolean draw = false;
        //Increment openSpace if there is an open place
        int openSpace=0;
        if(!winner){
            for(int i=0; i<board.length; i++){
                for(int j=0;j<board.length;j++)
                    if(board[i][j].getPlayer().equals("X"))
                        openSpace++;
            }
            //Set draw to true if there is no open spaces
            draw = openSpace>0 ? false : true;
        }
        return draw;
    }

    /*
    * foundWinner method gets the value of winner
    * @return boolean winner
    * */
    public boolean foundWinner(){
        return winner;
    }

    //Keeps track of score
    public void increaseScore(String player){
        if(player=="W")
            whiteScore++;
        else
            blackScore++;
    }

    //Player black current score
    public int getBlackScore(){
        return blackScore;
    }
    //Player white current score
    public int getWhiteScore(){
        return whiteScore;
    }

    //-----------------Winner Methods---------------
/*
* Check 2D array for winner by using helper
* methods.
* @Param int x horizontal point
* @Param int y vertical point
* @Return boolean if there is a winner
*/
    public boolean winner(int x , int y, String player){
        //if Horizontal Win
        int west  = west(x, y, player);
        int east = east(x, y, player);
//        Log.i("Check east & west size", "East " + east + " West " + west);
        if((west + east) >=6){
            winner = true;
            return addWinningEastWest(east, west, x, y, player);
        }
        int north = north(x,y,player);
        int south = south(x,y,player);
        //if Vertical Win
        if((north + south)>=6){
            winner = true;
            return addWinningNorthSouth(north, south, x, y, player);
        }
        //if Left-Diagonal Win
        int northWest = northWest(x, y, player);
        int southEast = southEast(x, y, player);
        if((northWest + southEast)>=6){
            winner = true;
            return addWinningLeftDiagonal(northWest, southEast, x, y, player);
        }
        //if Right-Diagonal Win
        int northEast = northEast(x, y, player);
        int southWest = southWest(x, y, player);
        if((northEast+southWest)>=6){
            winner = true;
            return addWinningRightDiagonal(northEast, southWest, x, y, player);
        }
        return false;
    }

    /*
    * Helper methods will go in all directions,
    * check for out of bounds first in the array
    * else use recursion to return an integer
    */
    private int east(int x, int y, String player){
        if(x>boardSize-1 || y>boardSize-1 || x<0 || y<0)
            return 0;
        if(board[x][y].getPlayer().equals(player)){
            return 1 + east(x + 1, y, player);}
        else return 0;
    }
    private int west(int x, int y, String player){
        if(x>boardSize-1 || y>boardSize-1 || x<0 || y<0)
            return 0;
        if(board[x][y].getPlayer().equals(player)){
            return 1 + west(x - 1, y, player);}
        else return 0;
    }
    private int south(int x, int y, String player){
        if(x>boardSize-1 || y>boardSize-1 || x<0 || y<0)
            return 0;
        if(board[x][y].getPlayer().equals(player)){
            return 1 + south(x, y + 1, player);}
        else return 0;
    }
    private int north(int x, int y, String player){
        if(x>boardSize-1 || y>boardSize-1 || x<0 || y<0)
            return 0;
        if(board[x][y].getPlayer().equals(player)){
            return 1 + north(x, y - 1, player);}
        else return 0;
    }
    private int northWest(int x, int y, String player){
        if(x>boardSize-1 || y>boardSize-1 || x<0 || y<0)
            return 0;
        if(board[x][y].getPlayer().equals(player)){
            return 1 + northWest(x - 1, y - 1, player);}
        else return 0;
    }
    private int northEast(int x, int y, String player){
        // Check the array for out of bounds in 2D array
        if(x>boardSize-1 || y>boardSize-1 || x<0 || y<0)
            //if out of bounds then get out with a 0
            return 0;

        // Check if the place is taken and return 1
        if(board[x][y].getPlayer().equals(player)){
            return 1 + northEast(x + 1, y - 1, player);}
        else return 0;
    }
    private int southWest(int x, int y, String player){
        if(x>boardSize-1 || y>boardSize-1 || x<0 || y<0)
            return 0;
        if(board[x][y].getPlayer().equals(player)){
            return 1 + southWest(x - 1, y + 1, player);}
        else return 0;
    }
    private int southEast(int x, int y, String player){
        if(x>boardSize-1 || y>boardSize-1 || x<0 || y<0)
            return 0;
        if(board[x][y].getPlayer().equals(player)){
            return 1 + southEast(x + 1, y + 1, player);}
        else return 0;
    }


    //-----------Methods used to denote the winning row-----------//

    /*
    * Based on the same stones found  east and west iterate at starting x & y to
    * change the string to lower case. This will in turn be read by the view and change
    * the stone with a contrast color, this method will change the Horizontal row
    * @Param int east the number of places checked east
    * @Param int west the number of places checked west
    * @Param int x the x position to start changing the string
    * @Param int y the y position to start changing the string
    * @Param String player the player string to change to lowercase*/
    private boolean addWinningEastWest(int east, int west, int x, int y, String player){
        if(east>0 & west>0){
            if(east+west>6)
                east--;
            for(int i =0; i < west; i++) {
                board[x-i][y].setPlayer(player.toLowerCase());
            }
            for(int j =0; j < east; j++) {
                board[x+j][y].setPlayer(player.toLowerCase());
            }
            return true;
        }
        return false;
    }

    /*
    * Based on the same stones found  north and south iterate at starting x & y to
    * change the string to lower case. This will in turn be read by the view and change
    * the stone with a contrast color, this method will change the Vertical row
    * @Param int north the number of places checked north
    * @Param int south the number of places checked south
    * @Param int x the x position to start changing the string
    * @Param int y the y position to start changing the string
    * @Param String player the player string to change to lowercase*/
    private boolean addWinningNorthSouth(int north, int south, int x, int y, String player){
        if(north>0 & south>0){
            if(north+south>6)
                north--;
            for(int i =0; i < north; i++) {
                board[x][y-i].setPlayer(player.toLowerCase());
            }
            for(int j =0; j < south; j++) {
                board[x][y+j].setPlayer(player.toLowerCase());
            }
            return true;
        }
        return false;
    }
    /*
    * Based on the same stones found  northWest and southEast iterate at starting x & y to
    * change the string to lower case. This will in turn be read by the view and change
    * the stone with a contrast color, this method will change the LeftDiagonal row
    * @Param int northWest the number of places checked northWest
    * @Param int southEast the number of places checked southEast
    * @Param int x the x position to start changing the string
    * @Param int y the y position to start changing the string
    * @Param String player the player string to change to lowercase*/
    private boolean addWinningLeftDiagonal(int northWest, int southEast, int x, int y, String player){
        if(northWest>0 & southEast>0){
            if(northWest+southEast>6)
                northWest--;
            for(int i =0; i < northWest; i++) {
                board[x-i][y-i].setPlayer(player.toLowerCase());
            }
            for(int j =0; j < southEast; j++) {
                board[x + j][y + j].setPlayer(player.toLowerCase());
            }
            return true;
        }
        return false;
    }
    /*
    * Based on the same stones found  northEast and southWest iterate at starting x & y to
    * change the string to lower case. This will in turn be read by the view and change
    * the stone with a contrast color, this method will change the RightDiagonal row
    * @Param int northEast the number of places checked northEast
    * @Param int southWest the number of places checked southWest
    * @Param int x the x position to start changing the string
    * @Param int y the y position to start changing the string
    * @Param String player the player string to change to lowercase*/
    private boolean addWinningRightDiagonal(int northEast, int southWest, int x, int y, String player){
        if(northEast>0 & southWest>0){
            if(northEast+southWest>6)
                northEast--;
            for(int i =0; i < northEast; i++) {
                board[x+i][y-i].setPlayer(player.toLowerCase());
            }
            for(int j =0; j < southWest; j++) {
                board[x-j][y+j].setPlayer(player.toLowerCase());
            }
            return true;
        }
        return false;
    }

}