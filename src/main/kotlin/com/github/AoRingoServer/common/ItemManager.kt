package com.github.AoRingoServer.common

import org.bukkit.entity.Player

class ItemManager {
    private val pachinkoItem = PachinkoItem()
    fun givePachinkoBall(player: Player, amount: Int) {
        val item = pachinkoItem.pachinkoBall()
        item.amount = amount
        player.inventory.addItem(item)
    }
}
