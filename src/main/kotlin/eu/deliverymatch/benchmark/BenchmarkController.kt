package eu.deliverymatch.benchmark

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Random
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("benchmark")
class BenchmarkController {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("generate/{arrayCount}/{arrayLength}")
    fun generateArray(@PathVariable arrayCount: Int, @PathVariable arrayLength: Int): List<IntArray> =
        (1..arrayCount).map {
            IntArray(arrayLength) { Random().nextInt(0, 10000) }
        }

    @PostMapping
    fun benchmark(
        @RequestBody nestedArray: List<IntArray>,
        @RequestParam(defaultValue = "false") parallel: Boolean
    ) {
        val duration = if (parallel)
            measureTimeMillis {
                nestedArray.parallelStream().forEach { array ->
                    bubbleSort(array)
                }
            }
        else
            measureTimeMillis {
                nestedArray.forEach { array ->
                    bubbleSort(array)
                }
            }

        logger.info("Sorted array in ${duration}ms. parallel: $parallel")

        return
    }

    fun bubbleSort(arr: IntArray): IntArray {
        var swap = true
        while (swap) {
            swap = false
            for (i in 0 until arr.size - 1) {
                if (arr[i] > arr[i + 1]) {
                    val temp = arr[i]
                    arr[i] = arr[i + 1]
                    arr[i + 1] = temp

                    swap = true
                }
            }
        }
        return arr
    }

}