package libs.math

import junit.framework.TestCase
import org.junit.Test

class ConfusionMatrixTest: TestCase() {

    @Test
    fun test() {
        // 全猜對
        println(ConfusionMatrix(6,4,0,0))

        // 全猜錯
        println(ConfusionMatrix(0,0,6,4))

        // 該是true猜成false
        println(ConfusionMatrix(5,4,0,1))

        // 該是false猜成true
        println(ConfusionMatrix(5,4,1,0))

    }
}
