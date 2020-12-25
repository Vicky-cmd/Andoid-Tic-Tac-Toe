package com.infotrends.in.tictactoe.database;

import android.provider.BaseColumns;

public class DBContract {

    private DBContract() {

    }

    public static final class PlayGameEntery implements BaseColumns {
        public static final String TABLE_NAME = "gameStats";
        public static final String COLUMN_Game_Type = "game_type";
        public static final String COLUMN_Game_No = "game_no";
        public static final String COLUMN_Player1 = "player1";
        public static final String COLUMN_Player2 = "player2";
        public static final String COLUMN_Player1_SCORE = "player1_score";
        public static final String COLUMN_Player2_SCORE = "player2_score";
        public static final String COLUMN_Player1_KEY = "player1_key";
        public static final String COLUMN_Player2_KEY = "player2_key";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public static final class LastGameEntry implements BaseColumns {
        public static final String TABLE_NAME = "lastgameStats";
        public static final String COLUMN_Game_Type = "game_type";
        public static final String COLUMN_Game_No = "game_no";
        public static final String COLUMN_Player1 = "player1";
        public static final String COLUMN_Player2 = "player2";
        public static final String COLUMN_Player1_SCORE = "player1_score";
        public static final String COLUMN_Player2_SCORE = "player2_score";
        public static final String COLUMN_Player1_SCORE_INITIAL = "player1_score_initial";
        public static final String COLUMN_Player2_SCORE_INITIAL = "player2_score_initial";
        public static final String COLUMN_Player1_KEY = "player1_key";
        public static final String COLUMN_Player2_KEY = "player2_key";
        public static final String COLUMN_BoardValus = "board_values";
        public static final String COLUMN_ROUND_COUNT = "round_count";
        public static final String COLUMN_PLAYER1_TURN = "player1Turn";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
    public static final class NoGenerator implements BaseColumns {
        public static final String TABLE_NAME = "nogenerator";
        public static final String COLUMN_NO_TYPE = "notype";
        public static final String COLUMN_NO_GENERATED = "nogenerated";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
