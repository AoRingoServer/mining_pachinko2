package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.Staging
import org.bukkit.block.Block

interface PachinkoMachines {
    fun acquisitionUseBallCount(): Int
    fun shoot(block: Block, stagingBlock: Block, pachinkoPlayer: PachinkoPlayer, staging: Staging)
}
