package lombok.sample;

import lombok.Getter;
import lombok.Singleton;

import java.util.UUID;

@Getter
@Singleton
public class SingletonSample {

    private final UUID uuid = UUID.randomUUID();

}
