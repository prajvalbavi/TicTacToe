package com.example.tictactoe;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by prajvalb on 11/24/17.
 */

public class Minimax {
    /**
     *
     * This will recursively call minimax depending on the current player,
     * if the current player is X, the algorithm will find the max available board and if
     * the current player is O, the algorithm will find the min avaiable board
     * The terminal state check is done at the start before recursively calling minimax,
     * the terminal checks are checkWinner for player X(AI) and O(User) and if the board state is
     * full, if either of the conditions gets satisfied then it will return the value as decided
     * if winner is AI(X), assign +1000, if winner is User(O) assign -1000 and if the state is
     * draw assign 0 and return
     *
     * @param  board  board for which the minimax will be called recursively
     * @param player  player for which the board state should be generated
     * @return boolean true/false
     **/
    public static int miniMax(Board board, String player){
        BoardActivity.totalCount++;
        if (board.checkWinner(board.getBoardState(), "X")) {
            return 1000;
        } else if (board.checkWinner(board.getBoardState(), "O")) {
            return -1000;
        } else if (board.boardFullCheck(board.getBoardState())){
            return 0;
        }
        else {
            if (player.equals("X")) {
                int value = (int)Double.NEGATIVE_INFINITY;
                ArrayList<Board> someStates = board.generateSuccessors(board, "X");
                for (Board sBoard : someStates) {
                    board.printBoardStateMax(sBoard.getBoardState());
                    value = Math.max(value, miniMax(sBoard,  "O"));
                    Log.d("prb_log","Inside maxValue " + " " + value);
                }
                return value;
            }

            else  {
                int best = (int)Double.POSITIVE_INFINITY;
                ArrayList<Board> otherStates = board.generateSuccessors(board, "O");
                for (Board oBoard : otherStates) {
                    board.printBoardStateMin(oBoard.getBoardState());
                    best = Math.min(best, miniMax(oBoard, "X"));
                    Log.d("prb_log", "Inside minValue " + " " + best);
                }
                return best;
            }

        }

    }
}
