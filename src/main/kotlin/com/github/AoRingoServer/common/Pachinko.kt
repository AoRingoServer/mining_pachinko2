package com.github.AoRingoServer.common

import com.github.AoRingoServer.PachinkoPlayer
import org.bukkit.Sound

class Pachinko {
    fun hit(pachinkoPlayer: PachinkoPlayer) {
        ItemManager().givePachinkoBall(pachinkoPlayer.player)
        pachinkoPlayer.blinkingDisplay("当たり！！", Sound.ENTITY_EXPERIENCE_ORB_PICKUP)
    }
}
