package com.example.tictactoe;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by prajvalb on 11/24/17.
 */

public class MinimaxCutoffHeuristicWin {
    /**
     *
     * This will recursively call minimax depending on the current player,
     * if the current player is X, the algorithm will find the max available board and if
     * the current player is O, the algorithm will find the min avaiable board
     * Here, the depth is set to 4, so the evalution function is used to give a utility value
     * to the board when the board reaches the depth 2.
     * This also implemets Alpha Beta pruinig, so that the whole tree is not necessarily explored
     * every time and only the necessary ones are explored, this will speed up the algorithm
     * and return the result quickly.
     *
     * @param  board  board for which the minimax will be called recursively
     * @param player  player for which the board state should be generated
     * @return boolean true/false
     **/
    public static int miniMax(Board board, int depth, int alpha, int beta, String player) {
        BoardActivity.totalCount++;
        if (depth == 2)
            return minimaxCutoffEval(board, board.getBoardSize());
        else if (board.checkWinner(board.getBoardState(), "X"))
            return minimaxCutoffEval(board, board.getBoardSize()) + 1000;
        else if (board.checkWinner(board.getBoardState(), "O"))
            return minimaxCutoffEval(board, board.getBoardSize()) - 1000;
        else if (board.boardFullCheck(board.getBoardState()))
            return 0;
        else {
            if (player.equals("X")) {
                int value = (int) Double.NEGATIVE_INFINITY;
                ArrayList<Board> someStates = board.generateSuccessors(board, "X");
                for (Board sBoard : someStates) {
                    board.printBoardStateMax(sBoard.getBoardState());
                    value = Math.max(value, miniMax(sBoard, depth + 1, alpha, beta, "O"));
                    alpha = Math.max(alpha, value);
                    if (beta <= alpha)
                        break;
                    Log.d("prb_log", "Inside maxValue " + " " + value);
                }
                return value;
            } else {
                int best = (int) Double.POSITIVE_INFINITY;
                ArrayList<Board> otherStates = board.generateSuccessors(board, "O");
                for (Board oBoard : otherStates) {
                    board.printBoardStateMin(oBoard.getBoardState());
                    best = Math.min(best, miniMax(oBoard, depth + 1, alpha, beta, "X"));
                    beta = Math.min(beta, best);
                    if (beta <= alpha)
                        break;
                    Log.d("prb_log", "Inside minValue " + " " + best);

                }
                return best;
            }

        }


    }

    public static int minimaxCutoffEval(Board board, int boardSize){
        String[][] tmpState = new String [boardSize][boardSize];
        int winX = 0, winO = 0;

        int k = 0;
        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                tmpState[i][j] = board.getBoardState()[k++];
            }
        }

        ArrayList<Integer> totalResult = new ArrayList<>();
        ArrayList<String[]> rowList = new ArrayList<>();
        for (int i = 0; i < boardSize; i++){
            String[] tmpBoard = new String[boardSize];
            for (int j = 0; j < boardSize; j++){
                tmpBoard[j] = tmpState[i][j];
            }
            rowList.add(tmpBoard);
        }
        for (int i = 0; i < boardSize; i++){
            if (Arrays.asList(rowList.get(i)).contains("X") && Arrays.asList(rowList.get(i)).contains("O"))
                totalResult.add(0);
            else if (Arrays.asList(rowList.get(i)).contains("X") && !Arrays.asList(rowList.get(i)).contains("O")){
                winX++;
            }
            else if (Arrays.asList(rowList.get(i)).contains("O") && !Arrays.asList(rowList.get(i)).contains("X")){
                winO++;
            }

            else
                totalResult.add(0);
        }

        ArrayList<String[]> columnList = new ArrayList<>();
        for (int i = 0; i < boardSize; i++){
            String[] tmpBoard = new String[boardSize];
            for (int j = 0; j < boardSize; j++){
                tmpBoard[j] = tmpState[j][i];
            }
            columnList.add(tmpBoard);
        }

        for (int i = 0; i < boardSize; i++){
            if (Arrays.asList(columnList.get(i)).contains("X") && Arrays.asList(columnList.get(i)).contains("O"))
                totalResult.add(0);
            else if (Arrays.asList(columnList.get(i)).contains("X") && !Arrays.asList(columnList.get(i)).contains("O")){
                winX++;
            }
            else if (Arrays.asList(columnList.get(i)).contains("O") && !Arrays.asList(columnList.get(i)).contains("X")){
                winO++;
            }
            else
                totalResult.add(0);
        }

        k=0;
        String[] tmpBoard = new String[boardSize];
        ArrayList<String[]> diagonalList = new ArrayList<>();
        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                if (i == j)
                    tmpBoard[k++] = tmpState[i][j];
            }
        }
        diagonalList.add(tmpBoard);

        k=0;
        String[] tmpBoard1 = new String[boardSize];
        for (int i = 0; i < boardSize; i++){

            for (int j = 0; j < boardSize; j++){
                if (i == boardSize-j-1)
                    tmpBoard1[k++] = tmpState[i][j];
            }

        }
        diagonalList.add(tmpBoard1);

        for (int i = 0; i < 2; i++){
            if (Arrays.asList(diagonalList.get(i)).contains("X") && Arrays.asList(diagonalList.get(i)).contains("O"))
                totalResult.add(0);
            else if (Arrays.asList(diagonalList.get(i)).contains("X") && !Arrays.asList(diagonalList.get(i)).contains("O")){
                winX++;
            }
            else if (Arrays.asList(diagonalList.get(i)).contains("O") && !Arrays.asList(diagonalList.get(i)).contains("X")){
                winO++;
            }
            else
                totalResult.add(0);
        }

        int evalValue =0;

        evalValue = winX - winO;
        Log.d("prb_log", "Evaluation value " + evalValue);

        return evalValue;
    }
}
