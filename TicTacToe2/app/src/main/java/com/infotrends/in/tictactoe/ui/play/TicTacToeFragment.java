package com.infotrends.in.tictactoe.ui.play;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class TicTacToeFragment extends Fragment implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];

    private boolean player1Turn = true;

    private final String gameType= "1";
    private int gameId = 0;

    private int roundcount;

    private int player1Points;
    private int player2Points;

    private int player1PointsInitial;
    private int player2PointsInitial;

    private int gameNo;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    private String P1_NAME = "Player1";
    private String P2_NAME = "Player2";

    private String P1_SCORE = "p1_score";
    private String P2_SCORE = "p2_score";

    private String tmpVariable ="";

    private MenuItem resetGame;
    private MenuItem gameClear;

    private String boardValues="";

    private SQLiteDatabase mDatabase;
    private TicTacToeAdapter mAdapter;
    private TicTacToeOrc ticTacToeOrc = new TicTacToeOrc();

    private View root;
    Context context;
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

//        Button buttonReset = root.findViewById(R.id.button_reset);
//        buttonReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                confirmMsg();
//
//            }
//        });
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
        textViewPlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2NameEdit();
            }
        });

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
            player1Turn = savedInstanceState.getBoolean("P1Chance");
            roundcount = savedInstanceState.getInt("RoundCount");
            gameId = savedInstanceState.getInt("gameId");
            updatesPointsText();
        }
        else if(getArguments()!=null) {
            gameId = getArguments().getInt("gameId");
            gameNo = getArguments().getInt("gameNo");
            P1_NAME = getArguments().getString("P1_NAME");
            P2_NAME = getArguments().getString("P2_NAME");
            player1Points = getArguments().getInt(P1_SCORE);
            player2Points = getArguments().getInt(P2_SCORE);
            player1PointsInitial = getArguments().getInt("p1_scoreInitial");
            player2PointsInitial = getArguments().getInt("p2_scoreInitial");
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
                player1Turn = getArguments().getBoolean("P1Chance");
            }
            if(getArguments().getInt("RoundCount")>=0) {
                roundcount = getArguments().getInt("RoundCount");
            }
//            if(!getArguments().getBoolean("P1Chance")) {
//
//            }
            updatesPointsText();
        }
        setHasOptionsMenu(true);

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
            player1PointsInitial = savedInstanceState.getInt("p1_scoreInitial");
            player2PointsInitial = savedInstanceState.getInt("p2_scoreInitial");
            P1_NAME = savedInstanceState.getString("P1_NAME");
            P2_NAME = savedInstanceState.getString("P2_NAME");
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
        savedInstanceState.putInt("gameNo", gameNo);
        savedInstanceState.putString("P1_NAME",P1_NAME);
        savedInstanceState.putString("P2_NAME",P2_NAME);
        ArrayList tmp = new ArrayList();
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                if(buttons[i][j].getText()!=null)
                    tmp.add(buttons[i][j].getText());
                else
                    tmp.add("");
            }
        }
        savedInstanceState.putInt("gameId",gameId);
        savedInstanceState.putIntegerArrayList("BOXLst", tmp);
        savedInstanceState.putBoolean("P1Chance",player1Turn);
        savedInstanceState.putInt("RoundCount",roundcount);
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

        if(player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }

        roundcount++;

        if(checkForWin()) {
            if(player1Turn) {
                player1Wins();
            }
            else {
                player2Wins();
            }
        } else if (roundcount==9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }
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
    };

    private void player2Wins() {
        player2Points++;
        Toast.makeText( context,P2_NAME + " Wins!", Toast.LENGTH_SHORT ).show();
        updatesPointsText();
        resetBoard();
    }

    private void draw() {
        Toast.makeText( context,"Draw!", Toast.LENGTH_SHORT ).show();
        resetBoard();

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
        player1Turn = true;
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
                        P1_NAME = "Player1";
                        P2_NAME = "Player2";
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
//        if(!tmpVariable.equalsIgnoreCase("")) {
//            P1_NAME = tmpVariable;
//        }
//        tmpVariable="";
//        updatesPointsText();
    }

    private void p2NameEdit() {
        tmpVariable="Player 2";
        getPlayerName();
//        Toast.makeText(context, tmpVariable, Toast.LENGTH_SHORT).show();
//        if(!tmpVariable.equalsIgnoreCase("")) {
//            P2_NAME = tmpVariable;
//            Toast.makeText(context, tmpVariable, Toast.LENGTH_SHORT).show();
//        }
//        tmpVariable="";
//        Toast.makeText(context, "Hi"+tmpVariable, Toast.LENGTH_SHORT).show();
//        updatesPointsText();
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
        String player1Key = null;
        String player2Key = null;

        ContentValues cv = new ContentValues();
        cv.put(DBContract.PlayGameEntery.COLUMN_Player1, p1Name);
        cv.put(DBContract.PlayGameEntery.COLUMN_Player2, p2Name);
        cv.put(DBContract.PlayGameEntery.COLUMN_Player1_SCORE, p1Score);
        cv.put(DBContract.PlayGameEntery.COLUMN_Player2_SCORE, p2Score);
        cv.put(DBContract.PlayGameEntery.COLUMN_Game_Type, gameType);
        cv.put(DBContract.PlayGameEntery.COLUMN_Player1_KEY, "");
        cv.put(DBContract.PlayGameEntery.COLUMN_Player2_KEY, "");
        if(gameNo==0) {
            gameNo = ticTacToeOrc.genGameNo(mDatabase);
        }
        cv.put(DBContract.PlayGameEntery.COLUMN_Game_No, gameNo);
        if(gameId!=0) {
            mDatabase.update(DBContract.PlayGameEntery.TABLE_NAME, cv, DBContract.PlayGameEntery._ID + " = " + gameId, null);
        } else {
            String queryCheck = "select " + DBContract.PlayGameEntery._ID +  " from " + DBContract.PlayGameEntery.TABLE_NAME + " where " +
                    DBContract.PlayGameEntery.COLUMN_Game_No + " = " + gameNo;
//                    + "\" and " + DBContract.PlayGameEntery.COLUMN_Player1 + " = \"" + p1Name +"\" and " +
//                    DBContract.PlayGameEntery.COLUMN_Player1_SCORE + " = " + player1PointsInitial + " and " + DBContract.PlayGameEntery.COLUMN_Player2 + " = \"" + p2Name + "\" and "+
//                    DBContract.PlayGameEntery.COLUMN_Player2_SCORE + " = " + player2PointsInitial;
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
        String p1Key = "";
        String p2Key = "";

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
//        cv.put(DBContract.LastGameEntry.COLUMN_BoardValus, boardValues);
        mDatabase.delete(DBContract.LastGameEntry.TABLE_NAME,null,null);
        long rowInserted = mDatabase.insert(DBContract.LastGameEntry.TABLE_NAME, null, cv);
//                mDatabase.query(
//                DBContract.LastGameEntry.TABLE_NAME, null, null,
//                null, null, null,
//                DBContract.LastGameEntry.COLUMN_TIMESTAMP + " DESC limit 1"
//        );
        sendUpdates(context);
    }

    public void sendUpdates(Context context) {
        Intent intent = new Intent(context, TicTacToeResumeWidget.class);
        intent.setAction("DB_Changed");
        context.sendBroadcast(intent);
    }

}
