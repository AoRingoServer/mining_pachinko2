package com.github.AoRingoServer

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.IOException

class Yml(private val plugin: Plugin) {
    fun loadYml() {
        PluginData.DataManager.config = loadConfig()
    }
    fun setYml(plugin: Plugin, fileName: String, key: String, value: String) {
        val playerDataFile = File(plugin.dataFolder, "$fileName.yml")

        // YAMLファイルを読み込む
        val yamlConfiguration = YamlConfiguration.loadConfiguration(playerDataFile)

        // キーと値を設定
        yamlConfiguration.set(key, value)

        // 保存
        try {
            yamlConfiguration.save(playerDataFile)
            println("'$value' がキー '$key' に追加されました: $fileName.yml")
        } catch (e: IOException) {
            println("データの保存中にエラーが発生しました: ${e.message}")
        }
    }
    fun getYml(fileName: String): YamlConfiguration {
        val filePath = File(plugin.dataFolder, "$fileName.yml")
        return YamlConfiguration.loadConfiguration(filePath)
    }
    fun loadConfig(): YamlConfiguration {
        return getYml("config")
    }
}
