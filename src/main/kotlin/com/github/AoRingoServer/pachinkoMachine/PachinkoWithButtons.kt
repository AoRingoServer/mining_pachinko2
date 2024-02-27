package com.github.AoRingoServer.pachinkoMachine

import com.github.AoRingoServer.PachinkoPlayer
import com.github.AoRingoServer.Staging
import org.bukkit.block.Block

interface PachinkoWithButtons {
    fun pushingButton(button: Block, connectionBlock: Block, stagingBlock: Block, pachinkoPlayer: PachinkoPlayer, staging: Staging)
}
