package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.PlayerSetup;
import com.github.ocraft.s2client.protocol.syntax.request.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Preconditions.oneOfIsSet;
import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.Arrays.asList;

public final class RequestCreateGame extends Request {

    private static final long serialVersionUID = -505916634411290169L;

    private final boolean disableFog;
    private final boolean realTime;
    private final Integer randomSeed;
    private final List<PlayerSetup> playerSetups;
    private final BattlenetMap battlenetMap;
    private final LocalMap localMap;

    private RequestCreateGame(Builder builder) {
        this.disableFog = builder.disableFog;
        this.realTime = builder.realTime;
        this.randomSeed = builder.randomSeed;
        this.playerSetups = builder.playerSetups;
        this.battlenetMap = builder.battlenetMap;
        this.localMap = builder.localMap;
    }

    public static RequestCreateGameSyntax createGame() {
        return new Builder();
    }

    public static final class Builder implements RequestCreateGameSyntax, WithPlayerSetupSyntax, DisableFogSyntax,
            RealtimeSyntax, WithRandomSeedSyntax {

        private boolean disableFog;
        private boolean realTime;
        private Integer randomSeed;
        private List<PlayerSetup> playerSetups = new ArrayList<>();
        private BattlenetMap battlenetMap;
        private LocalMap localMap;

        @Override
        public WithPlayerSetupSyntax onBattlenetMap(BattlenetMap battlenetMap) {
            this.battlenetMap = battlenetMap;
            this.localMap = nothing();
            return this;
        }

        @Override
        public WithPlayerSetupSyntax onLocalMap(LocalMap localMap) {
            this.battlenetMap = nothing();
            this.localMap = localMap;
            return this;
        }

        @Override
        public DisableFogSyntax withPlayerSetup(PlayerSetup... playerSetups) {
            this.playerSetups.addAll(asList(playerSetups));
            return this;
        }

        @Override
        public RealtimeSyntax disableFog() {
            this.disableFog = true;
            return this;
        }

        @Override
        public WithRandomSeedSyntax realTime() {
            this.realTime = true;
            return this;
        }

        @Override
        public BuilderSyntax<RequestCreateGame> withRandomSeed(int randomSeed) {
            this.randomSeed = randomSeed;
            return this;
        }

        @Override
        public RequestCreateGame build() {
            oneOfIsSet("map data", battlenetMap, localMap);
            requireNotEmpty("player setup", playerSetups);
            return new RequestCreateGame(this);
        }

    }

    @Override
    public Sc2Api.Request toSc2Api() {
        Sc2Api.RequestCreateGame.Builder aSc2ApiCreateGame = Sc2Api.RequestCreateGame.newBuilder()
                .setDisableFog(disableFog)
                .setRealtime(realTime);

        getRandomSeed().ifPresent(aSc2ApiCreateGame::setRandomSeed);
        getBattlenetMap().map(BattlenetMap::toSc2Api).ifPresent(aSc2ApiCreateGame::setBattlenetMapName);
        getLocalMap().map(LocalMap::toSc2Api).ifPresent(aSc2ApiCreateGame::setLocalMap);

        playerSetups.stream().map(PlayerSetup::toSc2Api).forEach(aSc2ApiCreateGame::addPlayerSetup);

        return Sc2Api.Request.newBuilder().setCreateGame(aSc2ApiCreateGame.build()).build();
    }

    public boolean isDisableFog() {
        return disableFog;
    }

    public boolean isRealTime() {
        return realTime;
    }

    public Optional<Integer> getRandomSeed() {
        return Optional.ofNullable(randomSeed);
    }

    public List<PlayerSetup> getPlayerSetups() {
        return new ArrayList<>(playerSetups);
    }

    public Optional<BattlenetMap> getBattlenetMap() {
        return Optional.ofNullable(battlenetMap);
    }

    public Optional<LocalMap> getLocalMap() {
        return Optional.ofNullable(localMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestCreateGame that = (RequestCreateGame) o;

        return disableFog == that.disableFog &&
                realTime == that.realTime &&
                (randomSeed != null ? randomSeed.equals(that.randomSeed) : that.randomSeed == null) &&
                playerSetups.equals(that.playerSetups) &&
                (battlenetMap != null ? battlenetMap.equals(that.battlenetMap) : that.battlenetMap == null) &&
                (localMap != null ? localMap.equals(that.localMap) : that.localMap == null);
    }

    @Override
    public int hashCode() {
        int result = (disableFog ? 1 : 0);
        result = 31 * result + (realTime ? 1 : 0);
        result = 31 * result + (randomSeed != null ? randomSeed.hashCode() : 0);
        result = 31 * result + playerSetups.hashCode();
        result = 31 * result + (battlenetMap != null ? battlenetMap.hashCode() : 0);
        result = 31 * result + (localMap != null ? localMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}