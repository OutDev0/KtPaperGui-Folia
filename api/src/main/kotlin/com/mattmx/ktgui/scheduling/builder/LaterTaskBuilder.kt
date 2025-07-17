package com.mattmx.ktgui.scheduling.builder

import com.mattmx.ktgui.scheduling.*
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.scheduler.BukkitTask

class LaterTaskBuilder(
    val isAsync: Boolean
) {
    var delay = 0L
        private set
    lateinit var block: (IteratingTask) -> Unit
        private set

    infix fun delay(ticks: Long) = apply {
        this.delay = ticks
    }


    infix fun runs(block: IteratingTask.() -> Unit) = apply {
        this.block = block
    }

    fun run(): IteratingTask {
        var task: IteratingTask? = null

        val bukkitBlock: BukkitTask.() -> Unit = {
            block.invoke(task!!)
            task!!.iterations++
        }

        val foliaBlock: ScheduledTask.() -> Unit = {
            block.invoke(task!!)
            task!!.iterations++
        }

        val wrappedTask = if (isAsync) {
            val bukkitTask = asyncDelayed(delay, bukkitBlock)
            TaskWrapper.Bukkit(bukkitTask)
        } else {
            val foliaTask = syncDelayed(delay, foliaBlock)
            TaskWrapper.Folia(foliaTask)
        }

        task = IteratingTask(wrappedTask)

        return task
    }

}