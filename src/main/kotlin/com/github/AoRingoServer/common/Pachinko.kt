package com.github.AoRingoServer.common

import com.github.AoRingoServer.PachinkoPlayer
import org.bukkit.Sound
import org.bukkit.block.Block

class Pachinko {
    fun hit(pachinkoPlayer: PachinkoPlayer, block: Block) {
        val player = pachinkoPlayer.player
        ItemManager().givePachinkoBall(pachinkoPlayer.player)
        player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
        pachinkoPlayer.blinkingDisplay("当たり！！", Sound.ENTITY_EXPERIENCE_ORB_PICKUP, block)
    }
}
