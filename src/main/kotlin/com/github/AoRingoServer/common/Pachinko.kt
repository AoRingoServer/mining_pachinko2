package com.github.AoRingoServer.common

import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.Staging
import com.github.AoRingoServer.pachinkoMachine.FalseSimplePachinko
import com.github.AoRingoServer.pachinkoMachine.MonitoredPachinko
import com.github.AoRingoServer.pachinkoMachine.PachinkoMachines
import com.github.AoRingoServer.pachinkoMachine.SimplePachinko
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Pachinko {
    val breakBlockType = Material.EMERALD_ORE
    val pachinkoMachine = mapOf<String, PachinkoMachines>(
        "simple" to SimplePachinko(),
        "falseSimple" to FalseSimplePachinko(),
        "monitored" to MonitoredPachinko()
    )
    fun hit(pachinkoPlayer: PachinkoPlayer, block: Block, amount: Int, staging: Staging) {
        val player = pachinkoPlayer.player
        ItemManager().givePachinkoBall(pachinkoPlayer.player, amount)
        player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
        staging.blinkingDisplay(pachinkoPlayer, "当たり！！", Sound.ENTITY_EXPERIENCE_ORB_PICKUP, block)
    }
    fun consumptionPachinkoBall(player: Player, amount: Int): Boolean {
        if (player.gameMode == GameMode.CREATIVE) {
            return true
        }
        val errorMessage = "${ChatColor.RED}パチンコ玉をオフハンドに持ってください\n(統合版の場合 パチンコ玉を持ってしゃがむとオフハンドに入ります)"
        val item = PachinkoItem().pachinkoBall()
        item.amount = 1
        val offHandItem = player.inventory.itemInOffHand
        val playerHaveItem = offHandItem.clone()
        playerHaveItem.amount = 1
        if (playerHaveItem != item) {
            player.sendMessage(errorMessage)
            return false
        }
        if (playerHaveItem.amount < amount) {
            player.sendMessage(errorMessage)
            return false
        }
        offHandItem.amount = offHandItem.amount - amount
        return true
    }
    fun checkaPachinkoPickel(pickel: ItemStack): Boolean {
        val pickelName = "${ChatColor.GOLD}採掘パチンコ"
        val picelType = Material.IRON_PICKAXE
        return pickel.type == picelType && pickel.itemMeta?.displayName == pickelName
    }
}
