package com.example.tictactoe;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by prajvalb on 11/14/17.
 */

public class Board {
    private int boardSize;
    private String[] boardState;
    private int value;

    //Constructor for new Board
    public Board(int bSize, String[] boardState) {
        this.boardState = boardState;
        this.boardSize = bSize;
    }

    public Board(){

    }

    //Getters and Setters for the board
    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public String[] getBoardState() {
        return boardState;
    }

    public void setBoardState(String[] boardState) {
        this.boardState = boardState;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
     * Coverts the boardState(represented as array of string into String matrix of
     * boardSize * boardSize for the checkWinner function
     *
     * @param  d  boardState which needs to be converted to matrix
     * @return a  String matrix
     **/
    public String[][] convertArray ( String[] d)
    {
        String [][] a = new String[BoardActivity.boardSize][BoardActivity.boardSize];

        int k=0;
        for(int i=0;i<BoardActivity.boardSize;i++){
            for(int j=0;j<BoardActivity.boardSize;j++){
                a[i][j]=d[k++];
            }
        }
        return a;
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

    /**
     *
     * Returns the ArrayList of the Boards for the current boardState and the currentPlayer
     *
     * @param  board  Board state of the current boardState
     * @param currentPlayer the currentPlayer to generate the future moves
     * @return              ArrayList of the Boards i.e the next possible states a player can play
     *
     **/
    ArrayList<Board> generateSuccessors(Board board, String currentPlayer){
        ArrayList<Board> tmpBoard = new ArrayList<Board>();
        Board originalBoard = board;
        Integer[] gloabalIndex = new Integer[BoardActivity.boardSize*BoardActivity.boardSize - checkX(board.getBoardState())];
        int count = 0;
        String[] tmpState = board.getBoardState();

        int iterateValue = BoardActivity.boardSize*BoardActivity.boardSize - checkX(board.getBoardState());
        for (int i = 0; i < iterateValue; i++) {
            int[] indices = findAvaibleIndex(tmpState, gloabalIndex);
            gloabalIndex[count++] = indices[0];
            String[] maybeState = new String[board.getBoardState().length];
            for (int s = 0; s < maybeState.length; s++)
                maybeState[s] = board.getBoardState()[s];

            maybeState[indices[0]] = currentPlayer;
            tmpState = maybeState;
            Board newBoard = new Board(BoardActivity.boardSize, tmpState);
            tmpBoard.add(newBoard);
            tmpState = newBoard.getBoardState();
        }
        return tmpBoard;
    }

    /**
     *
     * Returns the avaible index for generating the new Boards
     *
     * @param  mboardState  Board state to generate the avaible index
     * @param globalI globalIndex for which the states are already generated
     * @return the Array of integers where the board states are yet to be generated
     *
     **/
    int[] findAvaibleIndex(String[] mboardState, Integer[] globalI){
        int[] tmpBoardState = new int[mboardState.length];
        int index = 0;

        for (int i = 0; i < mboardState.length; i++){
            if (mboardState[i].equals("-")) {
                if (!(Arrays.asList(globalI).contains(i)))
                    tmpBoardState[index++] = i;


            }
        }
        return tmpBoardState;
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
     * Prints the boardState of the current/requested boardState
     *
     * @param  state  current/requested boardState to be printed
     * @return void
     **/
    public void printBoardStateMax(String[] state){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < state.length; i++){
            sb.append(state[i]);
        }
        Log.d("prb_log", "Printing the board state Max " + sb.toString());
    }

    /**
     *
     * Prints the boardState of the current/requested boardState
     *
     * @param  state  current/requested boardState to be printed
     * @return void
     **/
    public void printBoardStateMin(String[] state){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < state.length; i++){
            sb.append(state[i]);
        }
        Log.d("prb_log", "Printing the board state Min " + sb.toString());
    }

    /**
     *
     * Returns the count for the number of x's and o's already set in the given board state
     *
     * @param  state  current/requested boardState to be printed
     * @return count for the number os x's and o's
     **/
    public int checkX(String[] state){
        int count = 0;
        for (int i = 0; i < state.length; i++){
            if (state[i]!= null)
                if (state[i].equals("X") || state[i].equals("O"))
                    count++;
        }
        return count;
    }

}
