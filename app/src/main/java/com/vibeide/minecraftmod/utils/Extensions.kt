package com.vibeide.minecraftmod.utils

import java.text.SimpleDateFormat
import java.util.*

fun String.toPascalCase(): String {
    return this.split(Regex("[-_]"))
        .joinToString("") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
}

fun String.getFileExtension(): String {
    return this.substringAfterLast('.', "")
}

fun String.getFileName(): String {
    return this.substringAfterLast('/')
}

fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

fun String.isJavaFile() = this.endsWith(".java")
fun String.isJsonFile() = this.endsWith(".json")
fun String.isTomlFile() = this.endsWith(".toml")
fun String.isPropertiesFile() = this.endsWith(".properties")
