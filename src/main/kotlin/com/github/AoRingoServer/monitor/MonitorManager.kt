package com.github.AoRingoServer.monitor

import com.github.AoRingoServer.PachinkoManager
import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.common.Pachinko
import com.github.Ringoame196.Image
import com.github.Ringoame196.ImageMapRenderer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapView
import org.bukkit.plugin.java.JavaPlugin

class MonitorManager(val plugin: JavaPlugin) {
    private val monitorListKey = "monitorList"
    private val monitorDataFile = "monitorData.yml"
    private fun getMonitor(mapNumber: Int): MapView {
        return Bukkit.getMap(mapNumber) ?: Bukkit.createMap(Bukkit.getWorlds()[0])
    }
    private fun resetMpaInfo(mapView: MapView) {
        mapView.renderers.clear()
    }
    fun displayImage(imageName: String, pachinko: Pachinko) {
        val breakBlock = pachinko.breakBlock
        val monitorID = PachinkoManager(plugin).acquisitionCoordinationMonitorID(breakBlock) ?: return
        val monitor = getMonitor(monitorID)
        val image = Image().make(plugin, imageName)
        resetMpaInfo(monitor)
        realTimeUpdate(pachinko.pachinkoPlayer, monitorID)
        val newMapRenderer = ImageMapRenderer(plugin, image)
        monitor.renderers.clear()
        monitor.addRenderer(newMapRenderer)
    }
    private fun realTimeUpdate(pachinkoPlayer: PachinkoPlayer, mapID: Int) {
        val player = pachinkoPlayer.player
        val inventoryNumber = 35
        val time: Long = 5
        val playerItem = player.inventory.getItem(inventoryNumber)
        val mapItem = makeMapItem(mapID)
        player.inventory.setItem(inventoryNumber, mapItem)
        if (playerItem == mapItem) { return }
        Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable {
                if (checkPlayerItem(playerItem, mapItem)) {
                    player.inventory.setItem(inventoryNumber, ItemStack(Material.AIR))
                } else {
                    player.inventory.setItem(inventoryNumber, playerItem)
                }
            },
            time
        ) // 20Lは1秒を表す（1秒 = 20ticks）
    }
    private fun checkPlayerItem(playerItem: ItemStack?, mapItem: ItemStack): Boolean {
        val playerItemName = playerItem?.itemMeta?.displayName ?: ""
        val mapItemName = mapItem.itemMeta?.displayName
        if (playerItem?.type != Material.FILLED_MAP) { return false }
        return playerItemName == mapItemName
    }
    fun makeMapItem(mapID: Int): ItemStack {
        val map = ItemStack(Material.FILLED_MAP)
        val meta = map.itemMeta as MapMeta
        meta.setDisplayName("${ChatColor.YELLOW}モニター")
        meta.lore = mutableListOf("${ChatColor.RED}※アイテム移動によるアイテム保証諸々はありません")
        meta.mapId = mapID
        map.setItemMeta(meta)
        return map
    }
}
