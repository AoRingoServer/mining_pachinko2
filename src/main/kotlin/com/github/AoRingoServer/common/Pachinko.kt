package com.github.AoRingoServer.common

import com.github.AoRingoServer.PachinkoPlayer
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class Pachinko(private val plugin: JavaPlugin, val breakBlock: Block, val stagingBlock: Block, val pachinkoPlayer: PachinkoPlayer) {
    fun hit(pachinkoPlayer: PachinkoPlayer, amount: Int) {
        val player = pachinkoPlayer.player
        ItemManager().givePachinkoBall(pachinkoPlayer.player, amount)
        player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
        blinkingDisplay("当たり！！", Sound.ENTITY_EXPERIENCE_ORB_PICKUP)
    }
    fun blinkingDisplay(message: String, sound: Sound, subMessage: String = "") {
        val interval = 9L
        var c = 8
        blockBreak(breakBlock ?: return)
        val player = pachinkoPlayer?.player
        player?.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 20, 255))
        object : BukkitRunnable() {
            override fun run() {
                if (c % 2 == 0) {
                    player?.sendTitle("${ChatColor.YELLOW}$message", subMessage, 5, 5, 5)
                } else {
                    player?.sendTitle(message, subMessage, 5, 5, 5)
                }
                player?.playSound(player, sound, 1f, 1f)
                c --
                if (c == 0) {
                    blockCancellationBreak(breakBlock)
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
