package com.vibeide.minecraftmod.utils

import com.vibeide.minecraftmod.data.models.ModInfo

object CodeTemplates {
    
    fun generateForgeMainClass(modInfo: ModInfo): String {
        val className = modInfo.modId.toPascalCase()
        return """
package com.example.${modInfo.modId};

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(${className}.MOD_ID)
public class ${className} {
    public static final String MOD_ID = "${modInfo.modId}";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ${className}() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        
        MinecraftForge.EVENT_BUS.register(this);
        
        LOGGER.info("${modInfo.modName} initialized!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("${modInfo.modId} common setup");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("${modInfo.modId} client setup");
    }
}
""".trimIndent()
    }

    fun generateFabricMainClass(modInfo: ModInfo): String {
        val className = modInfo.modId.toPascalCase()
        return """
package com.example.${modInfo.modId};

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ${className} implements ModInitializer {
    public static final String MOD_ID = "${modInfo.modId}";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing ${modInfo.modName}!");
    }
}
""".trimIndent()
    }

    fun generateBuildGradle(modInfo: ModInfo): String {
        return if (modInfo.modLoader.lowercase() == "forge") {
            generateForgeBuildGradle(modInfo)
        } else {
            generateFabricBuildGradle(modInfo)
        }
    }

    private fun generateForgeBuildGradle(modInfo: ModInfo): String {
        return """
plugins {
    id 'eclipse'
    id 'idea'
    id 'maven-publish'
    id 'net.minecraftforge.gradle' version '[6.0,6.2)'
}

version = '${modInfo.version}'
group = 'com.example.${modInfo.modId}'

base {
    archivesName = '${modInfo.modId}'
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

minecraft {
    mappings channel: 'official', version: '${modInfo.mcVersion}'
    
    runs {
        client {
            workingDirectory project.file('run')
            property 'forge.logging.markers', 'REGISTRIES'
            property 'forge.logging.console.level', 'debug'
            mods {
                ${modInfo.modId} {
                    source sourceSets.main
                }
            }
        }
        
        server {
            workingDirectory project.file('run')
            property 'forge.logging.markers', 'REGISTRIES'
            property 'forge.logging.console.level', 'debug'
            mods {
                ${modInfo.modId} {
                    source sourceSets.main
                }
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    minecraft 'net.minecraftforge:forge:${modInfo.mcVersion}-49.0.30'
}

jar {
    manifest {
        attributes([
            "Specification-Title"     : "${modInfo.modId}",
            "Specification-Vendor"    : "Author",
            "Specification-Version"   : "1",
            "Implementation-Title"    : project.name,
            "Implementation-Version"  : version,
            "Implementation-Vendor"   : "Author",
            "Implementation-Timestamp": new Date().format("yyyy-MM-dd'T'HH:mm:ssZ")
        ])
    }
}

tasks.withType(JavaCompile).configureEach {
    options.encoding = 'UTF-8'
}
""".trimIndent()
    }

    private fun generateFabricBuildGradle(modInfo: ModInfo): String {
        return """
plugins {
    id 'fabric-loom' version '1.4-SNAPSHOT'
    id 'maven-publish'
}

version = '${modInfo.version}'
group = 'com.example'

base {
    archivesName = '${modInfo.modId}'
}

repositories {
    mavenCentral()
}

dependencies {
    minecraft "com.mojang:minecraft:${modInfo.mcVersion}"
    mappings "net.fabricmc:yarn:${modInfo.mcVersion}+build.3:v2"
    modImplementation "net.fabricmc:fabric-loader:0.14.21"
    modImplementation "net.fabricmc.fabric-api:fabric-api:0.83.0+${modInfo.mcVersion}"
}

processResources {
    inputs.property "version", project.version
    filesMatching("fabric.mod.json") {
        expand "version": project.version
    }
}

tasks.withType(JavaCompile).configureEach {
    it.options.release = 17
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
""".trimIndent()
    }

    fun generateModsToml(modInfo: ModInfo): String {
        return """
modLoader="javafml"
loaderVersion="[49,)"
license="MIT"

[[mods]]
modId="${modInfo.modId}"
version="${modInfo.version}"
displayName="${modInfo.modName}"
description='''
A Minecraft mod created with Vibe IDE.
'''

[[dependencies.${modInfo.modId}]]
modId="forge"
mandatory=true
versionRange="[49,)"
ordering="NONE"
side="BOTH"

[[dependencies.${modInfo.modId}]]
modId="minecraft"
mandatory=true
versionRange="[${modInfo.mcVersion}]"
ordering="NONE"
side="BOTH"
""".trimIndent()
    }

    fun generateFabricModJson(modInfo: ModInfo): String {
        val className = modInfo.modId.toPascalCase()
        return """
{
    "schemaVersion": 1,
    "id": "${modInfo.modId}",
    "version": "${modInfo.version}",
    "name": "${modInfo.modName}",
    "description": "A Minecraft mod created with Vibe IDE",
    "authors": ["Author"],
    "contact": {},
    "license": "MIT",
    "environment": "*",
    "entrypoints": {
        "main": ["com.example.${modInfo.modId}.${className}"]
    },
    "depends": {
        "fabricloader": ">=0.14.21",
        "minecraft": "${modInfo.mcVersion}",
        "java": ">=17"
    }
}
""".trimIndent()
    }

    fun generateGitignore(): String {
        return """
# Gradle
.gradle/
build/
out/

# IDE
.idea/
*.iml
*.ipr
*.iws
.vscode/
*.code-workspace

# Minecraft
run/
logs/

# OS
.DS_Store
Thumbs.db

# Other
*.log
*.jar
!gradle/wrapper/gradle-wrapper.jar
""".trimIndent()
    }

    fun generateReadme(modInfo: ModInfo): String {
        return """
# ${modInfo.modName}

A Minecraft mod created with Vibe IDE.

## Information

- **Mod ID:** ${modInfo.modId}
- **Version:** ${modInfo.version}
- **Minecraft Version:** ${modInfo.mcVersion}
- **Mod Loader:** ${modInfo.modLoader}

## Building

### Prerequisites
- JDK 17 or higher
- Gradle 8.1+

### Build Instructions

1. Clone this repository
2. Open terminal in project directory
3. Run: `./gradlew build`
4. Find the built JAR in `build/libs/`

## Installation

1. Install ${modInfo.modLoader} for Minecraft ${modInfo.mcVersion}
2. Download the mod JAR file
3. Place it in your `.minecraft/mods` folder
4. Launch Minecraft with the ${modInfo.modLoader} profile

## License

MIT License

---
*Created with MineCraft Mod Vibe IDE*
""".trimIndent()
    }
}
