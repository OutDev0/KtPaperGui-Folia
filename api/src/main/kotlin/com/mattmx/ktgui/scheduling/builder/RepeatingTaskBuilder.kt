package com.mattmx.ktgui.scheduling.builder

import com.mattmx.ktgui.scheduling.IteratingTask
import com.mattmx.ktgui.scheduling.TaskWrapper
import com.mattmx.ktgui.scheduling.asyncRepeat
import com.mattmx.ktgui.scheduling.syncRepeat
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.scheduler.BukkitTask

class RepeatingTaskBuilder(
    val isAsync: Boolean
) {
    var max = -1L
        private set
    var delay = 0L
        private set
    var period = 0L
        private set
    lateinit var block: (IteratingTask) -> Unit
        private set

    infix fun repeat(times: Long) = apply {
        this.max = times
    }

    infix fun delay(ticks: Long) = apply {
        this.delay = ticks
    }

    infix fun period(ticks: Long) = apply {
        this.period = ticks
    }

    infix fun runs(block: IteratingTask.() -> Unit) = apply {
        this.block = block
    }

    fun run(): IteratingTask {
        var task: IteratingTask? = null

        val bukkitBlock: BukkitTask.() -> Unit = {
            if (task!!.iterations > max && max != -1L) {
                cancel()
            }
            block.invoke(task!!)
            task!!.iterations++
        }

        val foliaBlock: ScheduledTask.() -> Unit = {
            if (task!!.iterations > max && max != -1L) {
                cancel()
            }
            block.invoke(task!!)
            task!!.iterations++
        }

        val wrappedTask = if (isAsync) {
            val bukkitTask = asyncRepeat(period, delay, bukkitBlock)
            TaskWrapper.Bukkit(bukkitTask)
        } else {
            val foliaTask = syncRepeat(period, delay, foliaBlock)
            TaskWrapper.Folia(foliaTask)
        }

        task = IteratingTask(wrappedTask)

        return task
    }
}