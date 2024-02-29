package com.github.AoRingoServer

import org.bukkit.block.Block
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin

class PachinkoManager(private val plugin: Plugin) {
    private val pachinkoFileName = "pachinkoData"
    val pachinkoCountKey = "pachinkoCountKey"
    private val yml = Yml(plugin)
    private val pachinkoData = yml.getYml(pachinkoFileName)
    private val pachinkoTypeKey = "type"
    private val monitorIDKey = "monitorID"
    private fun acquisitionBlockLocation(block: Block): String {
        val blockLocation = block.location
        return "${block.world.name}x${blockLocation.x.toInt()}y${blockLocation.y.toInt()}z${blockLocation.z.toInt()}"
    }
    private fun additionalDataToPluginDataFile(key: String, value: String) {
        pachinkoData.set(key, value)
    }
    private fun acquisitionDataToPluginDataFile(key: String): String? {
        return pachinkoData.getString(key)
    }
    fun addPachinkoType(block: Block, pachinkoMachine: String) {
        val location = acquisitionBlockLocation(block)
        val key = "$location.$pachinkoTypeKey"
        additionalDataToPluginDataFile(key, pachinkoMachine)
    }
    fun acquisitionPachinkoType(block: Block): String? {
        val location = acquisitionBlockLocation(block)
        val key = "$location.$pachinkoTypeKey"
        return acquisitionDataToPluginDataFile(key)
    }
    fun addMonitorID(block: Block, monitorID: String) {
        val location = acquisitionBlockLocation(block)
        val key = "$location.$monitorIDKey"
        additionalDataToPluginDataFile(key, monitorID)
    }
    fun acquisitionMonitorID(block: Block): String? {
        val location = acquisitionBlockLocation(block)
        val key = "$location.$monitorIDKey"
        return acquisitionDataToPluginDataFile(key)
    }
    fun addContinuousCount(block: Block): Int {
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
