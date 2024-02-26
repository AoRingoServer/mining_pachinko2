package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.PluginData
import com.github.AoRingoServer.Staging
import com.github.AoRingoServer.common.Pachinko
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import kotlin.random.Random

class SimplePachinko : PachinkoMachines {
    private val redStoneHitMessage = "${ChatColor.RED}1/2チャンス！"
    private val emeraldHitMessage = "${ChatColor.GREEN}HIT!!"
    private val amount = 50
    override fun acquisitionUseBallCount(): Int {
        return 1
    }
    override fun shoot(block: Block, stagingBlock: Block, pachinkoPlayer: PachinkoPlayer, staging: Staging) {
        val config = PluginData.DataManager.config
        val emeraldProbability = config?.get("simple.probability").toString().toInt()
        val redStoneProbability = emeraldProbability / 2
        initialDrawing(block, stagingBlock, pachinkoPlayer, emeraldProbability, redStoneProbability, staging)
    }
    fun initialDrawing(block: Block, stagingBlock: Block, pachinkoPlayer: PachinkoPlayer, emeraldProbability: Int, redStoneProbability: Int, staging: Staging) {
        val pachinko = Pachinko()
        val judgementProcessing = mapOf(
            Material.BEDROCK to { fastDrawing(block, pachinkoPlayer, emeraldProbability, redStoneProbability, staging) },
            Material.REDSTONE_BLOCK to { redstoneDrawing(pachinkoPlayer, pachinko, block, stagingBlock, staging) },
            Material.EMERALD_BLOCK to { emeraldBrawing(pachinkoPlayer, pachinko, block, stagingBlock, staging) }
        )
        judgementProcessing[stagingBlock.type]?.invoke() ?: return
    }
    private fun fastDrawing(block: Block, pachinkoPlayer: PachinkoPlayer, emeraldProbability: Int, redStoneProbability: Int, staging: Staging) {
        val fastRandom = Random.nextInt(0, emeraldProbability) == 1
        val redstoneRandom = Random.nextInt(0, redStoneProbability) == 1
        if (fastRandom) {
            fastHit(block, pachinkoPlayer, Material.EMERALD_BLOCK, emeraldHitMessage, staging)
        } else if (redstoneRandom) {
            fastHit(block, pachinkoPlayer, Material.REDSTONE_BLOCK, redStoneHitMessage, staging)
        }
    }
    private fun fastHit(block: Block, pachinkoPlayer: PachinkoPlayer, changeBlock: Material, message: String, staging: Staging) {
        val stagingBlock = block.location.clone().add(0.0, -1.0, 0.0).block
        stagingBlock.type = changeBlock
        pachinkoPlayer.player.spawnParticle(Particle.LAVA, block.location, 100, 0.5, 0.5, 0.5, 0.1)
        staging.blinkingDisplay(pachinkoPlayer, message, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, block)
    }
    private fun redstoneDrawing(pachinkoPlayer: PachinkoPlayer, pachinko: Pachinko, block: Block, expressionBlock: Block, staging: Staging) {
        val random = Random.nextInt(0, 2) == 0
        if (random) {
            pachinko.hit(pachinkoPlayer, block, amount, staging)
        }
        expressionBlock.type = Material.BEDROCK
    }
    private fun emeraldBrawing(pachinkoPlayer: PachinkoPlayer, pachinko: Pachinko, block: Block, expressionBlock: Block, staging: Staging) {
        pachinko.hit(pachinkoPlayer, block, amount, staging)
        expressionBlock.type = Material.BEDROCK
        pachinkoPlayer.player.spawnParticle(Particle.EXPLOSION_LARGE, expressionBlock.location, 100, 0.5, 0.5, 0.5, 0.1)
    }
}
