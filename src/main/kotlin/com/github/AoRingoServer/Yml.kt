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
    fun makeImagesFolder() {
        val folder = File(plugin.dataFolder, "images/")
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }
    fun addList(fileName: String, key: String, value: String) {
        val file = File(plugin.dataFolder, fileName)

        // ファイルが存在しない場合は新規作成
        if (!file.exists()) {
            file.createNewFile()
        }

        // YAMLファイルとして読み込み
        val ymlFIle = YamlConfiguration.loadConfiguration(file)

        // 指定されたキーが存在するか確認し、リストを取得する
        val list = ymlFIle.getStringList(key)

        // 値をリストに追加
        list.add(value)

        // 更新したリストをYAMLファイルに保存
        ymlFIle.set(key, list)
        ymlFIle.save(file)
    }
    fun getList(fileName: String, key: String): MutableList<String> {
        val file = File(plugin.dataFolder, fileName)

        // ファイルが存在しない場合は新規作成
        if (!file.exists()) {
            file.createNewFile()
        }

        // YAMLファイルとして読み込み
        val ymlFIle = YamlConfiguration.loadConfiguration(file)

        // 指定されたキーが存在するか確認し、リストを取得する
        return ymlFIle.getStringList(key)
    }
}
