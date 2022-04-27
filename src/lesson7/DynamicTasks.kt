@file:Suppress("UNUSED_PARAMETER")

package lesson7

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */

fun longestCommonSubSequence(first: String, second: String): String {
    //Трудоёмкость O(n*m) n-длинна одной строки, m-другой
    //Ресурсоёмкость O(n)
    val arrayOfIntArrays = Array(first.length + 1) { IntArray(second.length + 1) }
    for (i in first.indices) {
        for (j in second.indices) {
            if (first[i] == second[j]) {
                arrayOfIntArrays[i + 1][j + 1] = arrayOfIntArrays[i][j] + 1
            } else {
                arrayOfIntArrays[i + 1][j + 1] = Math.max(arrayOfIntArrays[i][j + 1], arrayOfIntArrays[i + 1][j])
            }
        }
    }
    var i = first.length
    var j = second.length
    val answer = StringBuilder()
    while (arrayOfIntArrays[i][j] > 0) {
        when {
            arrayOfIntArrays[i][j] == arrayOfIntArrays[i - 1][j] -> {
                i -= 1
            }
            arrayOfIntArrays[i][j] == arrayOfIntArrays[i][j - 1] -> {
                j -= 1
            }
            else -> {
                answer.insert(0, first[i - 1])
                i -= 1
                j -= 1
            }
        }
    }
    return answer.toString()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */

fun longestIncreasingSubSequence(list: List<Int>): List<Int?> {
    //Трудоёмкость O(n^2)
    //Ресурсоёмкость O(n)
    val emptyList: MutableList<Int?> = mutableListOf()
    if (list.isEmpty()) {
        return emptyList
    }
    val d = IntArray(list.size)
    d[0] = 1
    for (i in 1 until list.size) {
        var longest = 0
        for (j in i - 1 downTo 0) {
            if (list[i] >= list[j] && d[j] >= longest) {
                longest = d[j]
            }
        }
        d[i] = longest + 1
    }
    var max = 1
    var maxI = 0
    for (i in 1 until list.size) {
        if (d[i] > max) {
            max = d[i]
            maxI = i
        }
    }
    val listAnswer = mutableListOf<Int?>()
    while (max > 0) {
        val last = maxI
        listAnswer.add(0, list[maxI])
        max--
        for (i in 0 until maxI) {
            if (d[i] == max && list[i] < list[last]) {
                maxI = i
                break
            }
        }
    }
    return listAnswer
}
/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {
    TODO()
}
