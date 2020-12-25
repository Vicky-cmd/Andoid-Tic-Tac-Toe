package com.infotrends.in.tictactoe.ui.send;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.in.tictactoe.R;
import com.infotrends.in.tictactoe.orchestrator.SendOrc;

public class ShareFragment extends Fragment {

    Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Toast.makeText(context, "Share via...", Toast.LENGTH_SHORT);
        SendOrc sOrc = new SendOrc();sOrc.shareApp(context);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
    }

}
