package com.infotrends.in.tictactoe.ui.home;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.in.tictactoe.R;
import com.infotrends.in.tictactoe.database.DBContract;
import com.infotrends.in.tictactoe.database.DBHelper;
import com.infotrends.in.tictactoe.database.TicTacToeAdapter;
import com.infotrends.in.tictactoe.orchestrator.TicTacToeOrc;
import com.infotrends.in.tictactoe.ui.play.TicTacViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    View root;
    Context context;
    private NavController navController;
    private NavHostFragment navHostFragment;

    private SQLiteDatabase mDatabase;
    private TicTacToeAdapter mAdapter;

    private TicTacViewModel ticTacViewModel;

    Button playgame, loadgame, resumegame, settings;
    TicTacToeOrc ticTacToeOrc = new TicTacToeOrc();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        context= getActivity();

        navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        playgame = root.findViewById(R.id.playgame);
        resumegame = root.findViewById(R.id.resumegame);
        loadgame = root.findViewById(R.id.loadnewgame);
        settings = root.findViewById(R.id.settings);

        DBHelper dbHelper = new DBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        Handler h1 = new Handler();
        h1.post(new Runnable() {
            @Override
            public void run() {
                Cursor mCursor = ticTacToeOrc.getRecentGame(context, mDatabase);
                if((mCursor.moveToFirst()) || mCursor.getCount() !=0) {
                    ticTacViewModel = new TicTacViewModel(mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry._ID)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Game_Type)),
                            mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2)),
                            mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_SCORE)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_SCORE)),
                            mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_KEY)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_KEY)),
                            mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_BoardValus)),mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_ROUND_COUNT)),
                            mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_PLAYER1_TURN)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_SCORE_INITIAL)),
                            mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_SCORE_INITIAL)),mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Game_No)));
                    if(ticTacViewModel.getId()!=null && !ticTacViewModel.getId().equalsIgnoreCase("")) {
                        resumegame.setEnabled(true);
                    }
                }
            }
        });

        playgame.setOnClickListener(this);

        resumegame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticTacToeOrc.resumeActivity(navController, ticTacViewModel);
            }
        });

        loadgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.Main_Activity_to_Load_Game_View);
            }
        });
        setHasOptionsMenu(true);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.Main_Activity_to_Settings_View);
            }
        });

        return root;
    }

    @Override
    public void onPause() {
        getActivity().getIntent().putExtra("widgetaction", "");
        super.onPause();
    }

    private void resumeActivity() {
        {
            Bundle bundle = new Bundle();
            bundle.putInt("p1_score",Integer.parseInt(ticTacViewModel.getPlayer1_score()));
            bundle.putInt("p2_score",Integer.parseInt(ticTacViewModel.getPlayer2_score()));
            bundle.putString("P1_NAME",ticTacViewModel.getPlayer1());
            bundle.putString("P2_NAME",ticTacViewModel.getPlayer2());
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



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.play_game:
                return false;
            case R.id.game_reset:
                return false;
            case R.id.game_clear:
                return false;
            case R.id.resetdata:
                mDatabase.delete(DBContract.LastGameEntry.TABLE_NAME,null,null);
                resumegame.setEnabled(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View v) {
        navController.navigate(R.id.Main_Activity_to_selct_mode_view);
    }
}