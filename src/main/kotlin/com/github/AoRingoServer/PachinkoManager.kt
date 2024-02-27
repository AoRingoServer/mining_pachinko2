package com.github.AoRingoServer

import org.bukkit.block.Block
import org.bukkit.plugin.Plugin

class PachinkoManager(private val plugin: Plugin) {
    private val pachinkoFileName = "pachinkoData"
    private val yml = Yml(plugin)
    private fun acquisitionBlockLocation(block: Block): String {
        val blockLocation = block.location
        return "${block.world.name}x${blockLocation.x.toInt()}y${blockLocation.y.toInt()}z${blockLocation.z.toInt()}"
    }
    fun addPachinkoType(block: Block, pachinkoMachine: String) {
        val key = acquisitionBlockLocation(block)
        yml.setYml(plugin, pachinkoFileName, key, pachinkoMachine)
    }
    fun acquisitionPachinkoType(block: Block): String? {
        val key = acquisitionBlockLocation(block)
        val pachinkoData = yml.getYml(pachinkoFileName)
        return pachinkoData.getString(key)
    }
}
