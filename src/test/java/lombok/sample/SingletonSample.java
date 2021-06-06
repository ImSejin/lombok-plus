package lombok.sample;

import lombok.Getter;
import lombok.Singleton;

import java.util.UUID;

public interface SingletonSample {

    UUID getUuid();

    @Getter
    class Vanilla implements SingletonSample {
        private final UUID uuid = UUID.randomUUID();

        private Vanilla() {
        }

        public static Vanilla getInstance() {
            return Vanilla.VanillaSingletonLazyHolder.INSTANCE;
        }

        private static class VanillaSingletonLazyHolder {
            private static final Vanilla INSTANCE = new Vanilla();

            private VanillaSingletonLazyHolder() {
            }
        }
    }

    @Getter
    @Singleton
    class Lombok implements SingletonSample {
        private final UUID uuid = UUID.randomUUID();
    }

}
