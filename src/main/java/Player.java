import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Player {
    private int currentScore;
    private int number;
    private Map<String, List<Integer>> tryOut = new HashMap<>();

    Map<String, List<Integer>> getTryOut() {
        return tryOut;
    }

    Player(int number) {
        this.number = number;
    }

    void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    int getCurrentScore() {
        return currentScore;
    }

    int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "> player " + number + ":";
    }

}
