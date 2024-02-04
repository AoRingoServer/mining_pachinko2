package com.github.AoRingoServer.common

import org.bukkit.ChatColor
import org.bukkit.Material
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
}
