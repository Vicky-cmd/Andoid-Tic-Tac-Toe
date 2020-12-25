package com.infotrends.in.tictactoe.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.in.tictactoe.R;
import com.infotrends.in.tictactoe.TicTacToeResumeWidget;
import com.infotrends.in.tictactoe.database.DBContract;
import com.infotrends.in.tictactoe.database.DBHelper;
import com.infotrends.in.tictactoe.database.TicTacToeAdapter;

public class SettingsFragment extends Fragment implements View.OnClickListener  {

    View root;
    Context context;
    private NavController navController;
    private NavHostFragment navHostFragment;

    private SQLiteDatabase mDatabase;
    private TicTacToeAdapter mAdapter;

    private Switch resetSwitch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings, container, false);
        context = getActivity();

        navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        DBHelper dbHelper = new DBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        resetSwitch = root.findViewById(R.id.res_switch);
        Cursor mCursor = getRecentGame();
        Cursor mCursor1 = getSavedGame();
        if(((mCursor.moveToFirst()) || mCursor.getCount() !=0) || ((mCursor1.moveToFirst()) || mCursor1.getCount() !=0)) {
            resetSwitch.setEnabled(true);
        } else {
            resetSwitch.setEnabled(false);
        }
        resetSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Reset");
                    builder.setMessage("Do You really want to reset the scores? (Can't be undone)");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDatabase.delete(DBContract.LastGameEntry.TABLE_NAME,null,null);
                                    mDatabase.delete(DBContract.PlayGameEntery.TABLE_NAME,null,null);
                                    resetSwitch.setChecked(false);
                                    resetSwitch.setEnabled(false);
                                    sendUpdates(context);
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
            }
        });
        return root;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStop() {
        sendUpdates(context);
        super.onStop();
    }



    private Cursor getRecentGame() {
        sendUpdates(context);
        return  mDatabase.query(
                DBContract.LastGameEntry.TABLE_NAME, null, null,
                null, null, null,
                DBContract.LastGameEntry.COLUMN_TIMESTAMP + " DESC limit 1"
        );
    }

    private Cursor getSavedGame() {
        return  mDatabase.query(
                DBContract.PlayGameEntery.TABLE_NAME, null, null,
                null, null, null,
                DBContract.PlayGameEntery.COLUMN_TIMESTAMP + " DESC limit 1"
        );
    }


    public void sendUpdates(Context context) {
        Intent intent = new Intent(context, TicTacToeResumeWidget.class);
        intent.setAction("DB_Changed");
        context.sendBroadcast(intent);
    }

}
