package com.github.AoRingoServer

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class Staging(private val plugin: Plugin) {
    fun blinkingDisplay(pachinkoPlayer: PachinkoPlayer, message: String, sound: Sound, breakBlock: Block? = null) {
        val interval = 9L
        var c = 8
        if (breakBlock != null) {
            blockBreak(breakBlock)
        }
        val player = pachinkoPlayer.player
        player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 20, 255))
        object : BukkitRunnable() {
            override fun run() {
                if (c % 2 == 0) {
                    player.sendTitle("${ChatColor.YELLOW}$message", "", 5, 5, 5)
                } else {
                    player.sendTitle(message, "", 5, 5, 5)
                }
                player.playSound(player, sound, 1f, 1f)
                c --
                if (c == 0) {
                    if (breakBlock != null) {
                        blockCancellationBreak(breakBlock)
                    }
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, interval) // 1秒間隔 (20 ticks) でタスクを実行
    }
    private fun blockBreak(block: Block) {
        block.type = Material.BEDROCK
    }
    private fun blockCancellationBreak(block: Block) {
        block.type = Material.EMERALD_ORE
    }
}
