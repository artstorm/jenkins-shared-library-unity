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
 * Creates or updates the Jenkins bot issue comment for the pull request.
 *
 * @param String body The comment body text.
 * @param String body The name of our Jenkins bot.
 */
def pullRequestComment(String body, String botName) {
  withCredentials([usernamePassword(credentialsId: 'githubapp-jenkins',
                                    usernameVariable: 'GITHUB_APP',
                                    passwordVariable: 'GITHUB_ACCESS_TOKEN')]) {
    // Check if the commit belongs to a PR.
    def pullRequest = pullRequest()
    // If it doesn't belong to a PR, we can stop here.
    if (!pullRequest) return

    // Get all issue comments belonging to the pull request.
    def comments = issueComments(pullRequest.number)

    // Check if our jenkins bot has already commented on the PR.
    // If so, we will update the existing comment instead of creating a new.
    def commentId
    for (comment in comments) {
      if (comment.user.login == botName) {
        commentId = comment.id
      }
    }

    // Create or update, depending on if a comment already existed.
    if (commentId) {
     updateIssueComment(commentId, body)
    } else {
     createIssueComment(pullRequest.number, body)
    }
  }
}

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

/**
 * Retrieves all comments for an issue/pull request.
 * https://docs.github.com/en/rest/issues/comments#list-issue-comments
 */
def issueComments(int number)
{
  withCredentials([usernamePassword(credentialsId: 'githubapp-jenkins',
                                    usernameVariable: 'GITHUB_APP',
                                    passwordVariable: 'GITHUB_ACCESS_TOKEN')]) {
    def ownerRepo = ownerRepo()

    def response = httpRequest(
      customHeaders: [
        [name: 'Authorization', value: "Token " + GITHUB_ACCESS_TOKEN],
        [name: 'accept', value: "application/vnd.github.v3+json"]
      ],
      httpMode: 'GET',
      url: "https://api.github.com/repos/${ownerRepo}/issues/${number}/comments"
    )

    def comments = readJSON text: response.content
    return comments
  }
}

/**
 * Create issue comment.
 * https://docs.github.com/en/rest/issues/comments#create-an-issue-comment
 */
def createIssueComment(int number, String body) {
  withCredentials([usernamePassword(credentialsId: 'githubapp-jenkins',
                                    usernameVariable: 'GITHUB_APP',
                                    passwordVariable: 'GITHUB_ACCESS_TOKEN')]) {
    def payload = ['body': body]
    String json = writeJSON returnText: true, json: payload
    def ownerRepo = ownerRepo()

    def response = httpRequest(
      customHeaders: [
        [name: 'Authorization', value: "Token " + GITHUB_ACCESS_TOKEN],
        [name: 'accept', value: "application/vnd.github.v3+json"]
      ],
      httpMode: 'POST',
      url: "https://api.github.com/repos/${ownerRepo}/issues/${number}/comments",
      requestBody: json
    )
  }
}

/**
 * Update issue comment.
 * https://docs.github.com/en/rest/issues/comments#update-an-issue-comment
 */
def updateIssueComment(int id, String body) {
  withCredentials([usernamePassword(credentialsId: 'githubapp-jenkins',
                                    usernameVariable: 'GITHUB_APP',
                                    passwordVariable: 'GITHUB_ACCESS_TOKEN')]) {
    def payload = ['body': body]
    String json = writeJSON returnText: true, json: payload
    def ownerRepo = ownerRepo()

    def response = httpRequest(
      customHeaders: [
        [name: 'Authorization', value: "Token " + GITHUB_ACCESS_TOKEN],
        [name: 'accept', value: "application/vnd.github.v3+json"]
      ],
      httpMode: 'PATCH',
      url: "https://api.github.com/repos/${ownerRepo}/issues/comments/${id}",
      requestBody: json
    )
  }
}

/**
 * Create a check run.
 * https://docs.github.com/en/rest/checks/runs#create-a-check-run
 *
 * status: queued, in_progress, completed
 *
 * As we have an GitHub app, we use the checks API instead of using the older commit status API.
 */
def createCheckRun(String name, String status, String url = '') {
  withCredentials([usernamePassword(credentialsId: 'githubapp-jenkins',
                                    usernameVariable: 'GITHUB_APP',
                                    passwordVariable: 'GITHUB_ACCESS_TOKEN')]) {
    def sha = git.commitSha();
    def ownerRepo = ownerRepo()
    def payload = [
      'head_sha': sha,
      'name': name,
      'external_id': env.BUILD_NUMBER,
      'status': status
    ]
    if (details_url.length() > 0) payload['details_url'] = details_url
    String json = writeJSON returnText: true, json: payload

    def response = httpRequest(
      customHeaders: [
        [name: 'Authorization', value: "Token " + GITHUB_ACCESS_TOKEN],
        [name: 'accept', value: "application/vnd.github.v3+json"]
      ],
      httpMode: 'POST',
      url: "https://api.github.com/repos/${ownerRepo}/check-runs",
      requestBody: json
    )

    def checkRun = readJSON text: response.content
    return checkRun.id
  }
}

/**
 * Update a check run.
 * https://docs.github.com/en/rest/checks/runs#update-a-check-run
 *
 * status: queued, in_progress, completed
 * conclusion: action_required, cancelled, failure, neutral, success, skipped, stale, timed_out
 * Providing conclusion will automatically set the status parameter to completed
 *
 * As we have an GitHub app, we use the checks API instead of using the older commit status API.
 */
def updateCheckRun(id, String status = '', String conclusion = '') {
  withCredentials([usernamePassword(credentialsId: 'githubapp-jenkins',
                                    usernameVariable: 'GITHUB_APP',
                                    passwordVariable: 'GITHUB_ACCESS_TOKEN')]) {
    def sha = git.commitSha();
    def ownerRepo = ownerRepo()

    def payload = [
      'head_sha': sha
    ]
    if (status.length() > 0) payload['status'] = status
    if (conclusion.length() > 0) payload['conclusion'] = conclusion
    String json = writeJSON returnText: true, json: payload
    echo "${json}"

    def response = httpRequest(
      customHeaders: [
        [name: 'Authorization', value: "Token " + GITHUB_ACCESS_TOKEN],
        [name: 'accept', value: "application/vnd.github.v3+json"]
      ],
      httpMode: 'PATCH',
      url: "https://api.github.com/repos/${ownerRepo}/check-runs/${id}",
      requestBody: json
    )
  }
}
