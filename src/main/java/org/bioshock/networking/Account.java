package org.bioshock.networking;

public class Account {
    static String userName = "";
    static String token = "";
    static int score = 0;
    static int scoreToInc = 0;

    public static int getScoreToInc() { return scoreToInc; }

    public static void setScoreToInc(int score) { scoreToInc = score; }

    public static void setUserNam(String newName) {
        userName = newName;
    }

    public static void setToken(String newToken) {
        token = newToken;
    }

    public static void setScore(int newScore) {
        score = newScore;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getToken() {
        return token;
    }

    public static int getScore() {
        return score;
    }
}
