package lombok;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * <h1>Singleton</h1>
 * <p>
 * This makes three members in the class.
 *
 * <ul>
 * <li>private default constructor in the class</li>
 * <li>private static class named own type name after 'SingletonLazyHolder'</li>
 * <li>public static method that returns itself</li>
 * </ul>
 *
 * <pre><code>
 * public class SingletonSample {
 *     private SingletonSample() {
 *     }
 *
 *     public static SingletonSample getInstance() {
 *         return SingletonSample.SingletonSampleSingletonLazyHolder.INSTANCE;
 *     }
 *
 *     private static class SingletonSampleSingletonLazyHolder {
 *         private static final SingletonSample INSTANCE = new SingletonSample();
 *
 *         private SingletonSampleSingletonLazyHolder() {
 *         }
 *     }
 * }
 * </code></pre>
 */
@Documented
@Retention(SOURCE)
@Target(TYPE)
public @interface Singleton {
}
