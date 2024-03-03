package com.github.AoRingoServer

import com.github.AoRingoServer.common.Pachinko
import com.github.AoRingoServer.pachinkoMachine.MonitoredPachinko
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.java.JavaPlugin

class Events(private val plugin: JavaPlugin) : Listener {
    private fun checkLoad(player: Player): Boolean {
        return true
    }
    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val player = e.player
        if (!checkLoad(player)) { return }
        val pickel = player.inventory.itemInMainHand
        val pachinkoPlayer = PachinkoPlayer(player, plugin)
        val block = e.block
        val stagingBlock = block.location.clone().add(0.0, -1.0, 0.0).block
        val pachinko = Pachinko(plugin, block, stagingBlock, pachinkoPlayer)
        val pachinkoManager = PachinkoManager(plugin, pachinko)
        val pachinkoMachine = pachinkoManager.pachinkoMachine
        val pachinkoType = pachinkoManager.acquisitionPachinkoType(block)
        if (!pachinkoManager.checkaPachinkoPickel(pickel)) {
            return
        }
        e.isCancelled = true
        if (block.type != pachinkoManager.breakBlockType) {
            return
        }
        e.isCancelled = true
        if (pachinkoType == null) {
            player.sendMessage("${ChatColor.RED}このパチンコ台はまだ未設定です")
            return
        }
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
        val usePachinkoBallCount = pachinkoMachine[pachinkoType]?.acquisitionUseBallCount()
        if (!pachinkoManager.consumptionPachinkoBall(player, usePachinkoBallCount?:return)) {
            return
        }
        pachinkoMachine[pachinkoType]?.shoot() ?: return
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
    fun onButtonPush(e: PlayerInteractEvent) {
        val player = e.player
        if (!checkLoad(player)) { return }
        val pachinkoPlayer = PachinkoPlayer(player, plugin)
        val button = e.clickedBlock ?: return
        val pachinkoButton = PachinkoButton()
        val buttonType = Material.OAK_BUTTON
        val action = e.action
        if (action != Action.RIGHT_CLICK_BLOCK) { return }
        if (button.type != buttonType) { return }
        val connectionBlock = pachinkoButton.acquisitionConnectionBlock(button) ?: return
        val stagingBlock = connectionBlock.location.clone().add(0.0, -1.0, 0.0).block
        val pachinko = Pachinko(plugin, connectionBlock, stagingBlock, pachinkoPlayer)
        val pachinkoManager = PachinkoManager(plugin, pachinko)
        val pachinkoType = pachinkoManager.acquisitionPachinkoType(connectionBlock)
        val pachinkoMap = mapOf(
            "monitored" to MonitoredPachinko(plugin, pachinko)
        )
        if (connectionBlock.type != pachinkoManager.breakBlockType) { return }
        if (!pachinkoMap.keys.contains(pachinkoType)) { return }
        pachinkoMap[pachinkoType]?.pushingButton(button)
    }
}
