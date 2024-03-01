package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PluginData
import com.github.AoRingoServer.common.Pachinko
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class SimplePachinko(val plugin: JavaPlugin, private val pachinko: Pachinko) : PachinkoMachines {
    private val redStoneHitMessage = "${ChatColor.RED}1/2チャンス！"
    private val emeraldHitMessage = "${ChatColor.GREEN}HIT!!"
    private val amount = 50
    override fun acquisitionUseBallCount(): Int {
        return 1
    }
    override fun shoot() {
        val config = PluginData.DataManager.config
        val emeraldProbability = config?.get("simple.probability").toString().toInt()
        val redStoneProbability = emeraldProbability / 2
        initialDrawing(pachinko, emeraldProbability, redStoneProbability)
    }
    fun initialDrawing(pachinko: Pachinko, emeraldProbability: Int, redStoneProbability: Int) {
        val judgementProcessing = mapOf(
            Material.BEDROCK to { fastDrawing(pachinko, emeraldProbability, redStoneProbability) },
            Material.REDSTONE_BLOCK to { redstoneDrawing(pachinko) },
            Material.EMERALD_BLOCK to { emeraldBrawing(pachinko) }
        )
        judgementProcessing[pachinko.stagingBlock.type]?.invoke() ?: return
    }
    private fun fastDrawing(pachinko: Pachinko, emeraldProbability: Int, redStoneProbability: Int) {
        val fastRandom = Random.nextInt(0, emeraldProbability) == 1
        val redstoneRandom = Random.nextInt(0, redStoneProbability) == 1
        if (fastRandom) {
            fastHit(pachinko, Material.EMERALD_BLOCK, emeraldHitMessage)
        } else if (redstoneRandom) {
            fastHit(pachinko, Material.REDSTONE_BLOCK, redStoneHitMessage)
        }
    }
    private fun fastHit(pachinko: Pachinko, changeBlock: Material, message: String) {
        val stagingBlock = pachinko.breakBlock.location.clone().add(0.0, -1.0, 0.0).block
        stagingBlock.type = changeBlock
        pachinko.pachinkoPlayer.player.spawnParticle(Particle.LAVA, pachinko.breakBlock.location, 100, 0.5, 0.5, 0.5, 0.1)
        pachinko.blinkingDisplay(message, Sound.ENTITY_EXPERIENCE_ORB_PICKUP)
    }
    private fun redstoneDrawing(pachinko: Pachinko) {
        val random = Random.nextInt(0, 2) == 0
        if (random) {
            pachinko.hit(pachinko.pachinkoPlayer, amount)
        }
        pachinko.stagingBlock.type = Material.BEDROCK
    }
    private fun emeraldBrawing(pachinko: Pachinko) {
        pachinko.hit(pachinko.pachinkoPlayer, amount)
        pachinko.stagingBlock.type = Material.BEDROCK
        pachinko.pachinkoPlayer.player.spawnParticle(Particle.EXPLOSION_LARGE, pachinko.stagingBlock.location, 100, 0.5, 0.5, 0.5, 0.1)
    }
}
