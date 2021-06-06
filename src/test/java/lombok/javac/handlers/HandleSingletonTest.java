package lombok.javac.handlers;

import lombok.sample.SingletonSample;
import org.junit.jupiter.api.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HandleSingletonTest {

    @Test
    @Order(0)
    @DisplayName("Verify existence of a inner class named '*SingletonLazyHolder'")
    void test0() {
        // given
        Class<SingletonSample> type = SingletonSample.class;

        // when
        List<Class<?>> innerClasses = Arrays.asList(type.getDeclaredClasses());

        // then
        String innerClassName = type.getName() + '$' + type.getSimpleName() + "SingletonLazyHolder";
        assertThat(innerClasses)
                .as("'lombok.SingletonTest$Sample$SampleSingletonLazyHolder' is in inner classes")
                .anyMatch(innerClass -> innerClassName.equals(innerClass.getName()));
    }

    @Test
    @Order(1)
    @DisplayName("Verify existence of method 'getInstance'")
    void test1() throws ReflectiveOperationException {
        // given
        Class<SingletonSample> type = SingletonSample.class;
        String methodName = "getInstance";

        // when
        Method getInstance = type.getDeclaredMethod(methodName);

        // then
        assertThat(getInstance).isNotNull();
        assertThat(Modifier.isStatic(getInstance.getModifiers())).isTrue();
        assertThat(getInstance.getName()).isEqualTo(methodName);

//        new SingletonSample(); // The constructor SingletonSample() is not visible.
    }

    @Test
    @Order(2)
    @DisplayName("Verify singleness of class annotated with @Singleton")
    void test2() throws ReflectiveOperationException {
        // given
        Class<SingletonSample> type = SingletonSample.class;
        String methodName = "getInstance";

        // when
        Method getInstance = type.getDeclaredMethod(methodName);

        // then
        SingletonSample instance = (SingletonSample) getInstance.invoke(null);
        SingletonSample other = (SingletonSample) getInstance.invoke(null);
        assertThat(instance)
                .isNotNull()
                .isExactlyInstanceOf(type)
                .isSameAs(other);
        assertThat(instance.getUuid()).isEqualTo(other.getUuid());
    }

}
