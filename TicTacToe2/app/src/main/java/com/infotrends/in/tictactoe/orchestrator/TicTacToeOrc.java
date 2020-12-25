package com.infotrends.in.tictactoe.orchestrator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.navigation.NavController;

import com.in.tictactoe.R;
import com.infotrends.in.tictactoe.database.DBContract;
import com.infotrends.in.tictactoe.ui.play.TicTacViewModel;

import java.util.ArrayList;

public class TicTacToeOrc {


    public void resumeActivity(NavController navController, TicTacViewModel ticTacViewModel) {
        {
            Bundle bundle = new Bundle();
            bundle.putInt("p1_score",Integer.parseInt(ticTacViewModel.getPlayer1_score()));
            bundle.putInt("p2_score",Integer.parseInt(ticTacViewModel.getPlayer2_score()));
            bundle.putInt("p1_scoreInitial",Integer.parseInt(ticTacViewModel.getPlayer1_score_initial()));
            bundle.putInt("p2_scoreInitial",Integer.parseInt(ticTacViewModel.getPlayer2_score_initial()));
            bundle.putInt("gameNo",Integer.parseInt(ticTacViewModel.getGame_no()));
            bundle.putString("P1_NAME",ticTacViewModel.getPlayer1());
            bundle.putString("P2_NAME",ticTacViewModel.getPlayer2());
//            bundle.putInt("gameId", Integer.parseInt(ticTacViewModel.getId()));
            ArrayList tmp = new ArrayList();
            String boxArr = ticTacViewModel.getBoard_values();
            int idx=0;
            int blen = boxArr.length();
            boolean flg= false;
            for(int i=0; i<3; i++) {
                for(int j=0; j<3; j++) {
                    if(idx>=blen) {
                        flg=true;
                        break;
                    }
                    if(boxArr.charAt(idx)== 48) {
                        tmp.add("O");
                    }
                    else if(boxArr.charAt(idx)== 49) {
                        tmp.add("X");
                    }
                    else
                        tmp.add("");
                    idx++;
                }
                if(flg) {
                    break;
                }
            }
            bundle.putIntegerArrayList("BOXLst", tmp);
//        savedInstanceState.putBoolean("P1Chance",player1Turn);
//            bundle.putInt("gameId", Integer.parseInt(ticTacViewModel.getId()));
            bundle.putInt("RoundCount",Integer.parseInt(ticTacViewModel.getRound_count()));
            bundle.putBoolean("P1Chance",Integer.parseInt(ticTacViewModel.getPlayer1Turn())==1);
            bundle.putString("player1key", ticTacViewModel.getPlayer1_key());
            bundle.putString("player2key", ticTacViewModel.getPlayer2_key());
            if(ticTacViewModel.getGame_type().equalsIgnoreCase("1")) {
                navController.navigate(R.id.Main_Activity_to_Tic_Tac_Toe_View, bundle);
            } else if(ticTacViewModel.getGame_type().equalsIgnoreCase("2")) {
                navController.navigate(R.id.Main_Activity_to_Tic_Tac_Toe_Vs_Comp_View, bundle);
            }
        }
    }

    public Cursor getRecentGame(Context context, SQLiteDatabase mDatabase) {
        return  mDatabase.query(
                DBContract.LastGameEntry.TABLE_NAME, null, null,
                null, null, null,
                DBContract.LastGameEntry.COLUMN_TIMESTAMP + " DESC limit 1"
        );
    }

    public Cursor loadLastSavedGame(Context context, SQLiteDatabase mDatabase) {
        return  mDatabase.query(
                DBContract.PlayGameEntery.TABLE_NAME, null, null,
                null, null, null,
                DBContract.PlayGameEntery.COLUMN_TIMESTAMP + " DESC limit 1"
        );
    }

    public int genGameNo(SQLiteDatabase mDatabase) {
        int gameNo=0;
        ContentValues cv = new ContentValues();
        cv.put(DBContract.NoGenerator.COLUMN_NO_TYPE, "Game_No");
        String query = "select " + DBContract.NoGenerator.COLUMN_NO_GENERATED + " from " + DBContract.NoGenerator.TABLE_NAME + " where " + DBContract.NoGenerator.COLUMN_NO_TYPE + " = \" Game_No\"";
        Cursor cursor1 = mDatabase.rawQuery(query, null);
        if ((cursor1.moveToFirst()) || cursor1.getCount() !=0) {
            gameNo = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(DBContract.NoGenerator.COLUMN_NO_GENERATED)));
            if(gameNo>=0) {
                cv.put(DBContract.NoGenerator.COLUMN_NO_GENERATED, gameNo+1);
                mDatabase.update(DBContract.NoGenerator.TABLE_NAME, cv, DBContract.NoGenerator.COLUMN_NO_TYPE + " = \" Game_No\"", null);
            }
        }
        return gameNo;
    }
}
