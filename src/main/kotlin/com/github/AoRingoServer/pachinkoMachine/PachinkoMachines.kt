package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoPlayer
import org.bukkit.block.Block

interface PachinkoMachines {
    fun acquisitionUseBallCount(): Int
    fun shoot(block: Block, stagingBlock:Block , pachinkoPlayer: PachinkoPlayer)
}
