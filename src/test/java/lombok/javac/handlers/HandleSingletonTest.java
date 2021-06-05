package lombok.javac.handlers;

import lombok.sample.SingletonSample;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HandleSingletonTest {

    @Test
    @DisplayName("Verify existence of a inner class for singleton lazy holder")
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
        assertThat(getInstance.invoke(null)).isNotNull().isExactlyInstanceOf(type);

//        new SingletonSample(); // The constructor SingletonSample() is not visible.
    }

}
