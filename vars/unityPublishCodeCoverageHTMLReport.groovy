/**
 * Publishes the Unity Code Coverage Report so it's available from the job page.
 *
 * Uses plugin: HTML Publisher
 * - https://plugins.jenkins.io/htmlpublisher/
 *
 * If Jenkins doesn't load CSS/JS on the report page, this is a possible solution.
 *
 * The HTML publisher needs to set the security header to allow javascript on
 * the published HTML.
 * https://www.jenkins.io/doc/book/security/configuring-content-security-policy/#html-publisher-plugin
 *
 * Unity ReportGenerator uses javascript for the history graph.
 *
 * Set it in console (Won't survive restart)
 * 1. Go to Manage Jenkins.
 * 2. Now go to Script Console.
 * 3. And in that console paste below statement and click on Run.
 * 4. System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "")
 * 5. After that it will load css and js.
 *
 * Set it in startup (permanent change)
 * - Modify: /usr/local/opt/jenkins/homebrew.mxcl.jenkins.plist
 * - Just add the CSP parameter below before the <string>-jar</string> line.
 * - <string>-Dhudson.model.DirectoryBrowserSupport.CSP=</string>
 */
def call() {
  publishHTML (target : [allowMissing: false,
    alwaysLinkToLastBuild: true,
    keepAll: true,
    reportDir: 'Reports/CodeCoverage/Report',
    reportFiles: 'index.html',
    reportName: 'Reports',
    reportTitles: 'Code Coverage'])
}
