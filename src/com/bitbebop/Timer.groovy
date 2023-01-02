package com.bitbebop

/**
 * Timer that provides the duration between started and stopped time.
 */
class Timer {
  private int started
  private int stopped
  private int duration

  def start() {
    started = System.currentTimeMillis()
  }

  def stop() {
    stopped = System.currentTimeMillis()
    duration = stopped - started
  }

  def getDuration() {
    return duration
  }
}
