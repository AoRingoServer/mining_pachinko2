package com.github.AoRingoServer

import com.github.AoRingoServer.Commands.PachinkoCommand
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    override fun onEnable() {
        super.onEnable()
        server.pluginManager.registerEvents(Events(this), this)
        getCommand("pachinko")!!.setExecutor(PachinkoCommand(this))
        this.dataFolder.mkdirs()
        Yml(this).makeImagesFolder()
        saveResource("config.yml", false)
        PluginData.DataManager.config = Yml(this).loadConfig()
    }

    override fun onDisable() {
        super.onDisable()
    }
}
