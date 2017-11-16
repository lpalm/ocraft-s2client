package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseGameInfoTest {
    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveGameInfo() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseGameInfo.from(nothing()))
                .withMessage("provided argument doesn't have game info response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseGameInfo.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have game info response");
    }

    @Test
    void throwsExceptionWhenMapNameIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseGameInfo.from(
                        sc2ApiGameInfoWithout(Sc2Api.ResponseGameInfo.Builder::clearMapName)))
                .withMessage("map name is required");
    }

    private Sc2Api.Response sc2ApiGameInfoWithout(Consumer<Sc2Api.ResponseGameInfo.Builder> clear) {
        return Sc2Api.Response.newBuilder()
                .setGameInfo(without(() -> sc2ApiResponseGameInfo().toBuilder(), clear)).build();
    }

    @Test
    void throwsExceptionWhenInterfaceOptionsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseGameInfo.from(
                        sc2ApiGameInfoWithout(Sc2Api.ResponseGameInfo.Builder::clearOptions)))
                .withMessage("interface options is required");
    }

    @Test
    void throwsExceptionWhenPlayersInfoIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseGameInfo.from(
                        sc2ApiGameInfoWithout(Sc2Api.ResponseGameInfo.Builder::clearPlayerInfo)))
                .withMessage("players info is required");
    }

    @Test
    void convertsSc2ApiResponseGameInfoToResponseGameInfo() {
        assertThatAllFieldsAreProperlyConverted(ResponseGameInfo.from(sc2ApiResponseWithGameInfo()));
    }

    private void assertThatAllFieldsAreProperlyConverted(ResponseGameInfo responseGameInfo) {
        assertThat(responseGameInfo.getType()).as("game info: type").isEqualTo(ResponseType.GAME_INFO);
        assertThat(responseGameInfo.getStatus()).as("game info: status").isEqualTo(GameStatus.IN_GAME);
        assertThat(responseGameInfo.getMapName()).as("game info: map name").isEqualTo(BATTLENET_MAP_NAME);
        assertThat(responseGameInfo.getModNames()).as("game info: mod names").containsExactlyInAnyOrder(MOD_NAME);
        assertThat(responseGameInfo.getLocalMap()).as("game info: local map").isNotEmpty();
        assertThat(responseGameInfo.getPlayersInfo()).as("game info: players info").isNotEmpty();
        assertThat(responseGameInfo.getStartRaw()).as("game info: start raw").isNotEmpty();
        assertThat(responseGameInfo.getInterfaceOptions()).as("game info: interface options").isNotNull();
    }

    @Test
    void hasEmptyModNamesSetIfNotProvided() {
        assertThat(ResponseGameInfo.from(
                sc2ApiGameInfoWithout(Sc2Api.ResponseGameInfo.Builder::clearModNames)).getModNames())
                .as("game info: default mod names").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ResponseGameInfo.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status", "mapName", "modNames", "playersInfo", "interfaceOptions")
                .withRedefinedSuperclass()
                .verify();
    }
}