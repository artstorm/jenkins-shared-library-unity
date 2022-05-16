#!/usr/bin/env groovy
import hudson.tasks.test.AbstractTestResultAction

/**
 * Tests related functions.
 *
 * Multiple functions in one file:
 * https://stackoverflow.com/q/69119132/1152087
 */

/**
 * Get Junit test result summary.
 *
 * Source:
 * https://stackoverflow.com/a/39958212/1152087
 */
@NonCPS
def summary() {
  AbstractTestResultAction testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
  if (testResultAction == null) {
    return
  }

  // We get the values that's most interesting for this purpose.
  def total = testResultAction.totalCount
  def failed = testResultAction.failCount
  def skipped = testResultAction.skipCount

  summary = [
    total: total,
    failed: failed,
    skipped: skipped,
    passed: total - failed - skipped
  ]

  return summary
}
