package com.github.AoRingoServer.Commands

import com.github.AoRingoServer.PachinkoManager
import com.github.AoRingoServer.Yml
import com.github.AoRingoServer.common.Pachinko
import com.github.AoRingoServer.common.PachinkoItem
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
        val subCommand = args[0]
        val subCommandMap = subCommandMap(sender)
        subCommandMap[subCommand]?.invoke(args)
        return true
    }
    private fun subCommandMap(sender: Player): Map<String, (Array<out String>) -> Unit> {
        val pachinkoManager = PachinkoManager(plugin)
        val maxDistance = 10
        val block = sender.getTargetBlock(null, maxDistance)
        val subCommandMap = mapOf(
            "give" to { PachinkoItem().givePachinkoPickel(sender) },
            "reloadconfig" to {
                Yml(plugin).loadYml()
                sender.sendMessage("${ChatColor.YELLOW}[パチンコ] configを再読込しました")
            },
            "setPachinkoType" to { args: Array<out String> ->
                if (args.size == 2) {
                    val entry = args[1]
                    if (block.type == Pachinko().breakBlockType) {
                        pachinkoManager.addPachinkoType(block, entry)
                        sender.sendMessage("${ChatColor.GREEN}パチンコの種類設定をしました")
                    }
                }
            },
            "acquisitionPachinkoType" to {
                val pachinkoType = pachinkoManager.acquisitionPachinkoType(block)
                sender.sendMessage("${ChatColor.YELLOW}[パチンコ種類] $pachinkoType")
            }
        )
        return subCommandMap
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        if (sender !is Player) { return mutableListOf() }
        return when (args.size) {
            1 -> subCommandMap(sender).keys.toMutableList()
            2 -> Pachinko().pachinkoMachine.keys.toMutableList()
            else -> mutableListOf()
        }
    }
}
