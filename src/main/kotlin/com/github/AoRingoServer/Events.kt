package com.github.AoRingoServer

import com.github.AoRingoServer.common.Pachinko
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.Plugin

class Events(private val plugin: Plugin) : Listener {
    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val player = e.player
        val pickel = player.inventory.itemInMainHand
        val pachinkoPlayer = PachinkoPlayer(player, plugin)
        val pachinko = Pachinko()
        val block = e.block
        val stagingBlock = block.location.clone().add(0.0, -1.0, 0.0).block
        val pachinkoMachine = pachinko.pachinkoMachine
        val pachinkoManager = PachinkoManager(plugin)
        val pachinkoType = pachinkoManager.acquisitionPachinkoType(block)
        val staging = Staging(plugin)
        if (!pachinko.checkaPachinkoPickel(pickel)) {
            return
        }
        e.isCancelled = true
        if (block.type != pachinko.breakBlockType) {
            return
        }
        e.isCancelled = true
        if (pachinkoType == null) {
            player.sendMessage("${ChatColor.RED}このパチンコ台はまだ未設定です")
            return
        }
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
        val usePachinkoBallCount = pachinkoMachine[pachinkoType]?.acquisitionUseBallCount()
        if (!pachinko.consumptionPachinkoBall(player, usePachinkoBallCount?:return)) {
            return
        }
        pachinkoMachine[pachinkoType]?.shoot(block, stagingBlock, pachinkoPlayer, staging) ?: return
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
    @EventHandler
    fun onHangingBreakByEntity(e: HangingBreakByEntityEvent) {
        val entity = e.entity
        val monitorTag = "monitor"
        if (entity.type != EntityType.PAINTING) { return }
        if (!entity.scoreboardTags.contains(monitorTag)) { return }
        e.isCancelled = true
    }
}
