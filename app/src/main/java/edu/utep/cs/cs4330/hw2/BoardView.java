package edu.utep.cs.cs4330.hw2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * A special view class to display an omok board. An omok board is
 * displayed as a 2D grid, and black/white stones are displayed as
 * filled circles. When a place (or intersection) of the board is touched,
 * it is notified to all registered
 * @{link edu.utep.cs.cs4330.hw2.BoardView.BoardTouchListener}.
 *
 * @author Yoonsik Cheon
 * @see edu.utep.cs.cs4330.hw2.BoardView.BoardTouchListener
 */
public class BoardView extends View {


    /** To listen to board touches. */
    public interface BoardTouchListener {

        /** Called when a board place at (x,y) is touched, where
         * x and y are 0-based column and row indices. */
        void onTouch(int x, int y);
    }

    /** Listeners to be notified for board touches. */
    private final List<BoardTouchListener> listeners = new ArrayList<>();

    /** Number of places or intersections in rows and columns,
     * total boardSize x boardSize places.
     * FIX: obtain from the corresponding board.
     */
    private int boardSize = 10;

    /** Board background color. */
    private int boardColor = Color.rgb(230, 138, 0);

    /** Last place touched. Just for testing! */
    private float lastX = -1;
    private float lastY = -1;
    // this is a place to store a copy of the board to be drawn
    private Place[][] places = new Place[boardSize][boardSize];
    // Method to initialize the places.
    private void initializePlace(){
        for(int x=0; x<places.length; x++){
            for(int j=0;j<places.length;j++){
                places[x][j]=new Place();
            }
        }
    }

    /** Create a new board view to be run in the given context. */
    public BoardView(Context context) {
        this(context, null);
        initializePlace();
    }

    /** Create a new board view with the given attributes. */
    public BoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initializePlace();
    }

    /** Create a new board view with the given attributes and style. */
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializePlace();
    }

    /** Overridden here to draw a 2-D representation of an omok board. */
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        // draw a 2D grid
        final float maxIndex = maxIndex();
        final float lineGap = lineGap();
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(boardColor);
        canvas.drawRect(0, 0, maxIndex, maxIndex, paint);
        paint.setColor(Color.BLACK);
        for (int i = 0; i < numOfLines(); i++) {
            float index = i * lineGap;
            canvas.drawLine(0, index, maxIndex, index, paint); // horizontal
            canvas.drawLine(index, 0, index, maxIndex, paint); // vertical
        }
        float r = lineGap/2;
        //Paint the copied board from board
        for(int x=0; x<places.length;x++){
            for(int y=0;y<places.length;y++){
                //Get the float value from X & Y position
                lastX = (x + 1) * lineGap();
                lastY = (y + 1) * lineGap();
                //If a white player found paint a white stone
                if(places[x][y].getPlayer().equals("W")) {
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(lastX, lastY, r, paint);
                }
                //If a black player fount paint a black stone
                if(places[x][y].getPlayer().equals("B")){
                    paint.setColor(Color.BLACK);
                    canvas.drawCircle(lastX, lastY, r, paint);
                }
                //If a winner white player found paint the black inner circle
                if(places[x][y].getPlayer().equals("w")){
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(lastX, lastY, r, paint);
                    paint.setColor(Color.BLACK);
                    canvas.drawCircle(lastX, lastY, r / 2, paint);
                }
                //If a winner black player found paint the white inner circle
                if(places[x][y].getPlayer().equals("b")){
                    paint.setColor(Color.BLACK);
                    canvas.drawCircle(lastX, lastY, r, paint);
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(lastX, lastY, r/2, paint);
                }

            }
        }
    }

    /** Return the margin between horizontal/vertical lines. */
    private float lineGap() {
        return Math.min(getMeasuredWidth(), getMeasuredHeight())
                / (boardSize + 1.0f);
    }

    /** Return the number of horizontal/vertical lines. */
    private int numOfLines() {
        return boardSize + 2;
    }

    /** Return the maximum x/y screen coordinate. */
    private float maxIndex() {
        return lineGap() * (numOfLines() - 1);
    }

    /** Overridden here to identify a board place corresponding to
     * a screen position touched. If the corresponding place is located,
     * all registered board touch listeners will be notified.
     *
     * @see edu.utep.cs.cs4330.hw2.BoardView.BoardTouchListener
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xy = locatePlace(event.getX(), event.getY());
        if (xy >= 0) {
            int x = xy / 100;
            int y = xy % 100;
            lastX = (x + 1) * lineGap();
            lastY = (y + 1) * lineGap();
            invalidate();
            notifyBoardTouch(x, y);
        }
        //toast(event.getX(), event.getY());
        return true;
    }

    /**
     * Given screen coordinates, locate the corresponding place
     * (intersection) in the board and return its coordinates;
     * return -1 if the given coordinates don't correspond to
     * a place in the board. The returned coordinates are encoded
     * as <code>x*100 + y</code>.
     */
    private int locatePlace(float x, float y) {
        int bx = calculateBoardCoordinate(x);
        if (bx < 0) {
            return -1;
        }
        int by = calculateBoardCoordinate(y);
        if (by < 0) {
            return -1;
        }
        return bx * 100 + by;
    }

    /** Return the 0-based board coordinate corresponding to the given
     * screen coordinate, or -1 if it doesn't correspond to
     * any board place. */
    private int calculateBoardCoordinate(float x) {
        //
        // X---X---X-- O: placeable
        // |PS |   |   X: Not placeable
        // X---O---O--
        //
        final float placeSize = lineGap();
        final float radius = placeSize / 2; // of a stone
        // off board?
        if (x < placeSize - radius
                || x > placeSize * boardSize + radius) {
            return -1;
        }

        // --(radius+radius)--(radius+-
        int boardX = (int) ((x - placeSize) / placeSize);
        float remainder = (x - placeSize) - boardX * placeSize;
        if (remainder <= radius) {
            return boardX;
        } else if (remainder >= placeSize - radius) {
            return boardX + 1;
        }
        return -1;
    }

    /** Register the given listener. */
    public void addBoardTouchListener(BoardTouchListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /** Unregister the given listener. */
    public void removeBoardTouchListener(BoardTouchListener listener) {
        listeners.remove(listener);
    }

    /** Notify a place touch to all registered listeners. */
    private void notifyBoardTouch(int x, int y) {
        for (BoardTouchListener listener: listeners) {
            listener.onTouch(x, y);
        }
    }

    private void toast(float x, float y) {
        Toast.makeText(getContext(), "x = " + x + " y = " + y,
                Toast.LENGTH_SHORT).show();
    }

    /*currentBoard copies the board from the model and call 	*invalidate new board
     *@param Place[][] dots get the stones to be drawn*/
    public void currentBoard(Place[][] dots){
        for(int i=0; i<dots.length;i++){
            for(int j=0;j<dots.length;j++){
                places[i][j]=dots[i][j];
            }
        }
        invalidate();
    }
}
