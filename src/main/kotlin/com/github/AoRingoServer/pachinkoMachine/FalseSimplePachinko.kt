package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PluginData
import com.github.AoRingoServer.common.Pachinko
import org.bukkit.plugin.java.JavaPlugin

class FalseSimplePachinko(val plugin: JavaPlugin, private val pachinko: Pachinko) : PachinkoMachines {
    private val simplePachinko = SimplePachinko(plugin, pachinko)
    override fun acquisitionUseBallCount(): Int {
        return 1
    }
    override fun shoot() {
        val config = PluginData.DataManager.config
        val emeraldProbability = config?.get("falseSimple.emeraldProbability").toString().toInt()
        val simpleEmeraldProbability = config?.get("simple.probability").toString().toInt()
        val redStoneProbability = simpleEmeraldProbability / 2
        simplePachinko.initialDrawing(pachinko, emeraldProbability, redStoneProbability)
    }
}
