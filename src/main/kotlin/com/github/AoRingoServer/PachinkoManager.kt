package com.github.AoRingoServer

import org.bukkit.block.Block
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin

class PachinkoManager(private val plugin: Plugin) {
    private val pachinkoTypeKey = "pachinkoType"
    fun addPachinkoType(block: Block, pachinkoMachine: String) {
        block.setMetadata(pachinkoTypeKey, FixedMetadataValue(plugin, pachinkoMachine))
    }
    fun acquisitionPachinkoType(block: Block): String? {
        val metadata = block.getMetadata(pachinkoTypeKey)
        if (metadata.isNotEmpty()) {
            return metadata[0].asString()
        }
        return null
    }
    fun resetPachinkoType(block: Block) {
        block.removeMetadata(pachinkoTypeKey, plugin)
    }
}
