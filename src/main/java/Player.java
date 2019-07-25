import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Player {
    private int currentScore;
    private int number;
    private Set<List<Integer>> thrownDice = new HashSet<>();

    Set<List<Integer>> getThrownDice() {
        return thrownDice;
    }

    void setThrownDice(List<Integer> list) {
        this.thrownDice.add(list);
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
