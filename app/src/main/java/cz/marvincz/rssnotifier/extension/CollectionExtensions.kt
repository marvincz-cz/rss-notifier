package cz.marvincz.rssnotifier.extension

fun List<*>.validIndex(index: Int) = when {
    index in indices -> index
    index < 0 -> 0
    index > lastIndex -> lastIndex
    else -> 0
}

fun <K, V> Map<K, V>.copyAndPut(key: K, value: V): Map<K, V> {
    return toMutableMap().apply{ put(key, value) }.toMap()
}