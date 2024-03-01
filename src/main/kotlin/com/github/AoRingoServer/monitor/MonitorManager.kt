package com.github.AoRingoServer.monitor

import com.github.AoRingoServer.PachinkoManager
import com.github.Ringoame196.Image
import com.github.Ringoame196.ImageMapRenderer
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.map.MapView
import org.bukkit.plugin.java.JavaPlugin

class MonitorManager() {
    private fun getMonitor(mapNumber: Int): MapView {
        return Bukkit.getMap(mapNumber) ?: Bukkit.createMap(Bukkit.getWorlds()[0])
    }
    private fun resetMpaInfo(mapView: MapView) {
        mapView.renderers.clear()
    }
    fun displayImage(plugin: JavaPlugin, block: Block, imageName: String) {
        val monitorID = PachinkoManager(plugin).acquisitionCoordinationMonitorID(block) ?: return
        val monitor = getMonitor(monitorID)
        val image = Image().make(plugin, imageName)
        resetMpaInfo(monitor)
        val newMapRenderer = ImageMapRenderer(plugin, image)
        monitor.renderers.clear() // Clear existing renderers
        monitor.addRenderer(newMapRenderer)
    }
}
