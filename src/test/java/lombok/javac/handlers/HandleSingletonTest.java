package lombok.javac.handlers;

import lombok.sample.SingletonSample;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HandleSingletonTest {

    @Order(0)
    @ParameterizedTest
    @ValueSource(classes = {SingletonSample.Vanilla.class, SingletonSample.Lombok.class})
    @DisplayName("Verify existence of a inner class named '*SingletonLazyHolder'")
    void test0(Class<?> type) {
        // when
        List<Class<?>> innerClasses = Arrays.asList(type.getDeclaredClasses());

        // then
        String innerClassName = type.getName() + '$' + type.getSimpleName() + "SingletonLazyHolder";
        assertThat(innerClasses)
                .as("'*SingletonLazyHolder' is in the class as a inner class")
                .anyMatch(innerClass -> innerClassName.equals(innerClass.getName()));
    }

    @Order(1)
    @ParameterizedTest
    @ValueSource(classes = {SingletonSample.Vanilla.class, SingletonSample.Lombok.class})
    @DisplayName("Verify existence of method 'getInstance'")
    void test1(Class<?> type) throws ReflectiveOperationException {
        // given
        String methodName = "getInstance";

        // when
        Method getInstance = type.getDeclaredMethod(methodName);

        // then
        assertThat(getInstance).isNotNull();
        assertThat(Modifier.isStatic(getInstance.getModifiers())).isTrue();
        assertThat(getInstance.getName()).isEqualTo(methodName);

//        new SingletonSample(); // The constructor SingletonSample() is not visible.
    }

    @Order(2)
    @ParameterizedTest
    @ValueSource(classes = {SingletonSample.Vanilla.class, SingletonSample.Lombok.class})
    @DisplayName("Verify singleness of class annotated with @Singleton")
    void test2(Class<?> type) throws ReflectiveOperationException {
        // given
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
