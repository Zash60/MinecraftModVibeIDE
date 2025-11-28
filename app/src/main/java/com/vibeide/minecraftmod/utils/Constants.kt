package com.vibeide.minecraftmod.utils

object Constants {
    const val DEFAULT_GEMINI_API_KEY = "AIzaSyB7t0WcQ71kTombp2XQCr4T1zzzQ499G8c"
    const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"
    const val GITHUB_BASE_URL = "https://api.github.com/"
    
    val MC_VERSIONS = listOf("1.20.4", "1.20.1", "1.19.4", "1.18.2", "1.16.5")
    val MOD_LOADERS = listOf("Forge", "Fabric")
    
    val SUGGESTION_PROMPTS = listOf(
        "Item" to "Crie um item personalizado com textura e crafting",
        "Bloco" to "Crie um bloco com propriedades especiais",
        "Mob" to "Crie um mob hostil com comportamento único",
        "Dimensão" to "Crie uma dimensão nova com biomas únicos",
        "Magia" to "Crie um sistema de magia com feitiços",
        "Armadura" to "Crie uma armadura com poderes especiais"
    )
}
