package lombok.javac.handlers.conf;

import lombok.javac.LombokPlusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class HandleReadWriteLockConfTest extends LombokPlusTest {

    @Test
    @DisplayName("Given lombok.config contains 'lombok.readWriteLock.defaultFieldName=customLock' " +
            "and method is annotated with @WriteLock " +
            "then should generate a lock field named as 'customLock' " +
            "and use it in method to generate write lock block")
    void configOverrideLockFieldName() throws IOException {
        testClass("readwritelockconf", "ConfigOverrideLockFieldName");
    }

}
