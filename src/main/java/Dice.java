import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dice {
    private static final int NUMBER_OF_SIDES = 6;

    private Random random = new Random();

    List<Integer> roll(){
        int value1 = random.nextInt(NUMBER_OF_SIDES) + 1;
        int value2 = random.nextInt(NUMBER_OF_SIDES) + 1;
        int value3 = random.nextInt(NUMBER_OF_SIDES) + 1;
        int value4 = random.nextInt(NUMBER_OF_SIDES) + 1;
        int value5 = random.nextInt(NUMBER_OF_SIDES) + 1;

        return Stream.of(value1, value2, value3, value4, value5)
                .sorted()
                .collect(Collectors.toList());
    }
}
