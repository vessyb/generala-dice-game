public enum Combination {
    MISS(0),
    PAIR (10),
    DOUBLE_PAIR (15),
    TRIPLE (20),
    FULL_HOUSE (25),
    STRAIGHT (30),
    FOUR_OF_A_KIND (40),
    GENERALA (50);

    private final int CONSTANT;

    Combination(int CONSTANT){
        this.CONSTANT = CONSTANT;
    }

    public int getCONSTANT(){
        return this.CONSTANT;
    }
}
