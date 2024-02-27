package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.PluginData
import com.github.AoRingoServer.Staging
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import kotlin.random.Random

class MonitoredPachinko : PachinkoMachines, PachinkoWithButtons {
    private val probabilityBlock = Material.SEA_LANTERN
    override fun shoot(block: Block, stagingBlock: Block, pachinkoPlayer: PachinkoPlayer, staging: Staging) {
        val buttonPushMessage = "${ChatColor.YELLOW}ボタンを押せ！！！"
        val config = PluginData.DataManager.config
        val fastProbability = config?.get("monitored.fastProbability").toString().toInt()
        when (stagingBlock.type) {
            Material.WHITE_WOOL -> fastDrawing(pachinkoPlayer, fastProbability, block, stagingBlock, probabilityBlock, buttonPushMessage, staging)
            probabilityBlock -> resetWool(stagingBlock)
        }
    }

    override fun acquisitionUseBallCount(): Int {
        return 1
    }
    private fun fastDrawing(pachinkoPlayer: PachinkoPlayer, fastProbability: Int, block: Block, stagingBlock: Block, probabilityBlock: Material, pushMessage: String, staging: Staging) {
        val fastDrawing = Random.nextInt(0, fastProbability) == 0
        if (!fastDrawing) { return }
        stagingBlock.type = probabilityBlock
        staging.blinkingDisplay(pachinkoPlayer, pushMessage, Sound.BLOCK_BELL_USE, block)
    }
    private fun colorDrawing(block: Block) {
        val redProbability = PluginData.DataManager.config?.get("monitored.red").toString().toInt()
        val blueProbability = PluginData.DataManager.config?.get("monitored.blue").toString().toInt()
        val redDrawing = Random.nextInt(0, redProbability) == 0
        val blueDrawing = Random.nextInt(0, blueProbability) == 0
        block.type = when {
            redDrawing -> Material.RED_WOOL
            blueDrawing -> Material.BLUE_WOOL
            else -> Material.GREEN_WOOL
        }
    }
    private fun resetWool(displayBlock: Block) {
        displayBlock.type = Material.WHITE_WOOL
    }

    override fun pushingButton(button: Block, connectionBlock: Block, stagingBlock: Block, pachinkoPlayer: PachinkoPlayer, staging: Staging) {
        if (stagingBlock.type != probabilityBlock) { return }
        val player = pachinkoPlayer.player
        val drawing = Random.nextInt(0, 2) == 0
        val message = "${ChatColor.YELLOW}継続"
        if (!drawing) {
            player.playSound(player, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f)
            resetWool(stagingBlock)
            return
        }
        staging.blinkingDisplay(pachinkoPlayer, message, Sound.ITEM_TOTEM_USE, connectionBlock)
        colorDrawing(stagingBlock)
    }
}
