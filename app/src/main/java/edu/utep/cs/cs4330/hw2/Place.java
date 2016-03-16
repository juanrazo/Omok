package edu.utep.cs.cs4330.hw2;

/*Place class creates objects called Place. Places helps us
*determind where or not a player has occupied an intersection on
*the board by an other player. In additon Place class also keeps
*track of the current player throught the game.
*@authors Juan Razo and Genesis Bejarano
**/
public class Place {

    private String player;
    private boolean taken;

    //default state
    public Place(){
        player = "X";
        taken = false;
    }

    /**setPlayer, updates the player's turn
     *to string data type player.
     *@param player, the name of the new current player.
     *@returns player,the new current player
     */
    public void setPlayer(String player){
        this.player = player;
    }

    //returns the currentPlayer
    public String getPlayer(){
        return player;
    }

    /**isTaken,determines where a intersection on
     *the board is taken or not.
     @return taken, true is so, false otherwise
     */
    public boolean isTaken(){
        return taken;
    }

    /**setPlace,updates the board games intersection
     *avaliablity.
     *@param player, the name of the new current player.
     *@param taken, the aviablity of the location
     *@returns player,the new current player
     */

    public void setPlace(String player, Boolean taken){
        this.player = player;
        this.taken = taken;
    }

    /**erasePlace is one of the restart methods
     *when called. This method erases all of the
     *mark intersections a player has made.
     */
    public void erasePlace(){
        player = "X";
        taken = false;
    }
}

