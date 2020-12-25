package com.infotrends.in.tictactoe.database;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.in.tictactoe.R;

public class TicTacToeAdapter extends RecyclerView.Adapter<TicTacToeAdapter.PlayerGameHolder> {

    private Context mContext;
    private Cursor mCursor;

    public TicTacToeAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class PlayerGameHolder extends RecyclerView.ViewHolder {

        public TextView player1name;
        public TextView player2name;
        public TextView player1score;
        public TextView player2score;
        public TextView gameType, p1Key;
        public RelativeLayout viewBackground, viewForeground, viewBackground_load;


        public PlayerGameHolder(@NonNull View itemView) {
            super(itemView);
            player1name = itemView.findViewById(R.id.textview_P1_name);
            player2name = itemView.findViewById(R.id.textview_P2_name);
            player1score = itemView.findViewById(R.id.textview_P1_score);
            player2score = itemView.findViewById(R.id.textview_P2_score);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            viewBackground_load = itemView.findViewById(R.id.view_background_load);
            gameType = itemView.findViewById(R.id.textview_game_type);
            p1Key = itemView.findViewById(R.id.textview_p1Key);
        }
    }

    @NonNull
    @Override
    public PlayerGameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.load_recycle_view, parent, false);
        return new PlayerGameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerGameHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String p1Name = mCursor.getString(mCursor.getColumnIndex(DBContract.PlayGameEntery.COLUMN_Player1));
        String p2Name = mCursor.getString(mCursor.getColumnIndex(DBContract.PlayGameEntery.COLUMN_Player2));
        int p1Score = mCursor.getInt(mCursor.getColumnIndex(DBContract.PlayGameEntery.COLUMN_Player1_SCORE));
        int p2Score = mCursor.getInt(mCursor.getColumnIndex(DBContract.PlayGameEntery.COLUMN_Player2_SCORE));
        String id = mCursor.getString(mCursor.getColumnIndex((DBContract.PlayGameEntery._ID)));
        String gameType = mCursor.getString(mCursor.getColumnIndex(DBContract.PlayGameEntery.COLUMN_Game_Type));
        String player1Type = mCursor.getString(mCursor.getColumnIndex(DBContract.PlayGameEntery.COLUMN_Player1_KEY));
        String player2Type = mCursor.getString(mCursor.getColumnIndex(DBContract.PlayGameEntery.COLUMN_Player2_KEY));
        int gameNo = mCursor.getInt(mCursor.getColumnIndex(DBContract.PlayGameEntery.COLUMN_Game_No));

        String p1KeyStr = " ";
        if(player1Type.equalsIgnoreCase("X") || player1Type.equalsIgnoreCase("O")) {
            p1KeyStr = player1Type;
        }
        String gType = "";
        if(gameType.equalsIgnoreCase("1")) {
            gType = "D";
        } else if(gameType.equalsIgnoreCase("2")) {
            gType = "S";
        }

        holder.player1name.setText(p1Name);
        holder.player2name.setText(p2Name);
        holder.player1score.setText(String.valueOf(p1Score));
        holder.player2score.setText(String.valueOf(p2Score));
        holder.gameType.setText("Type: " + gType);
        holder.p1Key.setText("Player1 Key: " + p1KeyStr);

        holder.itemView.setTag(R.string.id,id);
        holder.itemView.setTag(R.string.p1Score,p1Score);
        holder.itemView.setTag(R.string.p2Score,p2Score);
        holder.itemView.setTag(R.string.p1Name,p1Name);
        holder.itemView.setTag(R.string.p2Name,p2Name);
        holder.itemView.setTag(R.string.gametype, gameType);
        holder.itemView.setTag(R.string.player1_key, player1Type);
        holder.itemView.setTag(R.string.player2_key, player2Type);
        holder.itemView.setTag(R.string.gameNo, gameNo);
    }

    @Override
    public int getItemCount()  {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if(newCursor != null) {
            notifyDataSetChanged();
        }
    }

}
