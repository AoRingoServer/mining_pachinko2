package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoManager
import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.PluginData
import com.github.AoRingoServer.common.Pachinko
import com.github.AoRingoServer.monitor.MonitorManager
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class MonitoredPachinko(private val plugin: JavaPlugin, private val pachinko: Pachinko) : PachinkoMachines, PachinkoWithButtons {
    private val probabilityBlock = Material.SEA_LANTERN
    private val monitorManager = MonitorManager()
    override fun shoot() {
        val config = PluginData.DataManager.config
        val fastProbability = config?.get("monitored.fastProbability").toString().toInt()
        when (pachinko.stagingBlock?.type) {
            Material.WHITE_WOOL -> fastDrawing(pachinko.pachinkoPlayer, fastProbability, pachinko.breakBlock ?: return, pachinko.stagingBlock, probabilityBlock)
            probabilityBlock -> resetWool(pachinko.stagingBlock)
            Material.GREEN_WOOL, Material.BLUE_WOOL, Material.RED_WOOL -> continuation(pachinko.stagingBlock, pachinko.pachinkoPlayer)
        }
    }

    override fun acquisitionUseBallCount(): Int {
        return 1
    }
    private fun fastDrawing(
        pachinkoPlayer: PachinkoPlayer,
        fastProbability: Int,
        block: Block,
        stagingBlock: Block,
        probabilityBlock: Material
    ) {
        val fastDrawing = Random.nextInt(0, fastProbability) == 0
        val buttonPushMessage = "${ChatColor.YELLOW}ボタンを押せ！！！"
        val buttonPushSubMessage = "${ChatColor.RED}ラッシュを獲得しろ！！"
        if (!fastDrawing) { return }
        stagingBlock.type = probabilityBlock
        pachinko.blinkingDisplay(buttonPushMessage, Sound.BLOCK_BELL_USE, buttonPushSubMessage)
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

    override fun pushingButton(button: Block) {
        if (pachinko.stagingBlock.type != probabilityBlock) { return }
        val player = pachinko.pachinkoPlayer.player
        val drawing = Random.nextInt(0, 2) == 0
        val message = "${ChatColor.YELLOW}ラッシュ獲得"
        val failureMessage = "${ChatColor.RED}獲得失敗"
        if (!drawing) {
            player.playSound(player, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f)
            resetWool(pachinko.stagingBlock)
            player.sendTitle(failureMessage, "")
            return
        }
        pachinko.blinkingDisplay(message, Sound.ITEM_TOTEM_USE)
        colorDrawing(pachinko.stagingBlock)
    }
    private fun continuousDrawing(block: Block): Boolean {
        val config = PluginData.DataManager.config ?: return false
        val continuousRateKeys = mapOf(
            Material.RED_WOOL to "monitored.redContinuousRate",
            Material.BLUE_WOOL to "monitored.blueContinuousRate",
            Material.GREEN_WOOL to "monitored.greenContinuousRate"
        )
        val continuousRateKey = continuousRateKeys[block.type] ?: return false
        val probability = config.getString(continuousRateKey)?.toInt() ?: 0
        val hundredDrawing = Random.nextInt(1, 100)
        return hundredDrawing <= probability
    }
    private fun continuation(block: Block, pachinkoPlayer: PachinkoPlayer) {
        val pachinkoManager = PachinkoManager(plugin, pachinko)
        val max = 3
        val player = pachinkoPlayer.player
        val pachinkoCountKey = pachinkoManager.pachinkoCountKey
        val count = pachinkoManager.addContinuousCount(block) - 1
        val remainingCount = max - count
        player.sendTitle("${ChatColor.AQUA}$remainingCount", "")
        val breakBlock = block.location.clone().add(0.0, 1.0, 0.0).block
        imageDisplay(remainingCount, breakBlock)
        if (count == max) {
            pachinkoManager.setTemporaryIntData(block, pachinkoCountKey, 0)
            resetWool(block)
            return
        }
        if (continuousDrawing(block)) {
            reContinuation(pachinkoManager, pachinko)
        }
    }
    private fun imageDisplay(count: Int, block: Block) {
        when (count) {
            1 -> monitorManager.displayImage(plugin, block, "one.png", pachinko)
            2 -> monitorManager.displayImage(plugin, block, "two.png", pachinko)
            3 -> monitorManager.displayImage(plugin, block, "three.png", pachinko)
            else -> monitorManager.displayImage(plugin, block, "aoringoServer.png", pachinko)
        }
    }
    private fun reContinuation(pachinkoManager: PachinkoManager, pachinko: Pachinko) {
        val message = "${ChatColor.YELLOW}継続"
        val pachinkoCountKey = pachinkoManager.pachinkoCountKey
        pachinkoManager.setTemporaryIntData(pachinko.breakBlock, pachinkoCountKey, 0)
        pachinko.blinkingDisplay(message, Sound.BLOCK_BELL_USE)
    }
}
