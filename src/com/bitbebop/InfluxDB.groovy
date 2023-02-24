package com.bitbebop

/**
 * Collects measurements for writing to InfluxDB.
 *
 * The provided measurement data is transformed to InfluxDB line protocol.
 * https://docs.influxdata.com/influxdb/v2.6/reference/syntax/line-protocol/
 */
class InfluxDB {
    private def measurements = []

    /**
     * Add a measurement for writing to InfluxDB.
     *
     * @param String measurement The measurement to write data to.
     * @param Map    tags        The measurement tags.
     * @param Map    fields      The measurement fields, each field is an array
     *                           with type and value.
     */
    def addMeasurement(String measurement, def tags, def fields) {

        def tagsString = tags.collect { /$it.key=$it.value/ } join ","
        if (tagsString.length() > 0) {
            tagsString = ",${tagsString}"
        }

        def fieldsString = fields.collect {
            switch(it.value[0]) {
                case "f":
                /$it.key=${it.value[1]}/
                break;
                case "i":
                /$it.key=${it.value[1]}i/
                break;
                case "s":
                /$it.key="${it.value[1]}"/
                break;
            }
        } join ","

        def line = "${measurement}${tagsString} ${fieldsString}"
        measurements.add(line)
    }

    /**
     * Get the array of line protocol formatted strings, that can be written to
     * InfluxDB.
     */
    def getMeasurements() {
        return measurements
    }
}
