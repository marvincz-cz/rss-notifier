package cz.marvincz.rssnotifier.navigation

enum class NavigationScreen(private val id: String, val param: String? = null) {
    CHANNELS("channels"),
    MANAGE_CHANNELS("manageChannels", "addChannel"),
    SETTINGS("settings");

    val route = buildString {
        append(id)
        if (param != null) {
            append("?$param={$param}")
        }
    }

    fun route(value: Any) = buildString {
        append(id)
        if (param != null) {
            append("?$param=$value")
        }
    }
}