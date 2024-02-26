package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.PluginData
import org.bukkit.block.Block

class FalseSimplePachinko : PachinkoMachines {
    private val simplePachinko = SimplePachinko()
    override fun acquisitionUseBallCount(): Int {
        return 1
    }
    override fun shoot(block: Block, pachinkoPlayer: PachinkoPlayer) {
        val config = PluginData.DataManager.config
        val emeraldProbability = config?.get("falseSimple.emeraldProbability").toString().toInt()
        val simpleEmeraldProbability = config?.get("simple.probability").toString().toInt()
        val redStoneProbability = simpleEmeraldProbability / 2
        simplePachinko.initialDrawing(block, pachinkoPlayer, emeraldProbability, redStoneProbability)
    }
}
