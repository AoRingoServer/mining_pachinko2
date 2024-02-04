package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.common.ItemManager
import com.github.AoRingoServer.common.Pachinko
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import kotlin.random.Random

class SimplePachinko() : PachinkoMachines {
    private val fasetProbability = 60
    private val redstoneProbability = 30
    private val countUsePachinkoBall = 1
    override fun shoot(block: Block, pachinkoPlayer: PachinkoPlayer) {
        val pachinko = Pachinko()
        val expressionBlock = block.location.clone().add(0.0, -1.0, 0.0).block
        val judgementProcessing = mapOf(
            Material.BEDROCK to { fastDrawing(block, pachinkoPlayer) },
            Material.REDSTONE_BLOCK to { redstoneDrawing(pachinkoPlayer, pachinko, expressionBlock) },
            Material.EMERALD_BLOCK to { emeraldBrawing(pachinkoPlayer, pachinko, expressionBlock) }
        )
        ItemManager().reducePachinkoBall(pachinkoPlayer.player, countUsePachinkoBall)
        judgementProcessing[expressionBlock.type]?.invoke() ?: return
    }
    private fun fastDrawing(block: Block, pachinkoPlayer: PachinkoPlayer) {
        val fastRandom = Random.nextInt(0, fasetProbability) == 1
        val redstoneRandom = Random.nextInt(0, redstoneProbability) == 1
        if (fastRandom) {
            block.location.clone().add(0.0, -1.0, 0.0).block.type = Material.EMERALD_BLOCK
            pachinkoPlayer.blinkingDisplay("${ChatColor.GREEN}当たる濃厚！！", Sound.ENTITY_EXPERIENCE_ORB_PICKUP, block)
        } else if (redstoneRandom) {
            block.location.clone().add(0.0, -1.0, 0.0).block.type = Material.REDSTONE_BLOCK
            pachinkoPlayer.blinkingDisplay("当たる予感", Sound.ENTITY_EXPERIENCE_ORB_PICKUP, block)
        }
    }
    private fun redstoneDrawing(pachinkoPlayer: PachinkoPlayer, pachinko: Pachinko, expressionBlock: Block) {
        val random = Random.nextInt(0, 2) == 0
        if (random) {
            pachinko.hit(pachinkoPlayer)
        }
        expressionBlock.type = Material.BEDROCK
    }
    private fun emeraldBrawing(pachinkoPlayer: PachinkoPlayer, pachinko: Pachinko, expressionBlock: Block) {
        pachinko.hit(pachinkoPlayer)
        expressionBlock.type = Material.BEDROCK
        pachinkoPlayer.player.spawnParticle(Particle.EXPLOSION_LARGE, expressionBlock.location, 100, 0.5, 0.5, 0.5, 0.1)
    }
}
