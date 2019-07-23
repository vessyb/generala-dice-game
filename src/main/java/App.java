import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Vesela Bozheva
 */

public class App {
    private GetPropertyValues propertyValues = new GetPropertyValues();

    public static void main(String[] args) throws IOException {
        App app = new App();
        app.startNewGame();
    }

    private void startNewGame() {
        for (int i = 1; i <= propertyValues.round; i++) {
            System.out.println("round: " + i + ":");
            playOneRound();
        }
    }

    private void playOneRound() {
        for (int i = 0; i < propertyValues.numberOfPlayers; i++) {
            int currentScore;
            final Dice dice = new Dice();
            List<Integer> diceRolls = dice.roll();

            // a map with every integer and the frequency of its occurrence
            Map<Integer, Long> map = Arrays.stream(diceRolls.toArray())
                    .collect(Collectors.groupingBy(m -> (Integer) m, Collectors.counting()));

            int var = map.size();
            switch (var) {
                case 5:
                    Integer straightValue = map.keySet()
                            .stream()
                            .reduce(0, Integer::sum);

                    Integer sumOfStraight = straightValue + Combination.STRAIGHT.getCONSTANT();
                    break;
                case 4:
                    Integer pairValue = map.entrySet()
                            .stream()
                            .filter(e -> e.getValue() == 2)
                            .map(Map.Entry::getKey)
                            .reduce(0, Integer::sum);

                    Integer sumOfPair = pairValue * 2 + Combination.PAIR.getCONSTANT();
                    break;
                case 3:
                    if (map.containsValue(3)) {
                        Integer tripleValue = map.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 3)
                                .map(Map.Entry::getKey)
                                .reduce(0, Integer::sum);

                        Integer sumOfTriple = tripleValue * 3 + Combination.TRIPLE.getCONSTANT();

                    } else {
                        pairValue = map.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 2)
                                .map(Map.Entry::getKey)
                                .reduce(0, Integer::sum);

                        sumOfPair = pairValue * 2 + Combination.DOUBLE_PAIR.getCONSTANT();
                    }
                    break;
                case 2:
                    if (map.containsValue(4)) {
                        Integer valueOfFourOfAKind = map.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 4)
                                .map(Map.Entry::getKey)
                                .reduce(0, Integer::sum);

                        Integer sumOfFourOfAKind = valueOfFourOfAKind * 4 + Combination.FOUR_OF_A_KIND.getCONSTANT();

                    } else {
                        pairValue = map.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 2)
                                .map(Map.Entry::getKey)
                                .reduce(0, Integer::sum);

                        Integer tripleValue = map.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 3)
                                .map(Map.Entry::getKey)
                                .reduce(0, Integer::sum);

                        Integer sumOfFullHouse = (pairValue * 2) + (tripleValue * 3) + Combination.FULL_HOUSE.getCONSTANT();
                    }
                    break;
                case 1:
                    Integer valueOfGenerala = map.entrySet()
                            .stream()
                            .filter(e -> e.getValue() == 5)
                            .map(Map.Entry::getKey)
                            .reduce(0, Integer::sum);

                    Integer sumOfGenerala = valueOfGenerala * 5 + Combination.GENERALA.getCONSTANT();
                    break;
            }
            System.out.println("player " + i + ":");
        }
    }
}
