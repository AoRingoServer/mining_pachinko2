package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.PluginData
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import kotlin.random.Random

class MonitoredPachinko : PachinkoMachines {
    override fun shoot(block: Block,stagingBlock:Block, pachinkoPlayer: PachinkoPlayer) {
        val probabilityBlock = Material.SEA_LANTERN
        val buttonPushMessage = "${ChatColor.YELLOW}ボタンを押せ！！！"
        val config = PluginData.DataManager.config
        val fastProbability = config?.get("monitored.fastProbability").toString().toInt()
        when (stagingBlock.type) {
            Material.WHITE_WOOL -> fastDrawing(pachinkoPlayer, fastProbability, stagingBlock, probabilityBlock, buttonPushMessage)
            probabilityBlock -> resetWool(stagingBlock)
        }
    }

    override fun acquisitionUseBallCount(): Int {
        return 1
    }
    private fun fastDrawing(pachinkoPlayer: PachinkoPlayer, fastProbability: Int, block: Block, probabilityBlock: Material, pushMessage: String) {
        val fastDrawing = Random.nextInt(0, fastProbability) == 0
        if (!fastDrawing) { return }
        block.type = probabilityBlock
        pachinkoPlayer.blinkingDisplay(pushMessage, Sound.ENTITY_GENERIC_EXPLODE)
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
}
