package drink.machine.coins;

import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class CoinsJsonAdapter {

    public static Coins toCoins(JsonObject json) {
        Coins coins = new Coins();
        json.keySet().stream().forEach( key -> coins.set(Coin.valueOf(key.toUpperCase()), json.getInt(key)));
        return coins;
    }

    public static JsonObject toJson(Coins coins) {
        JsonObjectBuilder json = Json.createObjectBuilder();
        Arrays.stream(Coin.values()).forEach(c -> json.add(c.name().toLowerCase(), coins.get(c)));
        json.add("total", coins.sum());
        return json.build();
    }
}
