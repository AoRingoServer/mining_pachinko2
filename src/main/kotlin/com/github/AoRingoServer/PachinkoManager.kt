package com.github.AoRingoServer

import com.github.AoRingoServer.common.Pachinko
import com.github.AoRingoServer.common.PachinkoItem
import com.github.AoRingoServer.pachinkoMachine.FalseSimplePachinko
import com.github.AoRingoServer.pachinkoMachine.MonitoredPachinko
import com.github.AoRingoServer.pachinkoMachine.PachinkoMachines
import com.github.AoRingoServer.pachinkoMachine.SimplePachinko
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

class PachinkoManager(private val plugin: JavaPlugin) {
    val breakBlockType = Material.EMERALD_ORE
    private val pachinkoFileName = "pachinkoData"
    val pachinkoCountKey = "pachinkoCountKey"
    private val yml = Yml(plugin)
    private val pachinkoData = yml.getYml(pachinkoFileName)
    private val pachinkoTypeKey = "type"
    private val monitorIDKey = "monitorID"
    val pachinkoMachine = mapOf<String, (JavaPlugin, Pachinko) -> PachinkoMachines>(
        "simple" to { plugin, pachinko -> SimplePachinko(plugin, pachinko) },
        "falseSimple" to { plugin, pachinko -> FalseSimplePachinko(plugin, pachinko) },
        "monitored" to { plugin, pachinko -> MonitoredPachinko(plugin, pachinko) }
    )
    private fun acquisitionBlockLocation(block: Block): String {
        val blockLocation = block.location
        return "${block.world.name}x${blockLocation.x.toInt()}y${blockLocation.y.toInt()}z${blockLocation.z.toInt()}"
    }
    private fun additionalDataToPluginDataFile(key: String, value: String) {
        yml.setYml(plugin, pachinkoFileName, key, value)
    }
    private fun acquisitionDataToPluginDataFile(key: String): String? {
        return pachinkoData.getString(key)
    }
    fun addPachinkoType(block: Block, pachinkoMachine: String) {
        val location = acquisitionBlockLocation(block)
        val key = "$location.$pachinkoTypeKey"
        additionalDataToPluginDataFile(key, pachinkoMachine)
    }
    fun acquisitionPachinkoType(block: Block): String? {
        val location = acquisitionBlockLocation(block)
        val key = "$location.$pachinkoTypeKey"
        return acquisitionDataToPluginDataFile(key)
    }
    fun addMonitorID(block: Block, monitorID: String) {
        val location = acquisitionBlockLocation(block)
        val key = "$location.$monitorIDKey"
        additionalDataToPluginDataFile(key, monitorID)
    }
    fun acquisitionMonitorID(block: Block): String? {
        val location = acquisitionBlockLocation(block)
        val key = "$location.$monitorIDKey"
        return acquisitionDataToPluginDataFile(key)
    }
    fun addContinuousCount(block: Block): Int {
        val count = acquisitionTemporaryIntData(block, pachinkoCountKey) ?: 0
        val newCount = count + 1
        setTemporaryIntData(block, pachinkoCountKey, newCount)
        return newCount
    }
    fun setTemporaryIntData(block: Block, key: String, data: Int) {
        block.setMetadata(key, FixedMetadataValue(plugin, data))
    }
    private fun acquisitionTemporaryIntData(block: Block, key: String): Int? {
        val metadata = block.getMetadata(key)
        if (metadata.isNotEmpty()) {
            return metadata[0].asInt()
        }
        return null
    }
    fun acquisitionCoordinationMonitorID(block: Block): Int? {
        val location = acquisitionBlockLocation(block)
        val key = "$location.$monitorIDKey"
        val monitorID = acquisitionDataToPluginDataFile(key) ?: return null
        return monitorID.toInt()
    }
    fun consumptionPachinkoBall(player: Player, amount: Int): Boolean {
        if (player.gameMode == GameMode.CREATIVE) {
            return true
        }
        val errorMessage = "${ChatColor.RED}パチンコ玉をオフハンドに持ってください\n(統合版の場合 パチンコ玉を持ってしゃがむとオフハンドに入ります)"
        val item = PachinkoItem().pachinkoBall()
        item.amount = 1
        val offHandItem = player.inventory.itemInOffHand
        val playerHaveItem = offHandItem.clone()
        playerHaveItem.amount = 1
        if (playerHaveItem != item) {
            player.sendMessage(errorMessage)
            return false
        }
        if (playerHaveItem.amount < amount) {
            player.sendMessage(errorMessage)
            return false
        }
        offHandItem.amount = offHandItem.amount - amount
        return true
    }
    fun checkaPachinkoPickel(pickel: ItemStack): Boolean {
        val pickelName = "${ChatColor.GOLD}採掘パチンコ"
        val picelType = Material.IRON_PICKAXE
        return pickel.type == picelType && pickel.itemMeta?.displayName == pickelName
    }
    fun fastStaging(pachinko: Pachinko) {
        if (!drawingFastStaging()) { return }
        val message = "${ChatColor.AQUA}今のうちに掘れ！！"
        val subTitle = "${ChatColor.YELLOW}チャンス"
        val sound = Sound.ENTITY_PLAYER_LEVELUP
        val durationSeconds = 10
        val miningSpeedLevel = 5
        val player = pachinko.pachinkoPlayer.player
        pachinko.blinkingDisplay(message, sound, subMessage = subTitle)
        player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, durationSeconds * 20, miningSpeedLevel - 1))
    }
    private fun drawingFastStaging(): Boolean {
        val config = PluginData.DataManager.config
        val probability = config?.getInt("common,fastStagingProbability") ?: return false
        return Random.nextInt(0, probability) == 0
    }
}
