package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Fixtures.DELAY;
import static com.github.ocraft.s2client.protocol.Fixtures.debugTestProcess;
import static com.github.ocraft.s2client.protocol.debug.DebugTestProcess.testProcess;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class DebugTestProcessTest {
    @Test
    void serializesToSc2ApiDebugTestProcess() {
        assertThatAllFieldsAreSerialized(debugTestProcess().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugTestProcess sc2ApiTestProcess) {
        assertThat(sc2ApiTestProcess.hasTest()).as("sc2api test process: has test").isTrue();
        assertThat(sc2ApiTestProcess.getDelayMs()).as("sc2api test process: delay ms").isEqualTo(DELAY);
    }

    @Test
    void throwsExceptionWhenTestIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((DebugTestProcess.Builder) testProcess()).build())
                .withMessage("test is required");
    }

    @Test
    void serializesDefaultValueForDelayIfNotProvided() {
        Debug.DebugTestProcess sc2ApiTestProcess = testProcess()
                .with(DebugTestProcess.Test.EXIT).build().toSc2Api();

        assertThat(sc2ApiTestProcess.hasDelayMs()).as("sc2api test process: has delay ms").isTrue();
        assertThat(sc2ApiTestProcess.getDelayMs()).as("sc2api test process: default delay ms").isEqualTo(0);
    }

    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "testMappings")
    void serializesToSc2ApiTest(Debug.DebugTestProcess.Test expectedSc2ApiTest, DebugTestProcess.Test test) {
        assertThat(test.toSc2Api()).isEqualTo(expectedSc2ApiTest);
    }

    private static Stream<Arguments> testMappings() {
        return Stream.of(
                of(Debug.DebugTestProcess.Test.crash, DebugTestProcess.Test.CRASH),
                of(Debug.DebugTestProcess.Test.exit, DebugTestProcess.Test.EXIT),
                of(Debug.DebugTestProcess.Test.hang, DebugTestProcess.Test.HANG));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugTestProcess.class).withNonnullFields("test").verify();
    }

}