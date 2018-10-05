package dk.michaelwestergaard.galgeleg;

public class PlayerDTO {

    private String userID;
    private String username;
    private int score;

    public PlayerDTO(String username, int score, String userID) {
        this.username = username;
        this.score = score;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", score=" + score +
                '}';
    }
}
