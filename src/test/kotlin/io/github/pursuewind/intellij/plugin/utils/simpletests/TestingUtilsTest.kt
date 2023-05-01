package io.github.pursuewind.intellij.plugin.utils.simpletests

import com.intellij.testFramework.UsefulTestCase.assertThrows
import io.github.pursuewind.intellij.plugin.utils.TEST
import org.junit.Test

class TestingUtilsTest {

    @Test
    fun `object should be able to use foo`() {
        assert(ObjectUsesFoo.foo())
    }

    @Test
    fun `using bar should crash`() {
        assertThrows(Throwable::class.java) {
            ObjectUsesFoo.bar()
        }
    }
}

private object ObjectUsesFoo : TestingUtilsHelper {
    override fun foo() = true
    override fun bar() = TEST()
}

private interface TestingUtilsHelper {
    fun foo(): Boolean
    fun bar(): Boolean
}
