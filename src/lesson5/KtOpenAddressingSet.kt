package lesson5

/**
 * Множество(таблица) с открытой адресацией на 2^bits элементов без возможности роста.
 */
class KtOpenAddressingSet<T : Any>(private val bits: Int) : AbstractMutableSet<T>() {
    init {
        require(bits in 2..31)
    }

    private val capacity = 1 shl bits

    private val storage = Array<Any?>(capacity) { null }

    override var size: Int = 0

    private val deletedIndexes = mutableSetOf<Int>()

    /**
     * Индекс в таблице, начиная с которого следует искать данный элемент
     */
    private fun T.startingIndex(): Int {
        return hashCode() and (0x7FFFFFFF shr (31 - bits))
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    //Трудоёмкость O((1 / (1 - A))), где A = (size / capacity) - коэфициент заполнения таблицы, из лекции
    override fun contains(element: T): Boolean {
        val startingIndex = element.startingIndex();
        var index = startingIndex;
        var current = storage[index]
        while (current != null) {
            if (current == element && !deletedIndexes.contains(index)) {
                return true
            }
            index = (index + 1) % capacity
            current = storage[index]
            if (index == startingIndex) {
                return false
            }
        }
        return false
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    //Трудоёмкость O((1 / (1 - A))), где A = (size / capacity) - коэфициент заполнения таблицы, из лекции
    override fun add(element: T): Boolean {
        val startingIndex = element.startingIndex()
        var index = startingIndex
        var firstDeletedIndex: Int? = null
        var current = storage[index]

        while (current != null) {
            if (current == element && !deletedIndexes.contains(index)) {
                return false
            }
            if (deletedIndexes.contains(index) && firstDeletedIndex == null) {
                firstDeletedIndex = index;
            }
            index = (index + 1) % capacity
            check(index != startingIndex) { "Table is full" }
            current = storage[index]
        }
        index = firstDeletedIndex ?: index
        storage[index] = element
        deletedIndexes.remove(index)
        size++
        return true
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблице, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: [java.util.Set.remove] (Ctrl+Click по remove)
     *
     * Средняя
     */
    //Трудоёмкость O((1 / (1 - A))), где A = (size / capacity) - коэфициент заполнения таблицы, из лекции
    override fun remove(element: T): Boolean {
        val startingIndex = element.startingIndex();
        var index = startingIndex
        var current = storage[index]
        while (current != null) {
            if (current == element && !deletedIndexes.contains(index)) {
                deletedIndexes.add(index)
                size--
                return true
            }
            index = (index + 1) % capacity
            if (index == startingIndex) {
                return false
            }
            current = storage[index]
        }
        return false
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    override fun iterator(): MutableIterator<T> {
        return SetIterator()
    }

    private inner class SetIterator<T> : MutableIterator<T> {
        var next: Any?
        var consideredIndex = 0
        var removableIndex: Int? = null

        init {
            next = setNext()
        }

        //Трудоёмоксть O(1)
        override fun hasNext() = next != null

        //Трудоёмкость O(1 / A), где A = size / capacity - коэфициент заполнения таблицы
        override fun next(): T {
            if (next == null) {
                throw NoSuchElementException()
            }
            val returnedNext = next
            removableIndex = consideredIndex++
            next = setNext()
            return returnedNext as T
        }

        //Трудоемкость O(1)
        override fun remove() {
            check(removableIndex != null)
            deletedIndexes.add(removableIndex!!)
            size--
            removableIndex = -1
        }

        private fun setNext(): Any? {
            if (consideredIndex == storage.size) {
                return null
            }
            while (storage[consideredIndex] == null || deletedIndexes.contains(consideredIndex)) {
                consideredIndex++
                if (consideredIndex == storage.size) {
                    return null
                }
            }
            return storage[consideredIndex]
        }
    }
}
