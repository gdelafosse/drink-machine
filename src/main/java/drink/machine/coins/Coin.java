package drink.machine.coins;

public enum Coin
{
    TWO_EUROS(200),
    ONE_EURO(100),
    FIFTY_CENTS(50),
    TWENTY_CENTS(20),
    TEN_CENTS(10),
    FIVE_CENTS(5),
    TWO_CENTS(2),
    ONE_CENT(1);

    public int value;

    Coin(int value) {
        this.value = value;
    }
}
