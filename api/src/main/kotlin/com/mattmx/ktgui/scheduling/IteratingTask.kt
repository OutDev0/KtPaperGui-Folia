package com.mattmx.ktgui.scheduling

open class IteratingTask(
    open val taskWrapper: TaskWrapper
) {
    /**
     * How many times the task has repeated.
     * Do not increment yourself.
     */
    var iterations = 0

    open fun cancel() = when (val t = taskWrapper) {
        is TaskWrapper.Bukkit -> t.task.cancel()
        is TaskWrapper.Folia -> t.task.cancel()
    }
}