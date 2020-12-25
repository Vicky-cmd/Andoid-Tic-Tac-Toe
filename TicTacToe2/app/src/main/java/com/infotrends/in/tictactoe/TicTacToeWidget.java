package com.infotrends.in.tictactoe;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.in.tictactoe.R;


public class TicTacToeWidget extends AppWidgetProvider {




    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tic_tac_toe_widget);
            // Construct an Intent object includes web adresss.
            Intent intent = new Intent(context, MainActivity.class);
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName(context.getPackageName(),
                    "com.infotrends.in.tictactoe.MainActivity"));


            // In widget we are not allowing to use intents as usually. We have to use PendingIntent instead of 'startActivity'
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);
            // Here the basic operations the remote view can do.
            views.setOnClickPendingIntent(R.id.appwidget_layout, pendingIntent);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
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
}

