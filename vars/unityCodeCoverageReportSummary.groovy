#!/usr/bin/env groovy

/**
 * Parses Summary.xml from the Unity generated code report.
 *
 * https://docs.groovy-lang.org/latest/html/api/groovy/xml/XmlParser.html
 */
def call() {
  def path = "Reports/CodeCoverage/Report/Summary.xml"

  def text = readFile(path)
  def parser = new XmlParser()
  def xml = parser.parseText(text.toString())

  // We get the values that's most interesting.
  summary = [
    coveredLines: Integer.valueOf(xml['Summary']['Coveredlines'].text()),
    uncoveredLines: Integer.valueOf(xml['Summary']['Uncoveredlines'].text()),
    coverableLines: Integer.valueOf(xml['Summary']['Coverablelines'].text()),
    totalLines: Integer.valueOf(xml['Summary']['Totallines'].text()),
    lineCoverage: Float.valueOf(xml['Summary']['Linecoverage'].text())
  ]

  return summary
}
