package com.github.AoRingoServer

import com.github.AoRingoServer.pachinkoMachine.FalseSimplePachinko
import com.github.AoRingoServer.pachinkoMachine.PachinkoMachines
import com.github.AoRingoServer.pachinkoMachine.SimplePachinko
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.CommandBlock
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.Plugin

class Events(val plugin: Plugin) : Listener {
    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val player = e.player
        val pachinkoPlayer = PachinkoPlayer(player, plugin)
        val block = e.block
        val pickel = player.inventory.itemInMainHand
        val pickelName = "${ChatColor.GOLD}採掘パチンコ"
        val judgementBlock = block.location.clone().add(0.0, -2.0, 0.0).block
        val patinkoMachine = mapOf<String, PachinkoMachines>(
            "simple" to SimplePachinko(),
            "falseSimple" to FalseSimplePachinko()
        )
        if (pickel.type != Material.IRON_PICKAXE || pickel.itemMeta?.displayName != pickelName) {
            return
        }
        e.isCancelled = true
        if (block.type != Material.EMERALD_ORE || judgementBlock.type != Material.COMMAND_BLOCK) {
            return
        }
        val commandBlock = judgementBlock.state as CommandBlock
        val judgementMessage = commandBlock.command
        e.isCancelled = true
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
        patinkoMachine[judgementMessage]?.shoot(block, pachinkoPlayer) ?: return
    }
    @EventHandler
    fun onPlayerToggleSneak(e: PlayerToggleSneakEvent) {
        val player = e.player
        val sneaking = e.isSneaking
        val item = player.inventory.itemInMainHand.clone()
        val offhandItem = player.inventory.itemInOffHand.clone()
        if (!sneaking) { return }
        if (item.itemMeta?.displayName == com.github.AoRingoServer.common.PachinkoItem().pachinkoBall().itemMeta?.displayName) {
            player.inventory.setItemInMainHand(offhandItem)
            player.inventory.setItemInOffHand(item)
        }
    }
}
