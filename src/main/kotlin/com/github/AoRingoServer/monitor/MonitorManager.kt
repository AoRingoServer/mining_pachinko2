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
    private fun getMonitor(mapNumber: Int): MapView {
        return Bukkit.getMap(mapNumber) ?: Bukkit.createMap(Bukkit.getWorlds()[0])
    }
    private fun resetMpaInfo(mapView: MapView) {
        mapView.renderers.clear()
    }
    fun displayImage(imageName: String, pachinko: Pachinko) {
        val breakBlock = pachinko.breakBlock
        val monitorID = PachinkoManager(plugin, pachinko).acquisitionCoordinationMonitorID(breakBlock) ?: return
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
        val offhandItem = player.inventory.itemInOffHand.clone()
        val mapItem = makeMapItem(mapID)
        player.inventory.setItemInOffHand(mapItem)
        if (offhandItem == mapItem) { return }
        Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable {
                player.inventory.setItemInOffHand(offhandItem)
                player.inventory.removeItem(mapItem)
            },
            10
        ) // 20Lは1秒を表す（1秒 = 20ticks）
    }
    private fun makeMapItem(mapID: Int): ItemStack {
        val map = ItemStack(Material.FILLED_MAP)
        val meta = map.itemMeta as MapMeta
        meta.setDisplayName("${ChatColor.RED}移動不可")
        meta.mapId = mapID
        map.setItemMeta(meta)
        return map
    }
}
