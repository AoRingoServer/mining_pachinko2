package com.github.AoRingoServer.common

import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.Staging
import org.bukkit.Sound
import org.bukkit.block.Block

class Pachinko() {
    fun hit(pachinkoPlayer: PachinkoPlayer, block: Block, amount: Int, staging: Staging) {
        val player = pachinkoPlayer.player
        ItemManager().givePachinkoBall(pachinkoPlayer.player, amount)
        player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
        staging.blinkingDisplay(pachinkoPlayer, "当たり！！", Sound.ENTITY_EXPERIENCE_ORB_PICKUP, block)
    }
}
