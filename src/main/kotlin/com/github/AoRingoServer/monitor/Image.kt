package com.github.Ringoame196

import org.bukkit.plugin.Plugin
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class Image {
    fun make(plugin: Plugin, imageName: String): BufferedImage? {
        val imageFile = File(plugin.dataFolder, "images/$imageName") // 画像ファイルのパス
        if (!imageFile.exists()) {
            return null
        }
        return ImageIO.read(imageFile)
    }
}
