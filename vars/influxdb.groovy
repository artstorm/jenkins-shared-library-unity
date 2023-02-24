#!/usr/bin/env groovy
/**
 * Write data to InfluxDB.
 *
 * Measurements are collected with the addMeasurement method. Calling the write
 * method will write all collected measurements in the same call to InfluxDB.
 */
import com.bitbebop.InfluxDB

@groovy.transform.Field private def influxDB = new InfluxDB()

/**
 * Add a measurement for writing to InfluxDB.
 *
 * The provided measurement data is transformed to InfluxDB line protocol.
 * https://docs.influxdata.com/influxdb/v2.6/reference/syntax/line-protocol/
 *
 * @param String measurement The measurement to write data to.
 * @param Map    tags        The measurement tags.
 * @param Map    fields      The measurement fields, each field is an array
 *                           with type and value.
 */
def addMeasurement(String measurement, def tags, def fields) {
    influxDB.addMeasurement(measurement, tags, fields)
}

/**
 * Get the array of line protocol formatted strings.
 */
def getMeasurements() {
    return influxDB.getMeasurements()
}

/**
 * Write all added measurements to InfluxDB.
 *
 * @param String host   The host address to InfluxDB. Include http/https. No trailing slash.
 * @param String org    The InfluxDB organization.
 * @param String bucket The bucket to write to.
 * @param String token  The InfluxDB API token to use for authentication.
 */
def write(String host, String org, String bucket, String token) {
    if (getMeasurements().size() == 0) {
        return
    }

    sh 'curl --request POST ' +
    "\"${host}/api/v2/write?org=${org}&bucket=${bucket}&precision=ns\" " +
    '--header "Authorization: Token ' + token + '" ' +
    '--header "Content-Type: text/plain; charset=utf-8" ' +
    '--header "Accept: application/json" ' +
    '--fail-with-body ' +
    "--data-binary '${getMeasurements().join('\n')}'"
}
