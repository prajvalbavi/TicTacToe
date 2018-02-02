package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BoardActivity extends MenuActionClass {
    private TableLayout mainTableLayout;
    public static int boardSize;
    static int drawset = 0;
    static int totalCount;
    String[] boardState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);


        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(MainActivity.BUNDLEVAL);

        Random random = new Random();
        //Gets the value like name of the algorithm and the boardsize from the MainActivity
        //If for some reason the app get crashed and is not able to send the data, this will
        //finish the current activity and it will go to the MainActivity.
        //boardSize - the size of the board, eg: for 3x3 it will be 3
        //boardState - the x and o's of the current board
        //algoName - which algorithm the user has choosen to play against
        if (bundle != null) {
            totalCount = 0;
            String[] algoDetails = bundle.getStringArray(MainActivity.BOARDSIZE);
            boardSize = Integer.parseInt(algoDetails[0]);
            boardState = new String[boardSize*boardSize];
            final String algoName = algoDetails[1];
            int k = 0;
            for (int some = 0; some < boardSize*boardSize; some++){
                boardState[k++] = "-";
            }

            //If the user has selected the AI should play first, then we will generate a random
            //number between 1-(boardSize*boardSize) and the AI will play its first move there
            if (algoDetails[2].equals("1")) {
                int someindex = random.nextInt(boardSize*boardSize);
                boardState[someindex] = "X";
            }

            //This will be used to set the layout for the tic tac toe board
            //Setting up the number of rows and columns according to the boardSize the player has
            //selected
            float tableLayoutWeight = (1 / (float)boardSize);
            int textSizeSetting = (450/boardSize);
            mainTableLayout = (TableLayout) findViewById(R.id.mainTableLayout);
            TableRow.LayoutParams layoutParamsTR = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, tableLayoutWeight);
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(5);
            gd.setStroke(1, 0xFF000000);
            int count = 1;

            //Actual setup for the rows and columns, each and every cell will be treated as a
            //textview and clicking on the textview will disable the specific textview and you wont
            //to click that textview again and also the textview the AI has player will also be
            //disabled so as to not consider the clicking events on those textview
            for (int i = 0; i < boardSize; i++) {
                TableRow tableRow = new TableRow(BoardActivity.this);
                int sizeDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
                layoutParamsTR.setMargins(sizeDP, sizeDP, sizeDP, sizeDP);
                tableRow.setLayoutParams(layoutParamsTR);

                for (int j = 0; j < boardSize; j++) {
                    final TextView textView = new TextView(BoardActivity.this);
                    TableRow.LayoutParams textLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, tableLayoutWeight);
                    textLayoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
                    textView.setLayoutParams(textLayoutParams);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSetting);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setId(count++);
                    //OnClickListener for the TextView is implemented here, It will check which
                    //cell has been clicked and will updated the boardState accordingly to the
                    //set TextView and it will disable the corresponding TextView
                    //Following steps will be done after a click on any TextView from the user
                    //1. Update the boardState, and call checkWinner for "O" i.e the user
                    //if the checkWinner returns true, then user is the winner and finish the game
                    //2. Check if the boardState is full, ie if no addtional moves are available,
                    // then the game is a draw and notify the user with the same.
                    //3. Call the AI algorithm depending on the choice of algorithm the user has
                    //selected and call that specific method to do the further processing
                    //4. findBestBoardMinimax*alog* will return a new board state that the AI thinks
                    //is the best move, update the boardState accordingly
                    //5. Check if the updated boardState is the winner for AI, and update the game
                    //status accordingly
                    //6. Start the whole process for the new OnClick of a new TextView all over again
                    textView.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onClick(View view) {
                            ((TextView) view).setText("O");
                            ((TextView) view).setEnabled(false);

                            for (int i = 0; i < mainTableLayout.getChildCount(); i++){
                                TableRow tmpTR = (TableRow) mainTableLayout.getChildAt(i);
                                for (int j = 0; j < tmpTR.getChildCount(); j++){
                                    boardState[textView.getId()-1] = textView.getText().toString();
                                    if (checkWinner(boardState, "O")){
                                        Toast.makeText(BoardActivity.this, "You won ? This should not have happened", Toast.LENGTH_SHORT).show();
                                        gameOver();
                                    } else if (boardFullCheck(boardState)){
                                        Toast.makeText(BoardActivity.this, "Eww! Its a Draw", Toast.LENGTH_SHORT).show();
                                        drawset = 1;
                                        gameOver();
                                    }

                                }
                            }
                            Log.d("prb_log", "Printing the board state after player plays");
                            printBoardState(boardState);

                            Board board = new Board(boardSize, boardState);
                            if (textView.getText().toString().equals("O")) {
                                if (algoName.equals("Minimax")) {
                                    Log.d("prb_log", "Going for minimax");
                                    findBestBoardMinimax(board);
                                } else if (algoName.equals("Minimax + AlphaBeta")){
                                    Log.d("prb_log", "Going for minimax + AlphaBeta");
                                    findBestBoardMinimaxAlphaBeta(board);
                                } else if (algoName.equals("Minimax Cutoff")){
                                    Log.d("prb_log", "Going for minimaxCutoff");
                                    findBestBoardMinimaxCutoff(board);
                                } else {
                                    Log.d("prb_log", "Going for minimaxCutoff wint number of win ");
                                    findBestBoardMinimaxCutoffWinHeuristic(board);
                                }

                                for (int j = 1; j < (boardSize*boardSize +1); j++){
                                    ((TextView)findViewById(j)).setText(boardState[j-1]);
                                    if (!boardState[j-1].equals("-"))
                                        ((TextView) findViewById(j)).setEnabled(false);

                                }

                                if (checkWinner(boardState, "X")){
                                    Toast.makeText(BoardActivity.this, "AI won !", Toast.LENGTH_SHORT).show();
                                    gameOver();
                                } else if(boardFullCheck(boardState)){
                                    if (drawset == 0) {
                                        Toast.makeText(BoardActivity.this, "Eww! Its a Draw", Toast.LENGTH_SHORT).show();
                                        gameOver();
                                    }
                                }


                            }
                        }
                    });
                    textView.setBackground(gd);
                    tableRow.addView(textView);

                }
                mainTableLayout.addView(tableRow);
            }
            for (int j = 1; j < (boardSize*boardSize + 1); j++) {
                ((TextView) findViewById(j)).setText(boardState[j - 1]);
                if (!boardState[j-1].equals("-"))
                    ((TextView) findViewById(j)).setEnabled(false);

            }

        } else {
            finish();
        }
    }


    /**
     *
     * Prints the boardState of the current/requested boardState
     *
     * @param  state  current/requested boardState to be printed
     * @return void
     **/
    public void printBoardState(String[] state){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < state.length; i++){
            sb.append(state[i]);
        }
        Log.d("prb_log", "Printing the board state " + sb.toString());
    }

    /**
     *
     * Coverts the boardState(represented as array of string into String matrix of
     * boardSize * boardSize for the checkWinner function
     *
     * @param  d  boardState which needs to be converted to matrix
     * @return a  String matrix
     **/
    public String[][] convertArray ( String[] d)
    {
        String [][] a = new String[boardSize][boardSize];

        int k=0;
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                a[i][j]=d[k++];
            }
        }
        return a;
    }


    /**
     *
     * Updates the boardState with the play the AI thinks is most suitable according to the
     * algorithm, here the algorithm is simple Minimax, which will generate the whole tree
     * and assign the utility value at the leaf nodes.
     * The values assigned are +1000 for winner of AI, -1000 for winner of user and 0 if draw
     * The values are then backed up to the original list of available generatedBoards and the best
     * of the board is selected.
     *
     * @param  board  Board state of the current boardState
     * @return void
     *
     **/
    public void findBestBoardMinimax(Board board){
        ArrayList<Board> someStates = board.generateSuccessors(board, "X");
        int winnerVal = (int) Double.NEGATIVE_INFINITY;
        for (Board mState : someStates) {
            int tmpwinnerVal = Minimax.miniMax(mState,  "O");
            Log.d("prb_log", "Selected State Value tmpwinnerVal " + tmpwinnerVal);

            if (tmpwinnerVal  > winnerVal) {
                boardState = mState.getBoardState();
                winnerVal = tmpwinnerVal;
                Log.d("prb_log", "Selected State Value " + winnerVal);
                printBoardState(boardState);

            }
        }
    }

    /**
     *
     * Updates the boardState with the play the AI thinks is most suitable according to the
     * algorithm, here the algorithm is simple Minimax with AlpaBeta pruning,
     * which will generate the whole tree and assign the utility value at the leaf nodes.
     * The values assigned are +1000-depth for winner of AI, -1000-depth for winner of user and
     * 0 if draw The values are then backed up to the original list of available generatedBoards
     * and the best of the board is selected.
     *
     * @param  board  Board state of the current boardState
     * @return void
     *
     **/
    public void findBestBoardMinimaxAlphaBeta(Board board){
        ArrayList<Board> someStates = board.generateSuccessors(board, "X");
        int winnerVal = (int) Double.NEGATIVE_INFINITY;
        for (Board mState : someStates) {
            int tmpwinnerVal = MinimaxAlphBeta.miniMax(mState, 0, (int)Double.NEGATIVE_INFINITY, (int)Double.POSITIVE_INFINITY, "O");
            Log.d("prb_log", "Selected State Value tmpwinnerVal " + tmpwinnerVal);
            if (tmpwinnerVal  > winnerVal) {
                boardState = mState.getBoardState();
                winnerVal = tmpwinnerVal;
                Log.d("prb_log", "Selected State Value " + winnerVal);
                printBoardState(boardState);

            }
        }
    }

    /**
     *
     * Updates the boardState with the play the AI thinks is most suitable according to the
     * algorithm, here the algorithm is simple Minimax with Cutoff,
     * this will go to the depth as defined in the algorithm, here the algorithm is going at depth 4
     * When the depth is reached, the evalution function will return the value for that board
     * and these values are backed up to the original list of available generatedBoards
     * Heuristic of Evalution Function:
     * Checks the number of rows, columns and diagonals with only x's and o's
     * evaluationValue = 3 * X2 + 1 * X1 - 3 * O2 + 1 * O1
     * where, X2 - number of rows, columns and diagonals with only 2 number of X's
     *        O2 - number of rows, columns and diagonals with only 2 number of O's
     *        X1 - number of rows, columns and diagonals with only 1 number of X's
     *        O2 - number of rows, columns and diagonals with only 1 number of O's
     *
     * @param  board  Board state of the current boardState
     * @return void
     *
     **/
    public void findBestBoardMinimaxCutoff(Board board){
        ArrayList<Board> someStates = board.generateSuccessors(board, "X");
        int winnerVal = (int) Double.NEGATIVE_INFINITY;
        for (Board mState : someStates) {
            int tmpwinnerVal = MinimaxCutoff.miniMax(mState, 0, (int)Double.NEGATIVE_INFINITY, (int)Double.POSITIVE_INFINITY, "O");
            Log.d("prb_log", "Selected State Value tmpwinnerVal " + tmpwinnerVal);

            if (tmpwinnerVal  > winnerVal) {
                boardState = mState.getBoardState();
                winnerVal = tmpwinnerVal;
                Log.d("prb_log", "Selected State Value " + winnerVal);
                printBoardState(boardState);

            }
        }
    }

    /**
     *
     * Updates the boardState with the play the AI thinks is most suitable according to the
     * algorithm, here the algorithm is simple Minimax with Cutoff,
     * this will go to the depth as defined in the algorithm, here the algorithm is going at depth 2
     * When the depth is reached, the evalution function will return the value for that board
     * and these values are backed up to the original list of available generatedBoards
     * Heuristic of Evalution Function:
     * Checks the number of rows, columns and diagonals with victories for X and O
     * evaluationValue = winX - winO
     * where, winX - number of rows, columns and diagonals with X can win
     *        winO - number of rows, columns and diagonals with O can win
     *
     * @param  board  Board state of the current boardState
     * @return void
     *
     **/
    public void findBestBoardMinimaxCutoffWinHeuristic(Board board){
        ArrayList<Board> someStates = board.generateSuccessors(board, "X");
        int winnerVal = (int) Double.NEGATIVE_INFINITY;
        for (Board mState : someStates) {
            int tmpwinnerVal = MinimaxCutoffHeuristicWin.miniMax(mState, 0, (int)Double.NEGATIVE_INFINITY, (int)Double.POSITIVE_INFINITY, "O");
            Log.d("prb_log", "Selected State Value tmpwinnerVal " + tmpwinnerVal);

            if (tmpwinnerVal  > winnerVal) {
                boardState = mState.getBoardState();
                winnerVal = tmpwinnerVal;
                Log.d("prb_log", "Selected State Value " + winnerVal);
                printBoardState(boardState);

            }
        }
    }


    /**
     *
     * Returns the boolean if the currentPlayer is the winner
     * Checks the rows, columns and diagonals for the winner and return true if the winner is found
     * else returns false
     *
     * @param  state  Board state of the current boardState
     * @param currentPlayer the currentPlayer to check winner for
     * @return boolean
     *
     **/
    public boolean checkWinner(String[] state, String currentPlayer){
        int row = 0, col = 0, i = 0;
        String playerCurrent = currentPlayer;
        String [][] a = convertArray(state);
        int n = a.length;
        for ( row = 0; row < n; row++)
        {
            for ( col = 0; col < 1; col++)
            {
                while (a[row][col] == playerCurrent)
                {
                    col++;
                    i++;
                    if (i == n)
                    {
                        return true;
                    }
                }
                i = 0;
            }
        }

        for ( col = 0; col < n; col++)
        {
            for ( row = 0; row < 1; row++)
            {
                while (a[row][col] == playerCurrent)
                {
                    row++;
                    i++;
                    if (i == n)
                    {
                        return true;
                    }
                }
                i = 0;
            }
        }

        for ( col = 0; col < 1; col++)
        {
            for ( row = 0; row < 1; row++)
            {
                while (a[row][col] == playerCurrent)
                {
                    row++;
                    col++;
                    i++;
                    if (i == n)
                    {
                        return true;
                    }
                }
                i = 0;
            }
        }

        for ( col = n-1; col > 0+(n-2); col--)
        {
            for ( row = 0; row < 1; row++)
            {
                while (a[row][col] == playerCurrent)
                {
                    row++;
                    col--;
                    i++;
                    if (i == n)
                    {
                        return true;
                    }
                }
                i = 0;
            }
        }

        return false;
    }


    /**
     *
     * Displays toasts if the winner is found or if the boardState is full
     * If the user then decides to restart the game, then the new game is spwaned from here
     * with the same boardState as choosen at the start of the MainAcitivity
     *
     * @return void
     *
     **/
    public void gameOver(){
        Log.d("prb_log", "Total states generated/explored " + totalCount);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Restart Game ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(BoardActivity.this, MainActivity.class));
                    }
                })
                .setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     *
     * Returns the boolean true for the boardState is full and if no more moves are avaiable
     * else returns false
     *
     * @param  state  Board state of the current boardState
     * @return boolean
     *
     **/
    public boolean boardFullCheck(String[] state){
        for (int i = 0; i < state.length; i++){
            if (state[i].equals("-"))
                return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}