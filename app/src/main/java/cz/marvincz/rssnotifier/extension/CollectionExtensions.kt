package cz.marvincz.rssnotifier.extension

fun <T> MutableList<T>.swap(from: Int, to: Int) {
    val valFrom = this[from]
    val valTo = this[to]
    this[from] = valTo
    this[to] = valFrom
}