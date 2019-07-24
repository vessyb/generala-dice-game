import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Player {
    int currentScore;
    private int number;

    Player(int number) {
        this.number = number;
        System.out.println("> player " + number + ":");
    }

    Set<List<Integer>> thrownDice = new HashSet<>();
}
