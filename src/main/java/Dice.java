import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Dice {
    private static final int NUMBER_OF_SIDES = 6;

    Random random = new Random();

    List<Integer> roll(){
        int value1 = random.nextInt(NUMBER_OF_SIDES) + 1;
        int value2 = random.nextInt(NUMBER_OF_SIDES) + 1;
        int value3 = random.nextInt(NUMBER_OF_SIDES) + 1;
        int value4 = random.nextInt(NUMBER_OF_SIDES) + 1;
        int value5 = random.nextInt(NUMBER_OF_SIDES) + 1;

        return Arrays.asList(value1, value2, value3, value4, value5);
    }
}
