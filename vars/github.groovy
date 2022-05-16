#!/usr/bin/env groovy

/**
 * GitHub functions.
 *
 * Multiple functions in one file:
 * https://stackoverflow.com/q/69119132/1152087
 *
 *
 * The GitHub integration relies on plugins and Shared Pipeline methods.
 *
 * Plugins:
 * - Pipeline Utility Steps: encode/decode JSON.
 * - GitHub App: Authentication (https://www.jenkins.io/blog/2020/04/16/github-app-authentication/).
 * - HTTP Request: API calls.
 *
 * Shared Pipeline:
 * - git.commitSha()
 */

/**
 * Get the <owner/repo> part from the project's git url.
 *
 * Source: https://stackoverflow.com/a/69917658/1152087
 */
def ownerRepo() {
  return env.GIT_URL.replaceFirst(/^.*?(?::\/\/.*?\/|:)(.*).git$/, '$1')
}

/*
  -----------------------------------------------------------------------------
    API CALLS
  -----------------------------------------------------------------------------
*/

/**
 * Returns the pull request the current commit belongs to.
 * https://docs.github.com/en/rest/commits/commits#list-pull-requests-associated-with-a-commit
 *
 * Returns the first PR if there are multiple PRs associated or null if there is no PR associated.
 */
def pullRequest()
{
  withCredentials([usernamePassword(credentialsId: 'githubapp-jenkins',
                                    usernameVariable: 'GITHUB_APP',
                                    passwordVariable: 'GITHUB_ACCESS_TOKEN')]) {
    def sha = git.commitSha()
    def ownerRepo = ownerRepo()

    def response = httpRequest(
      customHeaders: [
        [name: 'Authorization', value: "Token " + GITHUB_ACCESS_TOKEN],
        [name: 'accept', value: "application/vnd.github.v3+json"]
      ],
      httpMode: 'GET', url: "https://api.github.com/repos/${ownerRepo}/commits/${sha}/pulls"
    )

    def pullRequests = readJSON text: response.content
    return pullRequests[0]    // returns null if empty array
  }
}
