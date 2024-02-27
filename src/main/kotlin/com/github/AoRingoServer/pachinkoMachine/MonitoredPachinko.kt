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
        val config = PluginData.DataManager.config
        val fastProbability = config?.get("monitored.fastProbability").toString().toInt()
        when (stagingBlock.type) {
            Material.WHITE_WOOL -> fastDrawing(pachinkoPlayer, fastProbability, block, stagingBlock, probabilityBlock, staging)
            probabilityBlock -> resetWool(stagingBlock)
        }
    }

    override fun acquisitionUseBallCount(): Int {
        return 1
    }
    private fun fastDrawing(pachinkoPlayer: PachinkoPlayer, fastProbability: Int, block: Block, stagingBlock: Block, probabilityBlock: Material, staging: Staging) {
        val fastDrawing = Random.nextInt(0, fastProbability) == 0
        val buttonPushMessage = "${ChatColor.YELLOW}ボタンを押せ！！！"
        val buttonPushSubMessage = "${ChatColor.RED}ラッシュを獲得しろ！！"
        if (!fastDrawing) { return }
        stagingBlock.type = probabilityBlock
        staging.blinkingDisplay(pachinkoPlayer, buttonPushMessage, Sound.BLOCK_BELL_USE, block, buttonPushSubMessage)
    }
    private fun colorDrawing(block: Block) {
        val hundredDrawing = Random.nextInt(1, 100)
        val redProbability = PluginData.DataManager.config?.get("monitored.red").toString().toInt()
        val redDrawing = hundredDrawing <= redProbability
        val greenProbability = PluginData.DataManager.config?.get("monitored.green").toString().toInt()
        val greenAddingRed = greenProbability + redProbability
        val greenDrawing = hundredDrawing <= greenAddingRed
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
    fun continuousDrawing(block: Block):Boolean{
        val config = PluginData.DataManager.config ?:return false
        val continuousRateKeys = mapOf(
            Material.RED_WOOL to "redContinuousRate",
            Material.BLUE_WOOL to "blueContinuousRate",
            Material.GREEN_WOOL to "greenContinuousRate"
        )
        val  continuousRateKey = continuousRateKeys[block.type]?:return false
        val probability = config.getString(continuousRateKey)?.toInt() ?:0
        val hundredDrawing = Random.nextInt(1, 100)
        return hundredDrawing <= probability
    }
}
