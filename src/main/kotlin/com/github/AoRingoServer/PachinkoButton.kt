package com.github.AoRingoServer

import org.bukkit.block.Block
import org.bukkit.block.BlockFace

class PachinkoButton {
    fun acquisitionConnectionBlock(button: Block): Block? {
        for (face in arrayOf(BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST)) {
            val relative = button.getRelative(face)
            if (relative.type.isSolid) { // 接続しているブロックは固体かどうかを確認
                return relative
            }
        }
        return null
    }
}
