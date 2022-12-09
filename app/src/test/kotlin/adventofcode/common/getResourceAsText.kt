package adventofcode.common

inline fun <reified T> T.getResourceAsText(path: String): String =
    checkNotNull(T::class.javaClass.getResource("/${T::class.simpleName?.lowercase()}/$path")).readText()
