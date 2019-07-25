import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vesela Bozheva
 */

public class App {
    private static int round;
    private static int numberOfPlayers;
    private Player player;
    private Set<Player> players = new HashSet<>();

    public static void main(final String[] args) {
        App app = new App();

        try (InputStream input = new FileInputStream("C:\\GeneralaDiceGame\\src\\main\\resources\\config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            round = Integer.parseInt(prop.getProperty("round"));
            numberOfPlayers = Integer.parseInt(prop.getProperty("player"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        app.startNewGame();
    }

    private void startNewGame() {
        for (int i = 1; i <= App.round; i++) {
            System.out.println(">>> round: " + i + ":");
            playOneRound();
        }
        players.stream()
                .sorted(Comparator.comparing(Player::getCurrentScore)
                        .reversed())
                .forEach(e -> System.out.println("player " + player.getNumber() + "final score: " + player.getCurrentScore()));
    }

    private boolean isConsecutive(List<Integer> list) {
        int n = list.size();
        return list.stream()
                .mapToInt(Integer::intValue)
                .sum() == n * (n + 1) / 2;
    }

    private void playOneRound() {
        for (int i = 1; i <= App.numberOfPlayers; i++) {
            /*int sum = 0;*/
            String combo = "";
            player = new Player(i);
            final Dice dice = new Dice();
            List<Integer> diceRolls = dice.roll();
            List<Integer> temporaryList = new ArrayList<>();
            List<Integer> alternativeList = new ArrayList<>();
            List<Integer> altList = new ArrayList<>();
            players.add(player);

            // a diceCombinationOccurrences with every integer and the frequency of its occurrence
            Map<Integer, Long> diceCombinationOccurrences = Arrays.stream(diceRolls.toArray())
                    .collect(Collectors.groupingBy(m -> (Integer) m, Collectors.counting()));

            int var = diceCombinationOccurrences.size();
            switch (var) {
                case 5:
                    Integer straightValue;
                    List<Integer> straightNums = new ArrayList<>(diceCombinationOccurrences.keySet());
                    if (isConsecutive(straightNums) && !player.getThrownDice().contains(straightNums)) {
                        straightValue = straightNums.stream()
                                .reduce(0, Integer::sum);
                        player.setSum(straightValue + Combination.STRAIGHT.getCONSTANT());
                        combo = Combination.STRAIGHT.toString();
                        temporaryList.addAll(straightNums);
                        player.setThrownDice(temporaryList);
                    } else {
                        player.setSum(0);
                        combo = Combination.MISS.toString();
                    }
                    break;
                case 4:
                    Integer pairValue = diceCombinationOccurrences.entrySet()
                            .stream()
                            .filter(e -> e.getValue() == 2)
                            .map(Map.Entry::getKey)
                            .reduce(0, Integer::sum);

                    temporaryList.add(pairValue);
                    temporaryList.add(pairValue);

                    if (!player.getThrownDice().contains(temporaryList)) {
                        player.setSum(pairValue * 2 + Combination.PAIR.getCONSTANT());
                        combo = Combination.PAIR.toString();
                        player.setThrownDice(temporaryList);
                    } else {
                        player.setSum(0);
                        combo = Combination.MISS.toString();
                    }
                    break;
                case 3:
                    if (diceCombinationOccurrences.containsValue(Long.valueOf("3"))) {
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

                        if (!player.getThrownDice().contains(temporaryList)) {
                            player.setSum(tripleValue * 3 + Combination.TRIPLE.getCONSTANT());
                            combo = Combination.TRIPLE.toString();
                            player.setThrownDice(temporaryList);
                        } else if (!player.getThrownDice().contains(alternativeList)) {
                            player.setSum(tripleValue + Combination.PAIR.getCONSTANT());
                            combo = Combination.PAIR.toString();
                            player.setThrownDice(alternativeList);
                        } else {
                            player.setSum(0);
                            combo = Combination.MISS.toString();
                        }
                    } else {
                        temporaryList = diceCombinationOccurrences.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 2)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList());

                        temporaryList = temporaryList.stream()
                                .sorted()
                                .collect(Collectors.toList());

                        Integer max = diceCombinationOccurrences.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 2)
                                .map(Map.Entry::getKey)
                                .mapToInt(v -> v)
                                .max()
                                .orElse(0);

                        Integer min = diceCombinationOccurrences.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 2)
                                .map(Map.Entry::getKey)
                                .mapToInt(v -> v)
                                .min()
                                .orElse(0);

                        alternativeList.add(max);
                        alternativeList.add(max);

                        altList.add(min);
                        altList.add(min);

                        if (!player.getThrownDice().contains(temporaryList)) {
                            pairValue = diceCombinationOccurrences.entrySet()
                                    .stream()
                                    .filter(e -> e.getValue() == 2)
                                    .map(Map.Entry::getKey)
                                    .reduce(0, Integer::sum);
                            player.setSum(pairValue * 2 + Combination.DOUBLE_PAIR.getCONSTANT());
                            combo = Combination.DOUBLE_PAIR.toString();
                            player.setThrownDice(temporaryList);
                        } else if (!player.getThrownDice().contains(alternativeList)) {
                            pairValue = alternativeList.stream()
                                    .reduce(0, Integer::sum);
                            player.setSum(pairValue + Combination.PAIR.getCONSTANT());
                            combo = Combination.PAIR.toString();
                            player.setThrownDice(alternativeList);
                        } else if (!player.getThrownDice().contains(altList)) {
                            pairValue = altList.stream()
                                    .reduce(0, Integer::sum);
                            player.setSum(pairValue + Combination.PAIR.getCONSTANT());
                            combo = Combination.PAIR.toString();
                            player.setThrownDice(altList);
                        } else {
                            player.setSum(0);
                            combo = Combination.MISS.toString();
                        }
                    }
                    break;
                case 2:
                    if (diceCombinationOccurrences.containsValue(Long.valueOf("3"))) {
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

                        if (!player.getThrownDice().contains(temporaryList)) {
                            player.setSum((pairValue * 2) + (tripleValue * 3) + Combination.FULL_HOUSE.getCONSTANT());
                            combo = Combination.FULL_HOUSE.toString();
                            player.setThrownDice(temporaryList);
                        } else if (!player.getThrownDice().contains(alternativeList)) {
                            player.setSum(tripleValue * 3 + Combination.TRIPLE.getCONSTANT());
                            combo = Combination.TRIPLE.toString();
                            player.setThrownDice(alternativeList);
                        } else if (!player.getThrownDice().contains(altList)) {
                            player.setSum(pairValue * 2 + Combination.PAIR.getCONSTANT());
                            combo = Combination.PAIR.toString();
                            player.setThrownDice(altList);
                        } else {
                            player.setSum(0);
                            combo = Combination.MISS.toString();
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

                        if (!player.getThrownDice().contains(temporaryList)) {
                            player.setSum(valueOfFourOfAKind * 4 + Combination.FOUR_OF_A_KIND.getCONSTANT());
                            combo = Combination.FOUR_OF_A_KIND.toString();
                            player.setThrownDice(temporaryList);
                        } else if (!player.getThrownDice().contains(alternativeList)) {
                            player.setSum(valueOfFourOfAKind * 3 + Combination.TRIPLE.getCONSTANT());
                            combo = Combination.TRIPLE.toString();
                            player.setThrownDice(alternativeList);
                        } else if (!player.getThrownDice().contains(altList)) {
                            player.setSum(valueOfFourOfAKind * 2 + Combination.PAIR.getCONSTANT());
                            combo = Combination.PAIR.toString();
                            player.setThrownDice(altList);
                        } else {
                            player.setSum(0);
                            combo = Combination.MISS.toString();
                        }
                    }
                    break;
                case 1:
                    Integer valueOfGenerala = diceCombinationOccurrences.entrySet()
                            .stream()
                            .filter(e -> e.getValue() == 5)
                            .map(Map.Entry::getKey)
                            .reduce(0, Integer::sum);

                    player.setSum(valueOfGenerala * 5 + Combination.GENERALA.getCONSTANT());
                    combo = Combination.GENERALA.toString();

                    System.out.println("current score: " + player.getCurrentScore());
                    player.setSum(player.getSum() * 2);
                    player.setCurrentScore(player.getSum());
                    System.out.println("dice roll: " + diceRolls + " -> " + combo);
                    System.out.println("new score: " + player.getCurrentScore());

                    players.stream()
                            .sorted(Comparator.comparing(Player::getCurrentScore)
                                    .reversed())
                            .forEach(e -> System.out.println("player " + player.getNumber() + "final score: " + player.getCurrentScore()));

                    System.exit(0);
                    break;
            }
            /*player.setSum(player.getSum() * 2);*/
            player.setCurrentScore(player.getSum());
            System.out.println("current score: " + player.getCurrentScore());
            System.out.println("dice roll: " + diceRolls + " -> " + combo);
            //todo: fix new score = it should get updated
            System.out.println("new score: " + player.getCurrentScore());

            temporaryList.clear();
            alternativeList.clear();
            altList.clear();
        }
    }

}
