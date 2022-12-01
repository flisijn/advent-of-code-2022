package adventofcode.common

fun getResourceAsText(path: String): String =
    checkNotNull(object {}.javaClass.getResource(path)).readText()
