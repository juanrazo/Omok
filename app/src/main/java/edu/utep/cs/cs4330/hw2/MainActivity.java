package edu.utep.cs.cs4330.hw2;

/**Main Activity class,serves as the control for our Omok
 *application. Main Activity also handles all action events that
 *may accrue durning the game. Such as when a player selects an
 * intersection on the board or when the player wishes to create a new game.
 *In addition MainActivity determines whether a winner or draw as
 *be declared to end the game and display a sound effects.
 *@authors: Juan Razo and Genesis Bejarano.
 */
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/** Test the BoardView class. */
public class MainActivity extends AppCompatActivity {
    private Board board = new Board();
    private Button resetButton;
    private BoardView boardView;
    private MediaPlayer click;
    private MediaPlayer won;
    //Declaring listeners to the UI components
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView text = (TextView) findViewById(R.id.updatePlayer);
        final TextView currentPlayer = (TextView) findViewById(R.id.current_player);
        boardView = (BoardView) findViewById(R.id.boardView);
        resetButton = (Button) findViewById(R.id.reset);
        resetButton.setEnabled(true);
        click = MediaPlayer.create(getApplicationContext(), R.raw.click);
        won = MediaPlayer.create(getApplicationContext(), R.raw.tada);
        boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            @Override
            public void onTouch(int x, int y) {

                //Continue placing stones while there is no winner
                if(!board.foundWinner()){
                    //Check if a stone can be placed at the current X & Y touched
                    if (board.setStone(x, y, board.getPlayerTurn())) {
                        click.start();
                        //Check if there is a winner at the last stone placed
                        if (board.winner(x, y, board.getPlayerTurn())) {
                            won.start();
                            // display the text for the winning player
                            text.setText("Player " + board.getPlayerName() + " Has won!");
                            board.increaseScore(board.getPlayerName());
                        }
                        else {
                            //If there is no winner flip the player
                            board.flipTurn();
                        }
                        //Check for a draw and display the message
                        if(board.gameDraw()){
                            text.setText("Draw");
                        }

                    }
                    //Update the next players turn
                    currentPlayer.setText(board.getPlayerName());
                    //update the view based on the new board
                    boardView.currentBoard(board.getPlace());
                }
            }
        });
    }

    /*
    * Method to confirm a new game when the button New Game is touched
    * This will clear the text where the last winner was displayed and
    * clear the current board as well update the view based on the new board*/
    public void resetButton(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Would you like to start a new Game");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                final TextView text = (TextView) findViewById(R.id.updatePlayer);
                board.eraseBoard();
                boardView.currentBoard(board.getPlace());
                text.setText("");
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}