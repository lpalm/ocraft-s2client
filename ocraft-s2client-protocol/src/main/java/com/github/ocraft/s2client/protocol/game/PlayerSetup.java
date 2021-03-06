package com.github.ocraft.s2client.protocol.game;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

public class PlayerSetup implements Sc2ApiSerializable<Sc2Api.PlayerSetup> {

    private static final long serialVersionUID = -6790202317629033299L;

    private final PlayerType playerType;

    PlayerSetup(PlayerType playerType) {
        this.playerType = playerType;
    }

    public static PlayerSetup observer() {
        return new PlayerSetup(PlayerType.OBSERVER);
    }

    public static PlayerSetup participant() {
        return new PlayerSetup(PlayerType.PARTICIPANT);
    }

    @Override
    public Sc2Api.PlayerSetup toSc2Api() {
        return Sc2Api.PlayerSetup.newBuilder()
                .setType(playerType.toSc2Api())
                .build();
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerSetup)) return false;

        PlayerSetup that = (PlayerSetup) o;

        return that.canEqual(this) && playerType == that.playerType;
    }

    public boolean canEqual(Object other) {
        return (other instanceof PlayerSetup);
    }

    @Override
    public int hashCode() {
        return playerType.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
