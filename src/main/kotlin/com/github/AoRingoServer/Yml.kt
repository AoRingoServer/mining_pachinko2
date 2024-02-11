package com.github.AoRingoServer

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File

class Yml(val plugin: Plugin) {
    fun getYml(path: String, fileName: String): YamlConfiguration {
        val playerDataFolder = File(plugin.dataFolder, path)
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs()
        }
        val filePath = File(playerDataFolder, "$fileName.yml")
        return YamlConfiguration.loadConfiguration(filePath)
    }
}
