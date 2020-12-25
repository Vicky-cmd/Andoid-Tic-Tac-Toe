package com.infotrends.in.tictactoe.ui.playVsComp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.in.tictactoe.R;
import com.infotrends.in.tictactoe.TicTacToeResumeWidget;
import com.infotrends.in.tictactoe.database.DBContract;
import com.infotrends.in.tictactoe.database.DBHelper;
import com.infotrends.in.tictactoe.database.TicTacToeAdapter;
import com.infotrends.in.tictactoe.orchestrator.TicTacToeOrc;

import java.util.ArrayList;
import java.util.Random;

public class TicTacToeCompFragment extends Fragment implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];

    private Button playturn;
    private final String gameType= "2";
    private int gameId = 0;

    private boolean player1Turn = true;

    private int roundcount;

    private boolean roundcompleted = false;

    private int player1Points;
    private int player2Points;

    private int player1PointsInitial;
    private int player2PointsInitial;

    private int gameNo;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    private String P1_NAME = "Player";
    private String P2_NAME = "Computer";

    private String P1_SCORE = "p1_score";
    private String P2_SCORE = "p2_score";

    private String tmpVariable ="";

    private MenuItem resetGame;
    private MenuItem gameClear;

    private String player1key="X";
    private String player2key="O";
    private String boardValues="";

    private SQLiteDatabase mDatabase;
    private TicTacToeAdapter mAdapter;
    TicTacToeOrc ticTacToeOrc = new TicTacToeOrc();

    private View root;
    Context context;

    final Handler handler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.tictac_view, container, false);
        textViewPlayer1 = root.findViewById(R.id.text_view_p1);
        textViewPlayer2 = root.findViewById(R.id.text_view_p2);
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
                buttons[i][j] = root.findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        playturn = root.findViewById(R.id.play_mode);
        playturn.setVisibility(View.INVISIBLE);
        playturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playturn.getText().toString().trim().equalsIgnoreCase("X")) {
                    player1key = "O";
                    player2key = "X";
                } else if (playturn.getText().toString().trim().equalsIgnoreCase("O")) {
                    player1key = "X";
                    player2key = "O";
                }
                playturn.setText(player1key);
                resetBoard();
                firstmoveComputer();
            }
        });

        Button buttonsave = root.findViewById(R.id.button_save);
        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                resetBoard();
                saveGame();
            }
        });
        textViewPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1NameEdit();
            }
        });
        TextView textViewPlayer2 = root.findViewById(R.id.text_view_p2);
        textViewPlayer2.setText("Computer:0");

        context = getActivity();

        DBHelper dbHelper = new DBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();

        if(savedInstanceState!=null) {
            player1Points = savedInstanceState.getInt(P1_SCORE);
            player2Points = savedInstanceState.getInt(P2_SCORE);
            player1PointsInitial = savedInstanceState.getInt("p1_scoreInitial");
            player2PointsInitial = savedInstanceState.getInt("p2_scoreInitial");
            gameNo = savedInstanceState.getInt("gameNo");
            updatesPointsText();
            ArrayList tmp = new ArrayList();
            int k=0;
            tmp = savedInstanceState.getIntegerArrayList("BOXLst");
            for (int i=0; i<3; i++) {
                for (int j=0; j<3; j++) {
                    if(tmp.get(k)!=null) {
                        buttons[i][j].setText(tmp.get(k).toString());
                        k++;
                    }
                }
            }
            P1_NAME = savedInstanceState.getString("P1_NAME");
            P2_NAME = savedInstanceState.getString("P2_NAME");
//            player1Turn = savedInstanceState.getBoolean("P1Chance");
            roundcount = savedInstanceState.getInt("RoundCount");
            player1key = savedInstanceState.getString("player1key");
            player2key = savedInstanceState.getString("player2key");
            player1Turn = savedInstanceState.getBoolean("P1Chance");
            roundcount = savedInstanceState.getInt("RoundCount");
            gameId = savedInstanceState.getInt("gameId");
            updatesPointsText();
        }
        else if(getArguments()!=null) {
            if(getArguments().getString("P1_NAME")!=null) {
                P1_NAME = getArguments().getString("P1_NAME");
            }
            if(getArguments().getString("P2_NAME")!=null) {
                P2_NAME = getArguments().getString("P2_NAME");
            }
            gameNo = getArguments().getInt("gameNo");
            if(getArguments().getInt("p1_scoreInitial")>=0) {
                player1PointsInitial = getArguments().getInt("p1_scoreInitial");
            }
            if(getArguments().getInt("p2_scoreInitial")>=0) {
                player2PointsInitial = getArguments().getInt("p2_scoreInitial");
            }
            if(getArguments().getInt(P1_SCORE)>=0) {
                player1Points = getArguments().getInt(P1_SCORE);
            }
            if(getArguments().getInt(P2_SCORE)>=0) {
                player2Points = getArguments().getInt(P2_SCORE);
            }
            if(getArguments().getIntegerArrayList("BOXLst")!=null) {
                ArrayList tmp = new ArrayList();
                int k=0;
                tmp = getArguments().getIntegerArrayList("BOXLst");
                for (int i=0; i<3; i++) {
                    for (int j=0; j<3; j++) {
                        if(tmp.get(k)!=null) {
                            buttons[i][j].setText(tmp.get(k).toString());
                            k++;
                        }
                    }
                }
            }
            if(getArguments().getInt("RoundCount")>=0) {
                roundcount = getArguments().getInt("RoundCount");
            }
            if(!getArguments().getBoolean("P1Chance")) {
                player1Turn = getArguments().getBoolean("P1Chance");
            }
            if(getArguments().getString("player1key")!=null) {
                player1key = getArguments().getString("player1key");
            }
            if(getArguments().getString("player2key")!=null) {
                player2key = getArguments().getString("player2key");
            }
            gameId = getArguments().getInt("gameId");
            updatesPointsText();
        }
        setHasOptionsMenu(true);

        firstmoveComputer();

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onActivityCreated(savedInstanceState);

        // recovering the instance state
        if (savedInstanceState != null) {
            player1Points = savedInstanceState.getInt(P1_SCORE);
            player2Points = savedInstanceState.getInt(P2_SCORE);
            P1_NAME = savedInstanceState.getString("P1_NAME");
            P2_NAME = savedInstanceState.getString("P2_NAME");
            roundcount = savedInstanceState.getInt("RoundCount");
            player1key = savedInstanceState.getString("player1key");
            player2key = savedInstanceState.getString("player2key");
            player1Turn = savedInstanceState.getBoolean("P1Chance");
            roundcount = savedInstanceState.getInt("RoundCount");
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            for (int i=0; i<3; i++) {
//                for (int j=0; j<3; j++) {
//                    buttons[i][j].setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
//                }
//            }
//            Toast.makeText(context, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            for (int i=0; i<3; i++) {
//                for (int j=0; j<3; j++) {
//                    buttons[i][j].setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
//                }
//            }
//            Toast.makeText(context, "portrait", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(P1_SCORE,player1Points);
        savedInstanceState.putInt(P2_SCORE,player2Points);
        savedInstanceState.putInt("p1_scoreInitial",player1PointsInitial);
        savedInstanceState.putInt("p1_scoreInitial",player2PointsInitial);
        savedInstanceState.putString("P1_NAME",P1_NAME);
        savedInstanceState.putString("P2_NAME",P2_NAME);
        savedInstanceState.putInt("gameNo", gameNo);
        ArrayList tmp = new ArrayList();
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                if(buttons[i][j].getText()!=null)
                    tmp.add(buttons[i][j].getText());
                else
                    tmp.add("");
            }
        }
        savedInstanceState.putIntegerArrayList("BOXLst", tmp);
//        savedInstanceState.putBoolean("P1Chance",player1Turn);
        savedInstanceState.putInt("RoundCount",roundcount);
        savedInstanceState.putString("player1key", player1key);
        savedInstanceState.putString("player2key", player2key);
        savedInstanceState.putBoolean("P1Chance",player1Turn);
        savedInstanceState.putInt("RoundCount", roundcount);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop() {
        if(player1Points==0 && player2Points==0) {
            super.onStop();
            return;
        }
        boardValues="";
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if(buttons[i][j].getText().toString().trim().equalsIgnoreCase("X")){
                    boardValues+="1";
                }
                else if(buttons[i][j].getText().toString().trim().equalsIgnoreCase("O")){
                    boardValues+="0";
                }
                else {
                    boardValues+="5";
                }
            }
        }
        saveCurrGameState();
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        resetGame = menu.findItem(R.id.game_reset);
        resetGame.setVisible(true);
        gameClear = menu.findItem(R.id.game_clear);
        gameClear.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.play_game:
                return false;
            case R.id.game_reset:
                confirmMsg();
                return true;
            case R.id.game_clear:
                resetBoard();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        if(!((Button) v).getText().toString().equals("")) {
            return;
        }


        ((Button) v).setText(player1key);
        roundcount++;
        if(checkForWin()) {
                player1Wins();
        } else if (roundcount==9) {
            draw();
        }
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                buttons[i][j].setClickable(false);
            }
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!roundcompleted) {
                    computerMove();
                } else {
                    roundcompleted=false;
                }
                for (int i=0; i<3; i++) {
                    for (int j=0; j<3; j++) {
                        buttons[i][j].setClickable(true);
                    }
                }
                if(checkForWin()) {
                    player2Wins();
                } else if (roundcount==9) {
                    draw();
                }
            }
        }, 998);

    }

    private void firstmoveComputer() {
        if((player1key.equalsIgnoreCase("O") && roundcount%2==0) || (player1key.equalsIgnoreCase("X") && roundcount%2!=0)) {
            for (int i=0; i<3; i++) {
                for (int j=0; j<3; j++) {
                    buttons[i][j].setClickable(false);
                }
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerMove();
                    for (int i=0; i<3; i++) {
                        for (int j=0; j<3; j++) {
                            buttons[i][j].setClickable(true);
                        }
                    }
                    roundcompleted=false;
                }
            },1000);
        }
    }

    private void computerMove() {
        String[][] field = new String[3][3];

        for ( int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        final int random = new Random().nextInt(9);
        int rand2;
        int flag=0;
        for(int idx=1;idx<=8;idx++) {
            flag = 0;
            int rand1 = (idx+random)%4;
            if(idx>3) {
                rand1+=4;
            }
            switch (rand1) {
                case 0:
                    rand2 = new Random().nextInt(3);
                    for (int i = 0; i < 3; i++) {
                        int val = (i + rand2) % 3;
                        if (field[val][0].equals((field[val][1])) && field[val][2].equals("") && !field[val][0].equals("") && !field[val][0].equals(player1key)) {
                            buttons[val][2].setText(player2key);
                            flag = 1;
                            break;
                        } else if (field[val][0].equals((field[val][2])) && field[val][1].equals("") && !field[val][0].equals("") && !field[val][0].equals(player1key)) {
                            buttons[val][1].setText(player2key);
                            flag = 1;
                            break;
                        } else if (field[val][1].equals((field[val][2])) && field[val][0].equals("") && !field[val][1].equals("") && !field[val][1].equals(player1key)) {
                            buttons[val][0].setText(player2key);
                            flag = 1;
                            break;
                        }
                    }
                    break;
                case 1:
                    rand2 = new Random().nextInt(3);
                    for (int i = 0; i < 3; i++) {
                        int val = (i + rand2) % 3;
                        if (field[0][val].equals((field[1][val])) && field[2][val].equals("") && !field[0][val].equals("") && !field[0][val].equals(player1key)) {
                            buttons[2][val].setText(player2key);
                            flag = 1;
                            break;
                        } else if (field[0][val].equals((field[2][val])) && field[1][val].equals("") && !field[0][val].equals("") && !field[0][val].equals(player1key)) {
                            buttons[1][val].setText(player2key);
                            flag = 1;
                            break;
                        } else if (field[1][val].equals((field[2][val])) && field[0][val].equals("") && !field[1][val].equals("") && !field[1][val].equals(player1key)) {
                            buttons[0][val].setText(player2key);
                            flag = 1;
                            break;
                        }
                    }
                    break;
                case 2:
                    if (field[0][0].equals((field[1][1])) && field[2][2].equals("") && !field[0][0].equals("") && !field[0][0].equals(player1key)) {
                        buttons[2][2].setText(player2key);
                        flag = 1;
                    } else if (field[0][0].equals((field[2][2])) && field[1][1].equals("") && !field[0][0].equals("") && !field[0][0].equals(player1key)) {
                        buttons[1][1].setText(player2key);
                        flag = 1;
                    } else if (field[1][1].equals((field[2][2])) && field[0][0].equals("") && !field[1][1].equals("") && !field[1][1].equals(player1key)) {
                        buttons[0][0].setText(player2key);
                        flag = 1;
                    }
                    break;
                case 3:
                    if (field[0][2].equals((field[1][1])) && field[2][0].equals("") && !field[0][2].equals("") && !field[0][2].equals(player1key)) {
                        buttons[2][0].setText(player2key);
                        flag = 1;
                    } else if (field[0][2].equals((field[2][0])) && field[1][1].equals("") && !field[0][2].equals("") && !field[0][2].equals(player1key)) {
                        buttons[1][1].setText(player2key);
                        flag = 1;
                    } else if (field[1][1].equals((field[2][0])) && field[0][2].equals("") && !field[1][1].equals("") && !field[1][1].equals(player1key)) {
                        buttons[0][2].setText(player2key);
                        flag = 1;
                    }
                    break;
                case 4:
                    rand2 = new Random().nextInt(3);
                    for (int i = 0; i < 3; i++) {
                        int val = (i + rand2) % 3;
                        if (field[val][0].equals((field[val][1])) && field[val][2].equals("") && field[val][0].equals(player1key)) {
                            buttons[val][2].setText(player2key);
                            flag = 1;
                            break;
                        } else if (field[val][0].equals((field[val][2])) && field[val][1].equals("") && field[val][0].equals(player1key)) {
                            buttons[val][1].setText(player2key);
                            flag = 1;
                            break;
                        } else if (field[val][1].equals((field[val][2])) && field[val][0].equals("") && field[val][1].equals(player1key)) {
                            buttons[val][0].setText(player2key);
                            flag = 1;
                            break;
                        }
                    }
                    break;
                case 5:
                    rand2 = new Random().nextInt(3);
                    for (int i = 0; i < 3; i++) {
                        int val = (i + rand2) % 3;
                        if (field[0][val].equals((field[1][val])) && field[2][val].equals("") && field[0][val].equals(player1key)) {
                            buttons[2][val].setText(player2key);
                            flag = 1;
                            break;
                        } else if (field[0][val].equals((field[2][val])) && field[1][val].equals("") && field[0][val].equals(player1key)) {
                            buttons[1][val].setText(player2key);
                            flag = 1;
                            break;
                        } else if (field[1][val].equals((field[2][val])) && field[0][val].equals("") && field[1][val].equals(player1key)) {
                            buttons[0][val].setText(player2key);
                            flag = 1;
                            break;
                        }
                    }
                    break;
                case 6:
                    if (field[0][0].equals((field[1][1])) && field[2][2].equals("") && field[0][0].equals(player1key)) {
                        buttons[2][2].setText(player2key);
                        flag = 1;
                    } else if (field[0][0].equals((field[2][2])) && field[1][1].equals("") && field[0][0].equals(player1key)) {
                        buttons[1][1].setText(player2key);
                        flag = 1;
                    } else if (field[1][1].equals((field[2][2])) && field[0][0].equals("") && field[1][1].equals(player1key)) {
                        buttons[0][0].setText(player2key);
                        flag = 1;
                    }
                    break;
                case 7:
                    if (field[0][2].equals((field[1][1])) && field[2][0].equals("") && field[0][2].equals(player1key)) {
                        buttons[2][0].setText(player2key);
                        flag = 1;
                    } else if (field[0][2].equals((field[2][0])) && field[1][1].equals("") && field[0][2].equals(player1key)) {
                        buttons[1][1].setText(player2key);
                        flag = 1;
                    } else if (field[1][1].equals((field[2][0])) && field[0][2].equals("") && field[1][1].equals(player1key)) {
                        buttons[0][2].setText(player2key);
                        flag = 1;
                    }
                    break;
            }
            if(flag==1) {
                break;
            }
        }
        if(flag==0) {
            rand2 = new Random().nextInt(3);
            switch (rand2)
            {
                case 0:
                    if(buttons[1][1].getText().toString().trim().equalsIgnoreCase("")) {
                        buttons[1][1].setText(player2key);
                        break;
                    }
                case 1: case 2:
                while (true) {
                    int var1 = new Random().nextInt(3);
                    int var2 = new Random().nextInt(3);
                    if (buttons[var1][var2].getText().toString().trim().equalsIgnoreCase("")) {
                        buttons[var1][var2].setText(player2key);
                        break;
                    } else if (buttons[var2][var1].getText().toString().trim().equalsIgnoreCase("")) {
                        buttons[var2][var1].setText(player2key);
                        break;
                    }
                }
            }
        }
        roundcount++;
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for ( int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for(int i=0; i<3; i++) {
            if(field[i][0].equals((field[i][1])) && field[i][0].equals((field[i][2])) && !field[i][0].equals("")){
                return true;
            }
        }

        for(int i=0; i<3; i++) {
            if(field[0][i].equals((field[1][i])) && field[0][i].equals((field[2][i])) && !field[0][i].equals("")){
                return true;
            }
        }

        if(field[0][0].equals((field[1][1])) && field[0][0].equals((field[2][2])) && !field[0][0].equals("")){
            return true;
        }

        if(field[0][2].equals((field[1][1])) && field[0][2].equals((field[2][0])) && !field[0][2].equals("")){
            return true;
        }

        return false;
    }

    private void player1Wins() {
        player1Points++;
        Toast.makeText( context,P1_NAME + " Wins!", Toast.LENGTH_SHORT ).show();
        updatesPointsText();
        resetBoard();
        roundcompleted = true;
        firstmoveComputer();
    }

    private void player2Wins() {
        player2Points++;
        Toast.makeText( context,P2_NAME + " Wins!", Toast.LENGTH_SHORT ).show();
        updatesPointsText();
        resetBoard();
        if(player1key.equalsIgnoreCase("O")) {
            roundcompleted = true;
        }
        firstmoveComputer();
    }

    private void draw() {
        Toast.makeText( context,"Draw!", Toast.LENGTH_SHORT ).show();
        resetBoard();
        roundcompleted = true;
        firstmoveComputer();
    }

    private void updatesPointsText() {
        textViewPlayer1.setText(P1_NAME + ": " + player1Points);
        textViewPlayer2.setText(P2_NAME + ": " + player2Points);
    }

    private void resetBoard() {
        for (int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundcount = 0;
    }

    private void confirmMsg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Reset");
        builder.setMessage("Do You really want to reset the scores? (Can't be undone)");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetBoard();
                        player1Points = 0;
                        player2Points = 0;
                        P1_NAME = "Player";
                        P2_NAME = "Computer";
                        firstmoveComputer();
                        updatesPointsText();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void p1NameEdit() {
        tmpVariable="Player 1";
        getPlayerName();
    }

    private void getPlayerName() {
        LayoutInflater li = LayoutInflater.from(context);
        View prompt = li.inflate(R.layout.user_input_prompt, null);
        AlertDialog.Builder alertDb = new AlertDialog.Builder(context);
        alertDb.setView(prompt);
        final TextView txtView = (TextView) prompt.findViewById(R.id.enterName);
        txtView.setText("Enter "+ tmpVariable +" name:");
        final EditText userInput = (EditText) prompt.findViewById(R.id.textName);
        alertDb.setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                if(userInput.getText()!=null && !userInput.getText().toString().trim().equalsIgnoreCase("")) {
                                    if(tmpVariable.equalsIgnoreCase("Player 1")) {
                                        if(!tmpVariable.equalsIgnoreCase("")) {
                                            P1_NAME = userInput.getText().toString();
                                            Toast.makeText(context, "Hi Player1 " + " is now " +userInput.getText().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else if (tmpVariable.equalsIgnoreCase("Player 2")) {
                                        if(!tmpVariable.equalsIgnoreCase("")) {
                                            P2_NAME = userInput.getText().toString();
                                            Toast.makeText(context, "Hi Player2 " +" is now " +userInput.getText().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    //Toast.makeText(context, tmpVariable, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "Please enter a valid name!", Toast.LENGTH_SHORT).show();
                                }
                                tmpVariable="";
                                updatesPointsText();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDb.create();
        alert.show();

    }


    private void saveGame() {
        String p1Name = P1_NAME;
        String p2Name = P2_NAME;
        int p1Score = player1Points;
        int p2Score = player2Points;
        ContentValues cv = new ContentValues();
        cv.put(DBContract.PlayGameEntery.COLUMN_Player1, p1Name);
        cv.put(DBContract.PlayGameEntery.COLUMN_Player2, p2Name);
        cv.put(DBContract.PlayGameEntery.COLUMN_Player1_SCORE, p1Score);
        cv.put(DBContract.PlayGameEntery.COLUMN_Player2_SCORE, p2Score);
        cv.put(DBContract.PlayGameEntery.COLUMN_Game_Type, gameType);
        cv.put(DBContract.PlayGameEntery.COLUMN_Player1_KEY, player1key);
        cv.put(DBContract.PlayGameEntery.COLUMN_Player2_KEY, player2key);
        if(gameNo==0) {
            gameNo = ticTacToeOrc.genGameNo(mDatabase);
        }
        cv.put(DBContract.PlayGameEntery.COLUMN_Game_No, gameNo);
        if(gameId!=0) {
            mDatabase.update(DBContract.PlayGameEntery.TABLE_NAME, cv, DBContract.PlayGameEntery._ID + " = " + gameId, null);
        } else {
            String queryCheck = "select " + DBContract.PlayGameEntery._ID +  " from " + DBContract.PlayGameEntery.TABLE_NAME + " where " +
                    DBContract.PlayGameEntery.COLUMN_Game_No + " = " + gameNo ;
//            + "\" and " + DBContract.PlayGameEntery.COLUMN_Player1 + " = \"" + p1Name +"\" and " +
//                    DBContract.PlayGameEntery.COLUMN_Player1_SCORE + " = " + player1PointsInitial + " and " + DBContract.PlayGameEntery.COLUMN_Player2 + " = \"" + p2Name + "\" and "+
//                    DBContract.PlayGameEntery.COLUMN_Player2_SCORE + " = " + player2PointsInitial +" and " + DBContract.PlayGameEntery.COLUMN_Player1_KEY + " = \"" + player1key + "\" and " +
//                    DBContract.PlayGameEntery.COLUMN_Player2_KEY + " = \"" + player2key + "\"";
            Cursor cursor1 = mDatabase.rawQuery(queryCheck, null);
            if ((cursor1.moveToFirst()) || cursor1.getCount() !=0) {
                gameId = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(DBContract.PlayGameEntery._ID)));
            }
            if(gameId!=0) {
                mDatabase.update(DBContract.PlayGameEntery.TABLE_NAME, cv, DBContract.PlayGameEntery._ID + " = " + gameId, null);
            } else {
                mDatabase.insert(DBContract.PlayGameEntery.TABLE_NAME, null, cv);
                String query = "select " + DBContract.PlayGameEntery._ID + " from " + DBContract.PlayGameEntery.TABLE_NAME + " order by " + DBContract.PlayGameEntery.COLUMN_TIMESTAMP + " DESC limit 1";
                Cursor cursor = mDatabase.rawQuery(query, null);
                if ((cursor.moveToFirst()) || cursor.getCount() != 0) {
                    gameId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBContract.PlayGameEntery._ID)));
                }
            }
        }
        player1PointsInitial = player1Points;
        player2PointsInitial = player2Points;

        Toast.makeText(context, "Progress Saved Successfully!", Toast.LENGTH_SHORT).show();
    }

    private void saveCurrGameState() {
        String p1Name = P1_NAME;
        String p2Name = P2_NAME;
        int p1Score = player1Points;
        int p2Score = player2Points;
        String p1Key = player1key;
        String p2Key = player2key;



        ContentValues cv = new ContentValues();
        cv.put(DBContract.LastGameEntry.COLUMN_Player1, p1Name);
        cv.put(DBContract.LastGameEntry.COLUMN_Player2, p2Name);
        cv.put(DBContract.LastGameEntry.COLUMN_Player1_SCORE, p1Score);
        cv.put(DBContract.LastGameEntry.COLUMN_Player2_SCORE, p2Score);
        cv.put(DBContract.LastGameEntry.COLUMN_Player1_SCORE_INITIAL, player1PointsInitial);
        cv.put(DBContract.LastGameEntry.COLUMN_Player2_SCORE_INITIAL, player2PointsInitial);
        cv.put(DBContract.LastGameEntry.COLUMN_Game_Type, gameType);
        cv.put(DBContract.LastGameEntry.COLUMN_Player1_KEY, p1Key);
        cv.put(DBContract.LastGameEntry.COLUMN_Player2_KEY, p2Key);
        cv.put(DBContract.LastGameEntry.COLUMN_BoardValus, boardValues);
        cv.put(DBContract.LastGameEntry.COLUMN_ROUND_COUNT, roundcount);
        cv.put(DBContract.LastGameEntry.COLUMN_PLAYER1_TURN, player1Turn);
        if(gameNo==0) {
            gameNo = ticTacToeOrc.genGameNo(mDatabase);
        }
        cv.put(DBContract.LastGameEntry.COLUMN_Game_No, gameNo);
        mDatabase.delete(DBContract.LastGameEntry.TABLE_NAME,null,null);
        mDatabase.insert(DBContract.LastGameEntry.TABLE_NAME, null, cv);
        Toast.makeText(context, "Progress Saved Successfully!", Toast.LENGTH_SHORT).show();
        sendUpdates(context);
    }

    public void sendUpdates(Context context) {
        Intent intent = new Intent(context, TicTacToeResumeWidget.class);
        intent.setAction("DB_Changed");
        context.sendBroadcast(intent);
    }

}