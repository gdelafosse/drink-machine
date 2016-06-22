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

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

@Stateless
public class CoinsService
{
    @PersistenceContext(unitName = "drink-machine-pu")
    private EntityManager entityManager;

    public Coins getCoins()
    {
        Coins coins = safeGet();
        entityManager.detach(coins);
        return coins;
    }

    public void setCoins(Coins coins) {
        Coins original = safeGet();
        original.set(coins);
    }

    private Coins safeGet() {
        Coins coins = entityManager.find(Coins.class, Coins.COINSID, LockModeType.PESSIMISTIC_READ);
        if (coins == null) {
            throw new RuntimeException("Unable to get coins from machine");
        }
        return coins;
    }
}
