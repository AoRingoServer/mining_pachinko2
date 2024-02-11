package com.github.AoRingoServer.Commands

import com.github.AoRingoServer.PluginData
import com.github.AoRingoServer.Yml
import com.github.AoRingoServer.common.PachinkoItem
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class PachinkoCommand(val plugin: Plugin) : CommandExecutor, TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) { return true }
        when (args[0]) {
            "give" -> {
                sender.inventory.addItem(PachinkoItem().pachinkoBall())
                val command = "{display:{Name:\"{\\\"text\\\":\\\"採掘パチンコ\\\",\\\"color\\\":\\\"gold\\\"}\",Lore:[\"採掘パチンコ用で使うピッケル\"]},CanDestroy:[\"minecraft:emerald_ore\"]}"
                Bukkit.dispatchCommand(sender, "give @s iron_pickaxe$command")
            }
            "reloadconfig" -> {
                PluginData.DataManager.config = Yml(plugin).loadConfig()
                sender.sendMessage("${ChatColor.YELLOW}[パチンコ] configを再読込しました")
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        return when (args.size) {
            1 -> mutableListOf("give", "reloadconfig")
            else -> mutableListOf()
        }
    }
}
