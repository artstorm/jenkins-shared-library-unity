#!/usr/bin/env groovy
/**
 * Write data to InfluxDB.
 */

// Array that holds the measurements to write to InfluxDB.
@groovy.transform.Field private def measurements = []

// https://docs.influxdata.com/influxdb/v2.6/reference/syntax/line-protocol/
def addMeasurement(String measurement, def tags, def fields) {

    def tagsString = tags.collect { /$it.key=$it.value/ } join ","
    if (tagsString.length() > 0) {
        tagsString = ",${tagsString}"
    }

    def fieldsString = fields.collect { it.value instanceof String ? /$it.key="$it.value"/ : /$it.key=$it.value/ } join ","

    def line = "${measurement}${tagsString} ${fieldsString}"
    measurements.add(line)
}

def write(String host, String org, String bucket, String token) {
    if (this.measurements.size() == 0) {
        return
    }

    sh 'curl --request POST ' +
    "\"${host}/api/v2/write?org=${org}&bucket=${bucket}&precision=ns\" " +
    '--header "Authorization: Token ' + token + '" ' +
    '--header "Content-Type: text/plain; charset=utf-8" ' +
    '--header "Accept: application/json" ' +
    '--fail-with-body ' +
    "--data-binary '${this.measurements.join('\n')}'"
}
