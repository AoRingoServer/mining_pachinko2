package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoPlayer
import org.bukkit.block.Block

interface PachinkoMachines {
    fun shoot(block: Block, pachinkoPlayer: PachinkoPlayer)
}
