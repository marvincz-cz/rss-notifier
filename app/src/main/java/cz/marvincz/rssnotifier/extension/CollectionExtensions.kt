package cz.marvincz.rssnotifier.extension

fun <T> MutableList<T>.swap(from: Int, to: Int) {
    val valFrom = this[from]
    val valTo = this[to]
    this[from] = valTo
    this[to] = valFrom
}

fun List<*>.validIndex(index: Int) = when {
    index in indices -> index
    index < 0 -> 0
    index > lastIndex -> lastIndex
    else -> throw IllegalStateException("Unreachable")
}

fun <K, V> Map<K, V>.copyAndPut(key: K, value: V): Map<K, V> {
    return toMutableMap().apply{ put(key, value) }.toMap()
}