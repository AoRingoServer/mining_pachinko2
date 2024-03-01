package com.github.Ringoame196

import org.bukkit.entity.Player
import org.bukkit.map.MapCanvas
import org.bukkit.map.MapRenderer
import org.bukkit.map.MapView
import org.bukkit.plugin.java.JavaPlugin
import java.awt.image.BufferedImage

class ImageMapRenderer(private val plugin: JavaPlugin, private val imageFile: BufferedImage?) : MapRenderer() {
    override fun render(map: MapView, canvas: MapCanvas, player: Player) {
        // Check if the imageFile is not null
        if (imageFile != null) {
            // Load the image directly as BufferedImage
            val image: BufferedImage = imageFile

            // Resize the image to fit the map
            val resizedImage = resizeImage(image, 128, 128)

            // Draw the image on the map canvas
            canvas.drawImage(0, 0, resizedImage)
        }
    }

    private fun resizeImage(originalImage: BufferedImage, targetWidth: Int, targetHeight: Int): BufferedImage {
        val resizedImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB)
        val g = resizedImage.createGraphics()
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null)
        g.dispose()
        return resizedImage
    }
}
