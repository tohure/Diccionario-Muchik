package dev.tohure.muchik_dictionary

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform