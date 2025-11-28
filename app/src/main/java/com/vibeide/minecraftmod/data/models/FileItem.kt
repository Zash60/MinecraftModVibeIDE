package com.vibeide.minecraftmod.data.models

data class FileItem(
    val path: String,
    val name: String,
    val content: String = "",
    val isFolder: Boolean = false,
    val children: MutableList<FileItem> = mutableListOf(),
    val isExpanded: Boolean = false,
    val isDirty: Boolean = false
) {
    fun getExtension(): String = name.substringAfterLast('.', "")
    
    fun getIcon(): String = when {
        isFolder -> "folder"
        name.endsWith(".java") -> "java"
        name.endsWith(".json") -> "json"
        name.endsWith(".toml") -> "toml"
        name.endsWith(".properties") -> "properties"
        name.endsWith(".gradle") -> "gradle"
        name.endsWith(".xml") -> "xml"
        name.endsWith(".md") -> "markdown"
        else -> "file"
    }
}

data class OpenFile(
    val path: String,
    val name: String,
    var content: String,
    var isDirty: Boolean = false,
    val originalContent: String = content
)
