package com.github.AoRingoServer.common

import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ItemManager {
    private val pachinkoItem = PachinkoItem()
    fun givePachinkoBall(player: Player) {
        val item = pachinkoItem.pachinkoBall()
        player.inventory.addItem(item)
    }
    fun reducePachinkoBall(player: Player, amount: Int) {
        val errorMessage = "${ChatColor.RED}パチンコ玉をオフハンドに持ってください\n(統合版の場合 パチンコ玉を持ってしゃがむとオフハンドに入ります)"
        val item = pachinkoItem.pachinkoBall()
        item.amount = 1
        val offHandItem = player.inventory.itemInOffHand
        val playerHaveItem = offHandItem.clone()
        playerHaveItem.amount = 1
        if (playerHaveItem != item) {
            player.sendMessage(errorMessage)
            return
        }
        if (playerHaveItem.amount < amount) {
            player.sendMessage(errorMessage)
            return
        }
        offHandItem.amount = offHandItem.amount - amount
    }
}
