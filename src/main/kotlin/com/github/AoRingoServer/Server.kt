package com.github.AoRingoServer

import org.bukkit.plugin.java.JavaPlugin
import java.lang.management.ManagementFactory
import java.lang.management.MemoryMXBean

class Server(private val plugin: JavaPlugin) {
    fun getCPUUsage(): Double {
        val threadMXBean = ManagementFactory.getThreadMXBean()
        val threadIds = threadMXBean.allThreadIds
        var cpuTime = 0L

        if (threadMXBean.isThreadCpuTimeEnabled) { // CPU時間の収集が有効か確認
            for (threadId in threadIds) {
                cpuTime += threadMXBean.getThreadCpuTime(threadId)
            }
        } else {
            println("CPU time measurement is not enabled")
            return 0.0
        }

        val totalTime = threadIds.size * 1000 * 1000 // ns -> ms に変換
        return if (totalTime > 0) {
            cpuTime.toDouble() / totalTime.toDouble()
        } else {
            0.0
        }
    }
    fun getMemoryUsage(): Double {
        val memoryMXBean: MemoryMXBean = ManagementFactory.getMemoryMXBean()
        val heapMemoryUsage = memoryMXBean.heapMemoryUsage
        val usedMemory = heapMemoryUsage.used.toDouble()
        val maxMemory = heapMemoryUsage.max.toDouble()
        return usedMemory
    }
}
