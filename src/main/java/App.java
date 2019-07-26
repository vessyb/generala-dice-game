import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vesela Bozheva
 */

public class App {
    private static int round;
    private static int numberOfPlayers;
    private Player player;
    private List<Player> players = new ArrayList<>();
    private final Dice DICE = new Dice();

    public static void main(final String[] args) {
        final App APP = new App();
        Path path = Paths.get("src/main/resources/config.properties");

        try (InputStream input = Files.newInputStream(path)) {
            Properties prop = new Properties();
            prop.load(input);

            round = Integer.parseInt(prop.getProperty("round"));
            numberOfPlayers = Integer.parseInt(prop.getProperty("player"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        APP.startNewGame();
    }

    private void createPlayers() {
        for (int i = 1; i <= App.numberOfPlayers; i++) {
            player = new Player(i);
            players.add(player);
        }
    }

    private void startNewGame() {
        createPlayers();
        for (int i = 1; i <= App.round; i++) {
            System.out.println(">>> round: " + i + ":");
            playOneRound();
        }
        printFinalStats();
    }

    private boolean isConsecutive(List<Integer> list) {
        int n = list.size();
        return list.get(n - 1) - list.get(0) == n - 1;

    }

    private void playOneRound() {
        for (Player player : players) {
            String combo = "";
            List<Integer> diceRolls = DICE.roll();
            List<Integer> temporaryList = new ArrayList<>();
            List<Integer> alternativeList = new ArrayList<>();
            List<Integer> altList = new ArrayList<>();
            String joinedList, jl1, jl2;

            // a diceCombinationOccurrences with every integer and the frequency of its occurrence
            Map<Integer, Long> diceCombinationOccurrences = Arrays.stream(diceRolls.toArray())
                    .collect(Collectors.groupingBy(m -> (Integer) m, Collectors.counting()));

            System.out.println(player.toString());
            System.out.println("current score: " + player.getCurrentScore());
            int var = diceCombinationOccurrences.size();
            switch (var) {
                case 5:
                    Integer straightValue;
                    /*List<Integer> straightNums = new ArrayList<>(diceCombinationOccurrences.keySet());*/
                    temporaryList = new ArrayList<>(diceCombinationOccurrences.keySet());

                    joinedList = getJoinedList(temporaryList);

                    if (isConsecutive(temporaryList) && !player.getTryOut().containsKey(joinedList)) {
                        straightValue = temporaryList.stream()
                                .reduce(0, Integer::sum);
                        player.setCurrentScore(player.getCurrentScore() + straightValue
                                + Combination.STRAIGHT.getCONSTANT());
                        combo = Combination.STRAIGHT.toString();
                        /*temporaryList.addAll(temporaryList);*/
                        player.getTryOut()
                                .put(joinedList, temporaryList);
                    } else {
                        combo = Combination.MISS.toString();
                    }
                    break;
                case 4:
                    Integer pairValue = diceCombinationOccurrences.entrySet()
                            .stream()
                            .filter(e -> e.getValue() == 2)
                            .map(Map.Entry::getKey)
                            .reduce(0, Integer::sum);

                    fillPairList(temporaryList, pairValue);

                    joinedList = getJoinedList(temporaryList);

                    if (!player.getTryOut().containsKey(joinedList)) {
                        player.setCurrentScore(player.getCurrentScore() + pairValue * 2
                                + Combination.PAIR.getCONSTANT());
                        combo = Combination.PAIR.toString();
                        player.getTryOut()
                                .put(joinedList, temporaryList);
                    } else {
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


                        fillTripleList(temporaryList, tripleValue);
                        fillPairList(alternativeList, tripleValue);

                        joinedList = getJoinedList(temporaryList);
                        jl1 = getJoinedList(alternativeList);

                        if (!player.getTryOut().containsKey(joinedList)) {
                            player.setCurrentScore(player.getCurrentScore() + tripleValue * 3
                                    + Combination.TRIPLE.getCONSTANT());
                            combo = Combination.TRIPLE.toString();
                            player.getTryOut()
                                    .put(joinedList, temporaryList);
                        } else if (!player.getTryOut().containsKey(jl1)) {
                            player.setCurrentScore(player.getCurrentScore() + tripleValue
                                    + Combination.PAIR.getCONSTANT());
                            combo = Combination.PAIR.toString();
                            player.getTryOut()
                                    .put(jl1, alternativeList);
                        } else {
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

                        fillPairList(alternativeList, max);
                        fillPairList(altList, min);

                        joinedList = getJoinedList(temporaryList);
                        jl1 = getJoinedList(alternativeList);
                        jl2 = getJoinedList(altList);

                        if (!player.getTryOut().containsKey(joinedList)) {
                            pairValue = diceCombinationOccurrences.entrySet()
                                    .stream()
                                    .filter(e -> e.getValue() == 2)
                                    .map(Map.Entry::getKey)
                                    .reduce(0, Integer::sum);
                            player.setCurrentScore(player.getCurrentScore() + pairValue * 2
                                    + Combination.DOUBLE_PAIR.getCONSTANT());
                            combo = Combination.DOUBLE_PAIR.toString();
                            player.getTryOut()
                                    .put(joinedList, temporaryList);
                        } else if (!player.getTryOut().containsKey(jl1)) {
                            pairValue = alternativeList.stream()
                                    .reduce(0, Integer::sum);
                            player.setCurrentScore(player.getCurrentScore() + pairValue
                                    + Combination.PAIR.getCONSTANT());
                            combo = Combination.PAIR.toString();
                            player.getTryOut()
                                    .put(jl1, alternativeList);
                        } else if (!player.getTryOut().containsKey(jl2)) {
                            pairValue = altList.stream()
                                    .reduce(0, Integer::sum);
                            player.setCurrentScore(player.getCurrentScore() + pairValue
                                    + Combination.PAIR.getCONSTANT());
                            combo = Combination.PAIR.toString();
                            player.getTryOut()
                                    .put(jl2, altList);
                        } else {
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

                        fillTripleList(alternativeList, tripleValue);
                        fillPairList(altList, pairValue);
                        fillFullHouseList(temporaryList, alternativeList, altList);

                        temporaryList = temporaryList.stream()
                                .sorted()
                                .collect(Collectors.toList());

                        joinedList = getJoinedList(temporaryList);
                        jl1 = getJoinedList(alternativeList);
                        jl2 = getJoinedList(altList);

                        if (!player.getTryOut().containsKey(joinedList)) {
                            player.setCurrentScore(player.getCurrentScore() + (pairValue * 2)
                                    + (tripleValue * 3) + Combination.FULL_HOUSE.getCONSTANT());
                            combo = Combination.FULL_HOUSE.toString();
                            player.getTryOut()
                                    .put(joinedList, temporaryList);
                        } else if (!player.getTryOut().containsKey(jl1)) {
                            player.setCurrentScore(player.getCurrentScore() + tripleValue * 3
                                    + Combination.TRIPLE.getCONSTANT());
                            combo = Combination.TRIPLE.toString();
                            player.getTryOut()
                                    .put(jl1, alternativeList);
                        } else if (!player.getTryOut().containsKey(jl2)) {
                            player.setCurrentScore(player.getCurrentScore() + pairValue * 2
                                    + Combination.PAIR.getCONSTANT());
                            combo = Combination.PAIR.toString();
                            player.getTryOut()
                                    .put(jl2, altList);
                        } else {
                            combo = Combination.MISS.toString();
                        }
                    } else {
                        Integer valueOfFourOfAKind = diceCombinationOccurrences.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 4)
                                .map(Map.Entry::getKey)
                                .reduce(0, Integer::sum);

                        fillPairList(altList, valueOfFourOfAKind);
                        fillTripleList(alternativeList, valueOfFourOfAKind);
                        fillFourOfAKind(temporaryList, valueOfFourOfAKind);

                        joinedList = getJoinedList(temporaryList);
                        jl1 = getJoinedList(alternativeList);
                        jl2 = getJoinedList(altList);

                        if (!player.getTryOut().containsKey(joinedList)) {
                            player.setCurrentScore(player.getCurrentScore() + valueOfFourOfAKind * 4
                                    + Combination.FOUR_OF_A_KIND.getCONSTANT());
                            combo = Combination.FOUR_OF_A_KIND.toString();
                            player.getTryOut()
                                    .put(joinedList, temporaryList);
                        } else if (!player.getTryOut().containsKey(jl1)) {
                            player.setCurrentScore(player.getCurrentScore() + valueOfFourOfAKind * 3
                                    + Combination.TRIPLE.getCONSTANT());
                            combo = Combination.TRIPLE.toString();
                            player.getTryOut()
                                    .put(jl1, alternativeList);
                        } else if (!player.getTryOut().containsKey(jl2)) {
                            player.setCurrentScore(player.getCurrentScore() + valueOfFourOfAKind * 2
                                    + Combination.PAIR.getCONSTANT());
                            combo = Combination.PAIR.toString();
                            player.getTryOut()
                                    .put(jl2, altList);
                        } else {
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

                    player.setCurrentScore(player.getCurrentScore() + valueOfGenerala * 5
                            + Combination.GENERALA.getCONSTANT());
                    combo = Combination.GENERALA.toString();

                    System.out.println("dice roll: " + diceRolls + " -> " + combo);
                    System.out.println("new score: " + player.getCurrentScore());

                    printFinalStats();

                    System.exit(0);
                    break;
            }
            System.out.println("dice roll: " + diceRolls + " -> " + combo);
            System.out.println("new score: " + player.getCurrentScore());

            temporaryList.clear();
            alternativeList.clear();
            altList.clear();
        }
    }

    private void fillFourOfAKind(List<Integer> temporaryList, Integer pairValue) {
        fillPairList(temporaryList, pairValue);
        fillPairList(temporaryList, pairValue);
    }

    private void fillFullHouseList(List<Integer> dest, List<Integer> src1, List<Integer> src2) {
        dest.addAll(src1);
        dest.addAll(src2);
    }

    private void fillTripleList(List<Integer> temporaryList, Integer tripleValue) {
        fillPairList(temporaryList, tripleValue);
        temporaryList.add(tripleValue);
    }

    private void fillPairList(List<Integer> temporaryList, Integer pairValue) {
        temporaryList.add(pairValue);
        temporaryList.add(pairValue);
    }

    private void printFinalStats() {
        players.sort(Comparator.comparing(Player::getCurrentScore).reversed());
        players.forEach(e -> System.out.println("player " + e.getNumber()
                + "- final score: " + e.getCurrentScore()));
    }

    private String getJoinedList(List<Integer> list) {
        String joinedList;
        joinedList = list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        return joinedList;
    }
}
