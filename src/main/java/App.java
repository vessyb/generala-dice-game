import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vesela Bozheva
 */

public class App {
    private GetPropertyValues propertyValues = new GetPropertyValues();

    public static void main(final String[] args) throws IOException {
        App app = new App();
        app.startNewGame();
    }

    private void startNewGame() {
        for (int i = 1; i <= propertyValues.round; i++) {
            System.out.println(">>> round: " + i + ":");
            playOneRound();
        }
    }

    private void playOneRound() {
        for (int i = 0; i < propertyValues.numberOfPlayers; i++) {
            int sum = 0;
            String combo = "";
            Player player = new Player(i);
            final Dice dice = new Dice();
            List<Integer> diceRolls = dice.roll();
            List<Integer> temporaryList = new ArrayList<>();
            List<Integer> alternativeList = new ArrayList<>();
            List<Integer> altList = new ArrayList<>();

            // a diceCombinationOccurrences with every integer and the frequency of its occurrence
            Map<Integer, Long> diceCombinationOccurrences = Arrays.stream(diceRolls.toArray())
                    .collect(Collectors.groupingBy(m -> (Integer) m, Collectors.counting()));

            int var = diceCombinationOccurrences.size();
            switch (var) {
                case 5:
                    Integer straightValue = diceCombinationOccurrences.keySet()
                            .stream()
                            .reduce(0, Integer::sum);

                    sum = straightValue + Combination.STRAIGHT.getCONSTANT();
                    combo = Combination.STRAIGHT.toString();
                    temporaryList.addAll(diceRolls);
                    player.thrownDice.add(temporaryList);
                    break;
                case 4:
                    Integer pairValue = diceCombinationOccurrences.entrySet()
                            .stream()
                            .filter(e -> e.getValue() == 2)
                            .map(Map.Entry::getKey)
                            .reduce(0, Integer::sum);

                    sum = pairValue * 2 + Combination.PAIR.getCONSTANT();
                    combo = Combination.PAIR.toString();
                    temporaryList.add(pairValue);
                    temporaryList.add(pairValue);
                    player.thrownDice.add(temporaryList);
                    break;
                case 3:
                    if (diceCombinationOccurrences.containsValue(3)) {
                        Integer tripleValue = diceCombinationOccurrences.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 3)
                                .map(Map.Entry::getKey)
                                .reduce(0, Integer::sum);

                        temporaryList.add(tripleValue);
                        temporaryList.add(tripleValue);
                        temporaryList.add(tripleValue);

                        alternativeList.add(tripleValue);
                        alternativeList.add(tripleValue);

                        if (!player.thrownDice.contains(temporaryList)) {
                            sum = tripleValue * 3 + Combination.TRIPLE.getCONSTANT();
                            combo = Combination.TRIPLE.toString();
                            player.thrownDice.add(temporaryList);
                        } else if (!player.thrownDice.contains(alternativeList)) {
                            sum = tripleValue * 2 + Combination.PAIR.getCONSTANT();
                            combo = Combination.PAIR.toString();
                            player.thrownDice.add(alternativeList);
                        } else {
                            sum = 0;
                        }
                    } else {
                        //gets distinct keys which have frequency of 2 and adds them twice to the list, after that sorts them
                        for (int j = 0; j < 2; j++) {
                            temporaryList = diceCombinationOccurrences.entrySet()
                                    .stream()
                                    .filter(e -> e.getValue() == 2)
                                    .map(Map.Entry::getKey)
                                    .collect(Collectors.toList());
                        }

                        temporaryList = temporaryList.stream()
                                .sorted()
                                .collect(Collectors.toList());

                        Integer max = diceCombinationOccurrences.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 2)
                                .map(Map.Entry::getKey)
                                .mapToInt(v -> v)
                                .max()
                                .orElseThrow(NoSuchElementException::new);

                        Integer min = diceCombinationOccurrences.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 2)
                                .map(Map.Entry::getKey)
                                .mapToInt(v -> v)
                                .min()
                                .orElseThrow(NoSuchElementException::new);

                        alternativeList.add(max);
                        alternativeList.add(max);

                        altList.add(min);
                        altList.add(min);

                        if (!player.thrownDice.contains(temporaryList)) {
                            pairValue = temporaryList.stream()
                                    .reduce(0, Integer::sum);
                            sum = pairValue * 2 + Combination.DOUBLE_PAIR.getCONSTANT();
                            combo = Combination.DOUBLE_PAIR.toString();
                            player.thrownDice.add(temporaryList);
                        } else if (!player.thrownDice.contains(alternativeList)) {
                            pairValue = alternativeList.stream()
                                    .reduce(0, Integer::sum);
                            sum = pairValue + Combination.PAIR.getCONSTANT();
                            combo = Combination.PAIR.toString();
                        } else if (!player.thrownDice.contains(altList)) {
                            pairValue = altList.stream()
                                    .reduce(0, Integer::sum);
                            sum = pairValue + Combination.PAIR.getCONSTANT();
                            combo = Combination.PAIR.toString();
                        } else {
                            sum = 0;
                        }
                    }
                    break;
                case 2:
                    if (diceCombinationOccurrences.containsValue(3)) {
                        Integer tripleValue = diceCombinationOccurrences.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 3)
                                .map(Map.Entry::getKey)
                                .reduce(0, Integer::sum);

                        pairValue = diceCombinationOccurrences.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 2)
                                .map(Map.Entry::getKey)
                                .reduce(0, Integer::sum);

                        temporaryList.add(tripleValue);
                        temporaryList.add(tripleValue);
                        temporaryList.add(tripleValue);
                        temporaryList.add(pairValue);
                        temporaryList.add(pairValue);

                        alternativeList.add(tripleValue);
                        alternativeList.add(tripleValue);
                        alternativeList.add(tripleValue);

                        altList.add(pairValue);
                        altList.add(pairValue);

                        temporaryList = temporaryList.stream()
                                .sorted()
                                .collect(Collectors.toList());

                        if (!player.thrownDice.contains(temporaryList)) {
                            sum = (pairValue * 2) + (tripleValue * 3) + Combination.FULL_HOUSE.getCONSTANT();
                            combo = Combination.FULL_HOUSE.toString();
                            player.thrownDice.add(temporaryList);
                        } else if (!player.thrownDice.contains(alternativeList)) {
                            sum = tripleValue * 3 + Combination.TRIPLE.getCONSTANT();
                            combo = Combination.TRIPLE.toString();
                            player.thrownDice.add(alternativeList);
                        } else if (!player.thrownDice.contains(altList)) {
                            sum = pairValue * 2 + Combination.PAIR.getCONSTANT();
                            combo = Combination.TRIPLE.toString();
                            player.thrownDice.add(altList);
                        } else {
                            sum = 0;
                        }
                    } else {
                        Integer valueOfFourOfAKind = diceCombinationOccurrences.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 4)
                                .map(Map.Entry::getKey)
                                .reduce(0, Integer::sum);

                        temporaryList.add(valueOfFourOfAKind);
                        temporaryList.add(valueOfFourOfAKind);
                        temporaryList.add(valueOfFourOfAKind);
                        temporaryList.add(valueOfFourOfAKind);

                        alternativeList.add(valueOfFourOfAKind);
                        alternativeList.add(valueOfFourOfAKind);
                        alternativeList.add(valueOfFourOfAKind);

                        altList.add(valueOfFourOfAKind);
                        altList.add(valueOfFourOfAKind);

                        if (!player.thrownDice.contains(temporaryList)) {
                            sum = valueOfFourOfAKind * 4 + Combination.FOUR_OF_A_KIND.getCONSTANT();
                            combo = Combination.FOUR_OF_A_KIND.toString();
                            player.thrownDice.add(temporaryList);
                        } else if (!player.thrownDice.contains(alternativeList)) {
                            sum = valueOfFourOfAKind * 3 + Combination.TRIPLE.getCONSTANT();
                            combo = Combination.TRIPLE.toString();
                            player.thrownDice.add(alternativeList);
                        } else if (!player.thrownDice.contains(altList)) {
                            sum = valueOfFourOfAKind * 2 + Combination.PAIR.getCONSTANT();
                            combo = Combination.PAIR.toString();
                            player.thrownDice.add(altList);
                        } else {
                            sum = 0;
                        }
                    }
                    break;
                case 1:
                    Integer valueOfGenerala = diceCombinationOccurrences.entrySet()
                            .stream()
                            .filter(e -> e.getValue() == 5)
                            .map(Map.Entry::getKey)
                            .reduce(0, Integer::sum);

                    sum = valueOfGenerala * 5 + Combination.GENERALA.getCONSTANT();
                    combo = Combination.GENERALA.toString();
                    break;
            }
            System.out.println("current score: " + player.currentScore);
            player.currentScore += sum;
            System.out.println("dice roll: " + diceRolls + " -> " + combo);
            System.out.println("new score: " + player.currentScore);

            temporaryList.clear();
            alternativeList.clear();
            altList.clear();
        }
    }
}
