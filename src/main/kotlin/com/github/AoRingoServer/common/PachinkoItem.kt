package com.github.AoRingoServer.common

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PachinkoItem {
    fun pachinkoBall(): ItemStack {
        val item = ItemStack(Material.IRON_NUGGET)
        val meta = item.itemMeta
        meta?.setDisplayName("${ChatColor.YELLOW}パチンコ玉")
        meta?.setCustomModelData(1)
        item.setItemMeta(meta)
        return item
    }
    fun givePachinkoPickel(player: Player) {
        player.inventory.addItem(PachinkoItem().pachinkoBall())
        val command = "{display:{Name:\"{\\\"text\\\":\\\"採掘パチンコ\\\",\\\"color\\\":\\\"gold\\\"}\",Lore:[\"採掘パチンコ用で使うピッケル\"]},CanDestroy:[\"minecraft:emerald_ore\"]}"
        Bukkit.dispatchCommand(player, "give @s iron_pickaxe$command")
    }
}
