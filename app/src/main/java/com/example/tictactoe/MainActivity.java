package com.example.tictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public final static String BOARDSIZE = "boardsize";
    public final static String BUNDLEVAL = "boardsval";
    int playerTurn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerTurn = 1;
        //Checks if the user has selected to play first or AI will go first.
        //Switch is by default set to ON, so that AI will play the first move.
        //If the switch is OFF then the player will play the first move.
        final EditText editTextGetSize = (EditText) findViewById(R.id.editTextGetSize);
        Switch switchPlayerTurn = (Switch) findViewById(R.id.switchPlayerTurn);

        switchPlayerTurn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    playerTurn = 1;
                else
                    playerTurn = 0;
            }
        });

        //Gives a list of available algorithms the user can choose to play against.
        //These are 1. Minimax, 2. Minimax with Alpha Beta Pruning,
        //3. Minimax Cutoff Heuristic - Checks the number of X's and O's in row, column and diagonal
        //and allocates the value to eval function accordingly
        //4. Minimax Cutoff Heuristic - Checks the number of rows, columns and diagonal where the
        //winner is X or O.
        final Spinner spinnerChooseAlgo = (Spinner) findViewById(R.id.spinnerChooseAlgo);
        ArrayAdapter adapterAlgo = ArrayAdapter.createFromResource(this, R.array.listofAlgo, android.R.layout.simple_spinner_item);
        adapterAlgo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChooseAlgo.setAdapter(adapterAlgo);


        Button buttonPlay = (Button) findViewById(R.id.buttonPlay);

        //OnClick of Play Button, a new Acitivity will start and this is where the board is
        //displayed with the number of tiles as the user has selected.
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs(editTextGetSize.getText().toString())) {
                    Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                    Bundle bundle = new Bundle();
                    String[] fromMain = {editTextGetSize.getText().toString(), spinnerChooseAlgo.getSelectedItem().toString(), Integer.toString(playerTurn)};
                    bundle.putStringArray(BOARDSIZE, fromMain);
                    intent.putExtra(BUNDLEVAL, bundle);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "Please check size of board", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


     /**
     * Returns boolean true or false after checking the input from the user for boardsize
     * If the board size is not odd, it will return false else true.
     *
     * @param  getBoardSize  boardSize as entered by the user
     * @return boolean true/false
     **/
    public boolean validateInputs(String getBoardSize) {
        if (!(getBoardSize.equals(""))) {
            if (getBoardSize.matches("\\d+")) {
                int size = Integer.parseInt(getBoardSize);
                if (size % 2 != 0 && size > 1)
                    return true;
            }
        }
        return false;
    }


}
