package com.mattmx.ktgui.scheduling

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.scheduler.BukkitTask

/**
 * Wraps Bukkit or Folia scheduled tasks with a common interface.
 */
sealed class TaskWrapper {
    class Bukkit(val task: BukkitTask) : TaskWrapper()
    class Folia(val task: ScheduledTask) : TaskWrapper()

    fun cancel() {
        when (this) {
            is Bukkit -> task.cancel()
            is Folia -> task.cancel()
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this is Bukkit && other is Bukkit -> this.task == other.task
            this is Folia && other is Folia -> this.task == other.task
            else -> false
        }
    }

    override fun hashCode(): Int {
        return when (this) {
            is Bukkit -> task.hashCode()
            is Folia -> task.hashCode()
        }
    }
}
