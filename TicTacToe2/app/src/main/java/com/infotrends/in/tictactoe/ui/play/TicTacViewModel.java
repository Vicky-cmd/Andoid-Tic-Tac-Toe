package com.infotrends.in.tictactoe.ui.play;

public class TicTacViewModel {

    private static String id;
    private static String game_type;
    private static String player1;
    private static String player2;
    private static String player1_score;
    private static String player2_score;
    private static String player1_key;
    private static String player2_key;
    private static String board_values;
    private static String round_count;
    private static String player1Turn;
    private static String player1_score_initial;
    private static String player2_score_initial;
    private static String game_no;

    public TicTacViewModel() {
    }

    public TicTacViewModel(String id, String game_type, String player1, String player2, String player1_score,
                           String player2_score, String player1_key, String player2_key, String board_values, String round_count, String player1Turn,
                           String player1_score_initial, String player2_score_initial, String game_no) {
        this.id = id;
        this.game_type = game_type;
        this.player1 = player1;
        this.player2 = player2;
        this.player1_score = player1_score;
        this.player2_score = player2_score;
        this.player1_key = player1_key;
        this.player2_key = player2_key;
        this.board_values = board_values;
        this.round_count = round_count;
        this.player1Turn= player1Turn;
        this.player1_score_initial = player1_score_initial;
        this.player2_score_initial = player2_score_initial;
        this.game_no = game_no;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        TicTacViewModel.id = id;
    }

    public static String getGame_type() {
        return game_type;
    }

    public static void setGame_type(String game_type) {
        TicTacViewModel.game_type = game_type;
    }

    public static String getPlayer1() {
        return player1;
    }

    public static void setPlayer1(String player1) {
        TicTacViewModel.player1 = player1;
    }

    public static String getPlayer2() {
        return player2;
    }

    public static void setPlayer2(String player2) {
        TicTacViewModel.player2 = player2;
    }

    public static String getPlayer1_score() {
        return player1_score;
    }

    public static void setPlayer1_score(String player1_score) {
        TicTacViewModel.player1_score = player1_score;
    }

    public static String getPlayer2_score() {
        return player2_score;
    }

    public static void setPlayer2_score(String player2_score) {
        TicTacViewModel.player2_score = player2_score;
    }

    public static String getPlayer1_key() {
        return player1_key;
    }

    public static void setPlayer1_key(String player1_key) {
        TicTacViewModel.player1_key = player1_key;
    }

    public static String getPlayer2_key() {
        return player2_key;
    }

    public static void setPlayer2_key(String player2_key) {
        TicTacViewModel.player2_key = player2_key;
    }

    public static String getBoard_values() {
        return board_values;
    }

    public static void setBoard_values(String board_values) {
        TicTacViewModel.board_values = board_values;
    }

    public static String getRound_count() {
        return round_count;
    }

    public static void setRound_count(String round_count) {
        TicTacViewModel.round_count = round_count;
    }

    public static String getPlayer1Turn() {
        return player1Turn;
    }

    public static void setPlayer1Turn(String player1Turn) {
        TicTacViewModel.player1Turn = player1Turn;
    }

    public static String getPlayer1_score_initial() {
        return player1_score_initial;
    }

    public static void setPlayer1_score_initial(String player1_score_initial) {
        TicTacViewModel.player1_score_initial = player1_score_initial;
    }

    public static String getPlayer2_score_initial() {
        return player2_score_initial;
    }

    public static void setPlayer2_score_initial(String player2_score_initial) {
        TicTacViewModel.player2_score_initial = player2_score_initial;
    }

    public static String getGame_no() {
        return game_no;
    }

    public static void setGame_no(String game_no) {
        TicTacViewModel.game_no = game_no;
    }
}
