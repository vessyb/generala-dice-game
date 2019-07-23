import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Vesela Bozheva
 */

public class App {
    public static void main(String[] args) throws IOException {

    }

    private void startNewGame(){

    }

    private void playOneRound() throws IOException {
        GetPropertyValues propertyValues = new GetPropertyValues();
        propertyValues.getPropertyValues();

        for (int i = 0; i < propertyValues.numberOfPlayers; i++){
            int currentScore;
            final Dice dice = new Dice();
            List<Integer> diceRolls = dice.roll();

            // a map with every integer and te frequency of its occurrence
            Map<Object, Long> map = Arrays.stream(diceRolls.toArray())
                    .collect(Collectors.groupingBy(m -> m, Collectors.counting()));


        }
    }
}
