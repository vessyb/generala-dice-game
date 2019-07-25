import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Player {
    private int currentScore;
    private int number;
    private int sum;
    private Set<List<Integer>> thrownDice = new HashSet<>();

    int getSum() {
        return sum;
    }

    Set<List<Integer>> getThrownDice() {
        return thrownDice;
    }

    void setThrownDice(List<Integer> list) {
        this.thrownDice.add(list);
    }

    void setSum(int sum) {
        this.sum = sum;
    }

    Player(int number) {
        this.number = number;
        System.out.println("> player " + number + ":");
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


}
