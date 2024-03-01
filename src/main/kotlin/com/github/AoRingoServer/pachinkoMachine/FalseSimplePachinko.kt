package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.PluginData
import com.github.AoRingoServer.Staging
import org.bukkit.block.Block
import org.bukkit.plugin.java.JavaPlugin

class FalseSimplePachinko(val plugin: JavaPlugin) : PachinkoMachines {
    private val simplePachinko = SimplePachinko(plugin)
    override fun acquisitionUseBallCount(): Int {
        return 1
    }
    override fun shoot(block: Block, stagingBlock: Block, pachinkoPlayer: PachinkoPlayer, staging: Staging) {
        val config = PluginData.DataManager.config
        val emeraldProbability = config?.get("falseSimple.emeraldProbability").toString().toInt()
        val simpleEmeraldProbability = config?.get("simple.probability").toString().toInt()
        val redStoneProbability = simpleEmeraldProbability / 2
        simplePachinko.initialDrawing(block, stagingBlock, pachinkoPlayer, emeraldProbability, redStoneProbability, staging)
    }
}
