package com.infotrends.in.tictactoe.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.in.tictactoe.R;

public class GameModeSelFragment extends Fragment implements View.OnClickListener {

    View root;
    Context context;
    private NavController navController;
    private NavHostFragment navHostFragment;

    private String player1key="X";
    private String player2key="O";

    Button singleplayer, doubleplayer;
    ToggleButton gModeBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.game_type_sel_fragment, container, false);
        context = getActivity();

        singleplayer = root.findViewById(R.id.singleplayer);
        singleplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSingleGame();
            }
        });
        doubleplayer = root.findViewById(R.id.doubleplayer);
        doubleplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDoubleGame();
            }
        });

        gModeBtn = root.findViewById(R.id.selectgamemode);
        gModeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    player1key = "O";
                    player2key = "X";
                } else {
                    player1key = "X";
                    player2key = "O";

                }
            }
        });

        navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        return root;
    }

    @Override
    public void onClick(View v) {

    }

    private void loadSingleGame() {
        Bundle arguments = new Bundle();
        arguments.putString("player1key",player1key);
        arguments.putString("player2key",player2key);
        navController.navigate(R.id.Main_Activity_to_Tic_Tac_Toe_Vs_Comp_View, arguments);
    }

    private void loadDoubleGame() {
        Bundle arguments = new Bundle();
        navController.navigate(R.id.Main_Activity_to_Tic_Tac_Toe_View);
    }


}
