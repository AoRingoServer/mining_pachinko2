package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoManager
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
    private val monitorManager = MonitorManager(plugin)
    override fun shoot() {
        val config = PluginData.DataManager.config
        val fastProbability = config?.get("monitored.fastProbability").toString().toInt()
        when (pachinko.stagingBlock.type) {
            Material.WHITE_WOOL -> fastDrawing(fastProbability, pachinko, probabilityBlock)
            probabilityBlock -> resetWoolAndMonitor(pachinko)
            Material.GREEN_WOOL, Material.BLUE_WOOL, Material.RED_WOOL -> continuation()
            else -> return
        }
    }

    override fun acquisitionUseBallCount(): Int {
        return 1
    }
    private fun fastDrawing(
        fastProbability: Int,
        pachinko: Pachinko,
        probabilityBlock: Material
    ) {
        val stagingBlock = pachinko.stagingBlock
        val fastDrawing = Random.nextInt(0, fastProbability) == 0
        val buttonPushMessage = "${ChatColor.YELLOW}ボタンを押せ！！！"
        val buttonPushSubMessage = "${ChatColor.RED}ラッシュを獲得しろ！！"
        if (!fastDrawing) { return }
        stagingBlock.type = probabilityBlock
        pachinko.blinkingDisplay(buttonPushMessage, Sound.BLOCK_BELL_USE, buttonPushSubMessage)
        imageDisplay("push.png", pachinko)
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
    private fun resetWoolAndMonitor(pachinko: Pachinko) {
        val stringBlock = pachinko.stagingBlock
        imageDisplay("black.png", pachinko)
        stringBlock.type = Material.WHITE_WOOL
    }

    override fun pushingButton(button: Block) {
        if (pachinko.stagingBlock.type != probabilityBlock) { return }
        val player = pachinko.pachinkoPlayer.player
        val drawing = Random.nextInt(0, 2) == 0
        val message = "${ChatColor.YELLOW}ラッシュ獲得"
        val failureMessage = "${ChatColor.RED}獲得失敗"
        if (!drawing) {
            player.playSound(player, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f)
            resetWoolAndMonitor(pachinko)
            player.sendTitle(failureMessage, "")
            return
        }
        hitImageDisplay()
        pachinko.blinkingDisplay(message, Sound.ITEM_TOTEM_USE, imageName = "black.png")
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
        val hundredDrawing = Random.nextInt(1, 10)
        return hundredDrawing <= probability
    }
    private fun continuation() {
        val pachinkoManager = PachinkoManager(plugin)
        val max = 3
        val player = pachinko.pachinkoPlayer.player
        val staginBlock = pachinko.stagingBlock
        val breakBlock = pachinko.breakBlock
        val count = pachinkoManager.addContinuousCount(staginBlock) - 1
        val remainingCount = max - count
        player.sendTitle("${ChatColor.AQUA}$remainingCount", "")
        continuationImageDisplay(remainingCount, breakBlock)
        if (count == max) {
            if (resurrectionDrawing()) {
                reContinuation(pachinkoManager, pachinko)
            } else {
                endRush(pachinkoManager)
            }
            return
        }
        if (continuousDrawing(staginBlock)) {
            reContinuation(pachinkoManager, pachinko)
        }
    }
    private fun endRush(pachinkoManager: PachinkoManager) {
        val staginBlock = pachinko.stagingBlock
        val pachinkoCountKey = pachinkoManager.pachinkoCountKey
        pachinkoManager.setTemporaryIntData(staginBlock, pachinkoCountKey, 0)
        resetWoolAndMonitor(pachinko)
        imageDisplay("rushend.png", pachinko)
        pachinko.blinkingDisplay("${ChatColor.RED}ラッシュ終了", Sound.BLOCK_FIRE_EXTINGUISH, imageName = "black.png")
    }
    private fun resurrectionDrawing(): Boolean {
        return Random.nextInt(1, 100) == 1
    }
    private fun continuationImageDisplay(count: Int, block: Block) {
        val imageName = when (count) {
            1 -> "one.png"
            2 -> "two.png"
            3 -> "three.png"
            else -> null
        }
        imageName ?: return
        imageDisplay(imageName, pachinko)
    }
    private fun imageDisplay(imageName: String, pachinko: Pachinko) {
        monitorManager.displayImage(imageName, pachinko)
    }
    private fun reContinuation(pachinkoManager: PachinkoManager, pachinko: Pachinko) {
        val message = "${ChatColor.YELLOW}継続"
        val pachinkoCountKey = pachinkoManager.pachinkoCountKey
        pachinkoManager.setTemporaryIntData(pachinko.stagingBlock, pachinkoCountKey, 0)
        pachinko.blinkingDisplay(message, Sound.BLOCK_BELL_USE, imageName = "black.png")
        hitImageDisplay()
    }
    private fun hitImageDisplay() {
        val imageName = "hit.png"
        imageDisplay(imageName, pachinko)
    }
}
