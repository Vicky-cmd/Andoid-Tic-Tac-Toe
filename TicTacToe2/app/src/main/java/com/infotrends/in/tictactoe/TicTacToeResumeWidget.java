package com.infotrends.in.tictactoe;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.in.tictactoe.R;
import com.infotrends.in.tictactoe.database.DBContract;
import com.infotrends.in.tictactoe.database.DBHelper;
import com.infotrends.in.tictactoe.ui.play.TicTacViewModel;

public class TicTacToeResumeWidget  extends AppWidgetProvider {

    private SQLiteDatabase mDatabase;
    private TicTacViewModel ticTacViewModel;

    public void updateAction(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tic_tac_toe_resume_widget);


        DBHelper dbHelper = new DBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();

        Cursor mCursor = getRecentGame(context);
        if((mCursor.moveToFirst()) || mCursor.getCount() !=0) {
            ticTacViewModel = new TicTacViewModel(mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry._ID)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Game_Type)),
                    mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2)),
                    mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_SCORE)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_SCORE)),
                    mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_KEY)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_KEY)),
                    mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_BoardValus)),mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_ROUND_COUNT)),
                    mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_PLAYER1_TURN)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_SCORE_INITIAL)),
                    mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_SCORE_INITIAL)),mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Game_No)));
            if(ticTacViewModel.getId()!=null && !ticTacViewModel.getId().equalsIgnoreCase("")) {
                views.setTextViewText(R.id.wid_res_p1,ticTacViewModel.getPlayer1());
                views.setTextViewText(R.id.wid_res_p2,ticTacViewModel.getPlayer2());
                views.setTextViewText(R.id.wid_res_score_p1,ticTacViewModel.getPlayer1_score());
                views.setTextViewText(R.id.wid_res_score_p2,ticTacViewModel.getPlayer2_score());
            }
        } else {
            views.setTextViewText(R.id.wid_res_p1,"Player1");
            views.setTextViewText(R.id.wid_res_p2,"Player2");
            views.setTextViewText(R.id.wid_res_score_p1,"0");
            views.setTextViewText(R.id.wid_res_score_p2,"0");
        }

        // Construct an Intent object includes web adresss.
        Intent intent = new Intent(context, MainActivity.class);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(new ComponentName(context.getPackageName(),
                "com.infotrends.in.tictactoe.MainActivity"));
        intent.putExtra("widgetaction","resumeActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // In widget we are not allowing to use intents as usually. We have to use PendingIntent instead of 'startActivity'
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Here the basic operations the remote view can do.
        views.setOnClickPendingIntent(R.id.wid_res_layout_text, pendingIntent);

        Intent intentIcon = new Intent(context, MainActivity.class);
        intentIcon.addCategory("android.intent.category.LAUNCHER");
        intentIcon.setComponent(new ComponentName(context.getPackageName(),
                "com.infotrends.in.tictactoe.MainActivity"));

        // In widget we are not allowing to use intents as usually. We have to use PendingIntent instead of 'startActivity'
        PendingIntent pendingIntentIcon = PendingIntent.getActivity(context, 1, intentIcon, 0);
        // Here the basic operations the remote view can do.
        views.setOnClickPendingIntent(R.id.appwidget_img_wid, pendingIntentIcon);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAction(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("DB_Changed")) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, TicTacToeResumeWidget.class));
            this.onUpdate(context, appWidgetManager, ids);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
//            TicTacToeWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }



    private Cursor getRecentGame(Context context) {
        return  mDatabase.query(
                DBContract.LastGameEntry.TABLE_NAME, null, null,
                null, null, null,
                DBContract.LastGameEntry.COLUMN_TIMESTAMP + " DESC limit 1"
        );
    }

}


