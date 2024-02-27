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
        val buttonPushSubMessage = "${ChatColor.RED}ラッシュを獲得しろ！！"
        val config = PluginData.DataManager.config
        val fastProbability = config?.get("monitored.fastProbability").toString().toInt()
        when (stagingBlock.type) {
            Material.WHITE_WOOL -> fastDrawing(pachinkoPlayer, fastProbability, block, stagingBlock, probabilityBlock, buttonPushMessage, buttonPushSubMessage, staging)
            probabilityBlock -> resetWool(stagingBlock)
        }
    }

    override fun acquisitionUseBallCount(): Int {
        return 1
    }
    private fun fastDrawing(pachinkoPlayer: PachinkoPlayer, fastProbability: Int, block: Block, stagingBlock: Block, probabilityBlock: Material, pushMessage: String, subTitle: String, staging: Staging) {
        val fastDrawing = Random.nextInt(0, fastProbability) == 0
        if (!fastDrawing) { return }
        stagingBlock.type = probabilityBlock
        staging.blinkingDisplay(pachinkoPlayer, pushMessage, Sound.BLOCK_BELL_USE, block, subTitle)
    }
    private fun colorDrawing(block: Block) {
        val redProbability = PluginData.DataManager.config?.get("monitored.red").toString().toInt()
        val redDrawing = Random.nextInt(0, redProbability) == 0
        val greenProbability = PluginData.DataManager.config?.get("monitored.green").toString().toInt()
        val greenDrawing = Random.nextInt(0, greenProbability) == 0
        block.type = when {
            redDrawing -> Material.RED_WOOL
            greenDrawing -> Material.GREEN_WOOL
            else -> Material.BLUE_WOOL
        }
    }
    private fun resetWool(displayBlock: Block) {
        displayBlock.type = Material.WHITE_WOOL
    }

    override fun pushingButton(button: Block, connectionBlock: Block, stagingBlock: Block, pachinkoPlayer: PachinkoPlayer, staging: Staging) {
        if (stagingBlock.type != probabilityBlock) { return }
        val player = pachinkoPlayer.player
        val drawing = Random.nextInt(0, 2) == 0
        val message = "${ChatColor.YELLOW}ラッシュ獲得"
        val failureMessage = "${ChatColor.RED}獲得失敗"
        if (!drawing) {
            player.playSound(player, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f)
            resetWool(stagingBlock)
            player.sendTitle(failureMessage, "")
            return
        }
        staging.blinkingDisplay(pachinkoPlayer, message, Sound.ITEM_TOTEM_USE, connectionBlock)
        colorDrawing(stagingBlock)
    }
}
