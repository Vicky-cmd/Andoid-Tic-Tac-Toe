package com.infotrends.in.tictactoe.ui.load;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.in.tictactoe.R;
import com.infotrends.in.tictactoe.database.DBContract;
import com.infotrends.in.tictactoe.database.DBHelper;
import com.infotrends.in.tictactoe.database.TicTacToeAdapter;

public class Loadfragment extends Fragment {

    private SQLiteDatabase mDatabase;
    private TicTacToeAdapter mAdapter;

    private View root;
    Context context;


    private String P1_SCORE = "p1_score";
    private String P2_SCORE = "p2_score";

    private NavController navController;
    private NavHostFragment navHostFragment;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_load_saved, container, false);
        context = getActivity();
        DBHelper dbHelper = new DBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = root.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new  TicTacToeAdapter(context, getAllItems());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                TableLayout tlayout = root.findViewById(R.id.recycle_view_table);
//                tlayout.setBackgroundColor(Color.parseColor("#ffdd00"));
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((String) viewHolder.itemView.getTag(R.string.id), viewHolder);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewBackground_load.setVisibility(View.INVISIBLE);
                    ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewBackground.setVisibility(View.VISIBLE);
                    final View foregroundView = ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewForeground;

                    getDefaultUIUtil().onSelected(foregroundView);
                }
            }
            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewForeground;
                getDefaultUIUtil().clearView(foregroundView);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewForeground;

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }

        }).attachToRecyclerView(recyclerView);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Bundle savedInstanceState = new Bundle();
                savedInstanceState.putInt(P1_SCORE, (Integer) viewHolder.itemView.getTag(R.string.p1Score));
                savedInstanceState.putInt(P2_SCORE, (Integer) viewHolder.itemView.getTag(R.string.p2Score));
                savedInstanceState.putString("P1_NAME", (String) viewHolder.itemView.getTag(R.string.p1Name));
                savedInstanceState.putString("P2_NAME", (String) viewHolder.itemView.getTag(R.string.p2Name));
                savedInstanceState.putInt("gameNo",(Integer) viewHolder.itemView.getTag(R.string.gameNo));
                savedInstanceState.putInt("gameId",  Integer.parseInt((String) viewHolder.itemView.getTag(R.string.id)));
                if(((String) viewHolder.itemView.getTag(R.string.gametype)).equalsIgnoreCase("1")) {
                    navController.navigate(R.id.Load_Fragment_to_Play_Fragment, savedInstanceState);
                } else if(((String) viewHolder.itemView.getTag(R.string.gametype)).equalsIgnoreCase("2")) {
                    savedInstanceState.putString("player1key", (String) viewHolder.itemView.getTag(R.string.player1_key));
                    savedInstanceState.putString("player2key", (String) viewHolder.itemView.getTag(R.string.player2_key));
                    navController.navigate(R.id.Load_Fragment_to_Play_Fragment_Vs_Comp, savedInstanceState);
                }
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewBackground_load.setVisibility(View.VISIBLE);
                    ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewBackground.setVisibility(View.INVISIBLE);
                    final View foregroundView = ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewForeground;

                    getDefaultUIUtil().onSelected(foregroundView);
                }
            }
            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewForeground;
                getDefaultUIUtil().clearView(foregroundView);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((TicTacToeAdapter.PlayerGameHolder) viewHolder).viewForeground;

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }

        }).attachToRecyclerView(recyclerView);

        return root;
    }

    private Cursor getAllItems() {
        return  mDatabase.query(
                DBContract.PlayGameEntery.TABLE_NAME, null, null,
                null, null, null,
                DBContract.PlayGameEntery.COLUMN_TIMESTAMP + " DESC"
        );
    }
    private void removeItem(String id, RecyclerView.ViewHolder viewHolder) {
        mDatabase.delete(DBContract.PlayGameEntery.TABLE_NAME,
                DBContract.PlayGameEntery._ID + "=" +id, null);
        mAdapter.swapCursor(getAllItems());
    }
}
