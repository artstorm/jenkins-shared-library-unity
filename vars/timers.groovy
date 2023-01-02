import com.bitbebop.Timer

private def timers = [:]

/**
 * Creates and starts a new timer with the specified name.
 */
def start(String timerName) {
    this.timers[timerName] = new Timer()
    this.timers[timerName].start()
}

/**
 * Stops the timer with the specified name.
 */
def stop(String timerName) {
    this.timers[timerName].stop()
}

/**
 * Gets the duration in ms between start and stop for the timer with the specified name.
 */
def getDuration(String timerName) {
    return this.timers[timerName].duration as int
}
