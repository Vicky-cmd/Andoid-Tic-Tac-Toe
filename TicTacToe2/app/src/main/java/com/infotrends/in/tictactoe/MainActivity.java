package com.infotrends.in.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.in.tictactoe.R;
import com.infotrends.in.tictactoe.database.DBContract;
import com.infotrends.in.tictactoe.database.DBHelper;
import com.infotrends.in.tictactoe.orchestrator.SendOrc;
import com.google.android.material.navigation.NavigationView;
import com.infotrends.in.tictactoe.orchestrator.TicTacToeOrc;
import com.infotrends.in.tictactoe.ui.play.TicTacViewModel;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    private int Fragcount=0;

    private NavController navController;
    private NavHostFragment navHostFragment;
    private AppBarConfiguration mAppBarConfiguration;
    private TicTacViewModel ticTacViewModel;
    private MenuItem resetGame;
    private MenuItem gameClear;

    private SQLiteDatabase mDatabase;
    private TicTacToeOrc ticTacToeOrc = new TicTacToeOrc();

    private boolean consumedIntent;
    private final String SAVED_INST_STATE_CONSUMED_INTENT = "SAVED_INSTANCE_STATE_CONSUMED_INTENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null) {
            consumedIntent = savedInstanceState.getBoolean(SAVED_INST_STATE_CONSUMED_INTENT);
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
//        toolbar.setLogo(R.mipmap.ic_launcher_round);
        setSupportActionBar(toolbar);
//        mTextView = findViewById(R.id.text_home);
//        HomeFragment firstFragment = new HomeFragment();
//        firstFragment.setArguments(getIntent().getExtras());
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.nav_host_fragment, firstFragment).commit();
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.game_mode_select_view, R.id.loadfragment, R.id.settings, R.id.send_fragment, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

//        if(this.getIntent()!=null) {
//            Intent intent = this.getIntent();
//            Bundle val = intent.getExtras();
//            if(val!=null &&  val.containsKey("widgetaction")) {
//                if(val.getString("widgetaction")!=null && val.getString("widgetaction").equalsIgnoreCase("resumeActivity")) {
//                    Bundle args = new Bundle();
//                    args.putString("widgetaction", "resumeActivity");
//                    navController.navigate(R.id.Main_Activity_View, args);
//                }
//            }
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SAVED_INST_STATE_CONSUMED_INTENT, consumedIntent);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        this.getIntent().putExtra("widgetaction", "");
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getExtras()!=null) {
            Bundle bdl = intent.getExtras();
            if(bdl!=null && bdl.containsKey("widgetaction")) {
                String val = bdl.getString("widgetaction");
                if(val.equalsIgnoreCase("resumeActivity")) {
                    intent.putExtra("widgetaction", "");
                    this.getIntent().putExtra("widgetaction", "");
                    Cursor mCursor = ticTacToeOrc.getRecentGame(this, mDatabase);
                    if((mCursor.moveToFirst()) || mCursor.getCount() !=0) {
                        ticTacViewModel = new TicTacViewModel(mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry._ID)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Game_Type)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_SCORE)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_SCORE)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_KEY)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_KEY)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_BoardValus)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_ROUND_COUNT)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_PLAYER1_TURN)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_SCORE_INITIAL)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_SCORE_INITIAL)),mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Game_No)));
                        if (ticTacViewModel.getId() != null && !ticTacViewModel.getId().equalsIgnoreCase("")) {
                            navController.navigateUp();
                            ticTacToeOrc.resumeActivity(navController, ticTacViewModel);
                        }
                    }
                    consumedIntent = false;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        if(this.getIntent()!=null) {
            Intent intent = this.getIntent();
            boolean lfromHistory = intent != null ? (intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0 : false;
            Bundle val = intent.getExtras();
            if(val!=null &&  val.containsKey("widgetaction") && !lfromHistory && !consumedIntent) {
                if(val.getString("widgetaction")!=null && val.getString("widgetaction").equalsIgnoreCase("resumeActivity")) {
                    // getArguments().putString("widgetaction", "");
                    this.getIntent().putExtra("widgetaction","");
                    Cursor mCursor = ticTacToeOrc.getRecentGame(this, mDatabase);
                    if((mCursor.moveToFirst()) || mCursor.getCount() !=0) {
                        ticTacViewModel = new TicTacViewModel(mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry._ID)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Game_Type)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_SCORE)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_SCORE)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_KEY)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_KEY)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_BoardValus)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_ROUND_COUNT)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_PLAYER1_TURN)), mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player1_SCORE_INITIAL)),
                                mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Player2_SCORE_INITIAL)),mCursor.getString(mCursor.getColumnIndex(DBContract.LastGameEntry.COLUMN_Game_No)));
                        if (ticTacViewModel.getId() != null && !ticTacViewModel.getId().equalsIgnoreCase("")) {
                            ticTacToeOrc.resumeActivity(navController, ticTacViewModel);
                        }
                    }
                }
            }
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        this.getIntent().putExtra("widgetaction", "");
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        resetGame = menu.findItem(R.id.game_reset);
        gameClear = menu.findItem(R.id.game_clear);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.play_game || item.getItemId() == R.id.game_reset) {
//            resetGame.setVisible(true);
//        }
//        else {
//            resetGame.setVisible(false);
//        }
        switch (item.getItemId()) {
            case R.id.play_game:
                navController.navigateUp();
                navController.navigate(R.id.Main_Activity_to_Tic_Tac_Toe_View);
                return true;
            case R.id.Load:
                navController.navigateUp();
                navController.navigate(R.id.Main_Activity_to_Load_Game_View);
                return true;
            case R.id.game_reset:
                return false;
            case R.id.game_clear:
                return false;
            case R.id.play_vs_comp_game:
                navController.navigateUp();
                navController.navigate(R.id.Main_Activity_to_Tic_Tac_Toe_Vs_Comp_View);
                return true;
            case R.id.resetdata:
                return false;
            case R.id.settings:
                navController.navigateUp();
                navController.navigate(R.id.Main_Activity_to_Settings_View);
                return true;
            case R.id.share_app:
                SendOrc sOrc = new SendOrc();
                sOrc.shareApp(this);
//                String appurl = "https://infotrends-in.blogspot.com/2020/02/tic-tac-toe-apk-download.html";
//                Intent sendInt = new Intent();
//                sendInt.setAction(Intent.ACTION_SEND);
//                sendInt.setType("text/plain");
//                sendInt.putExtra(Intent.EXTRA_TEXT, "Download the latest version of the gameTic Tac Toe using the link: " + appurl);
//                try {
//                    startActivity(Intent.createChooser(sendInt, "Share via..."));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
//                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }

}
//                Fragcount++;
//                // Create new fragment and transaction
//                Fragment newFragment = new TicTacToeFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack
//                transaction.add(R.id.nav_host_fragment, newFragment);
//                transaction.addToBackStack(null);
//
//                // Commit the transaction
//                transaction.commit();
//getLayoutInflater().inflate(R.layout.fragment_home, null, false);
//                Intent intent = new Intent(this, TicTacActivity.class);
//                startActivity(intent);