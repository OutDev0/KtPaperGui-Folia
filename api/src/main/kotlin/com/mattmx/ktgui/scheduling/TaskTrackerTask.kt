package com.mattmx.ktgui.scheduling

/**
 * Wrapper class for a tracked task that can be either Bukkit or Folia.
 */
class TaskTrackerTask(
    private val owner: TaskTracker,
    override val taskWrapper: TaskWrapper
) : IteratingTask(taskWrapper) {

    override fun cancel() {
        owner.removeTask(this)
        super.cancel()
    }
}
