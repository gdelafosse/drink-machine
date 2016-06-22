/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package drink.machine.coins;

import static javax.persistence.FetchType.EAGER;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Coins
{

    public static int COINSID = 1000;

    @Id
    private int coinsId = COINSID;

    @ElementCollection(fetch = EAGER)
    private Map<Coin, Integer> coins = new HashMap<>();

    public void set(Coin coin, int amount)
    {
        coins.put(coin, amount);
    }

    public int get(Coin coin)
    {
        return coins.containsKey(coin) ? coins.get(coin) : 0;
    }

    public int sum() {
        return coins.keySet().stream()
                .mapToInt((coin) -> coins.get(coin)*coin.value)
                .sum();
    }

    public void add(Coin coin) {
        add(coin, 1);
    }

    public void add(Coin coin, int amount) {
        set(coin, get(coin) + amount);
    }

    public void add(Coins money) {
        Arrays.stream(Coin.values()).forEach(c -> add(c, money.get(c)));
    }

    public void remove(Coins money)
    {
        Arrays.stream(Coin.values()).forEach(c -> set(c, get(c) - money.get(c)));
    }

    public void reset() {
        Arrays.stream(Coin.values()).forEach(c -> set(c, 0));
    }

}

