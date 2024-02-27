package com.github.AoRingoServer

import org.bukkit.block.Block
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin

class PachinkoManager(private val plugin: Plugin) {
    private val pachinkoFileName = "pachinkoData"
    val pachinkoCountKey = "pachinkoCountKey"
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
    fun addcontinuousCount(block: Block): Int {
        val count = acquisitionTemporaryIntData(block, pachinkoCountKey) ?: 0
        val newCount = count + 1
        setTemporaryIntData(block, pachinkoCountKey, newCount)
        return newCount
    }
    fun setTemporaryIntData(block: Block, key: String, data: Int) {
        block.setMetadata(key, FixedMetadataValue(plugin, data))
    }
    private fun acquisitionTemporaryIntData(block: Block, key: String): Int? {
        val metadata = block.getMetadata(key)
        if (metadata.isNotEmpty()) {
            return metadata[0].asInt()
        }
        return null
    }
}
