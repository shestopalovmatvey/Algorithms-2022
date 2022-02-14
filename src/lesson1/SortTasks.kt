@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import kotlin.math.absoluteValue

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortTimes(inputName: String, outputName: String) {
    val check = Regex("""^(0[1-9]|1[0-2]):([0-5]\d):([0-5]\d)\s(A|P)M""")
    val amTime = mutableListOf<String>()
    val pmTime = mutableListOf<String>()
    val writer = File(outputName).bufferedWriter()
    for (line in File(inputName).readLines()) {
        if (check.matches(line)) {
            var time = line
            if (time.startsWith("12")) {
                time = time.replaceFirst("12", "00")
            }
            if (time.endsWith("AM")) {
                amTime.add(time)
            } else {
                pmTime.add(time)
            }
        } else {
            throw IllegalArgumentException()
        }
    }
    amTime.sort()
    pmTime.sort()
    for (line in amTime) {
        var time = line
        if (time.startsWith("00")) {
            time = time.replaceFirst("00", "12")
        }
        writer.write(time)
        writer.newLine()
    }
    for (line in pmTime) {
        var time = line
        if (time.startsWith("00")) {
            time = time.replaceFirst("00", "12")
        }
        writer.write(time)
        writer.newLine()
    }
    writer.close()
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) {
    val setAddresses =
        sortedMapOf<String, MutableSet<String>>(compareBy<String> { it.split(" ")[0] }.thenBy
        { it.split(" ")[1].toInt() })
    val reg = Regex("""[а-яА-ЯёЁ]+\s[а-яА-ЯёЁ]+\s-[а-яА-ЯёЁ]+\s\d+""")
    val writer = File(outputName).bufferedWriter()
    for (line in File(inputName).readLines()) {
        if (!reg.matches(line)) {
            val address = line.split(" - ")
            val nameList = mutableSetOf<String>()
            nameList.add(address[0])
            if (setAddresses.contains(address[1])) {
                setAddresses[address[1]]?.add(address[0])
            } else {
                setAddresses[address[1]] = nameList
            }
        } else {
            throw IllegalArgumentException()
        }
    }
    writer.use {
        for ((key, value) in setAddresses) {
            it.write(key + " - " + value.sorted().joinToString(", "))
            it.newLine()
        }
    }
    writer.close()
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
fun sortTemperatures(inputName: String, outputName: String) {
    val reg = Regex("""-?\d+\.\d""")
    val tempList = mutableListOf<Double>()
    val writer = File(outputName).bufferedWriter()
    for (line in File(inputName).readLines()) {
        if (reg.matches(line)) {
            tempList.add(line.toDouble())
        } else {
            throw IllegalArgumentException()
        }
    }
    tempList.sort()
    for (i in tempList) {
        writer.write(i.toString())
        writer.newLine()
    }
    writer.close()
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    TODO()
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}

