import org.junit.*
import com.lesfurets.jenkins.unit.BasePipelineTest
import static groovy.test.GroovyAssert.*

import com.bitbebop.InfluxDB

class InfluxDBTests extends BasePipelineTest {
    @Test
    void addMeasurement_NoTag_SkipsTagSegment() {
        def influxDB = new InfluxDB();
            influxDB.addMeasurement("foo", [:], [a_float: ["f", 123.45]]
        )

        def measurements = influxDB.getMeasurements()
        assertEquals "No Tag:", /foo a_float=123.45/, measurements[0].toString()
    }

    @Test
    void addMeasurement_WithTag_AddsTagSegment() {
        def influxDB = new InfluxDB();
            influxDB.addMeasurement("foo", [some_tag:"tag_value"], [a_float: ["f", 123.45]]
        )

        def measurements = influxDB.getMeasurements()
        assertEquals "With Tag:", /foo,some_tag=tag_value a_float=123.45/, measurements[0].toString()
    }

    @Test
    void addMeasurement_WithMultipleTags_AddsJoinedTagSegment() {
        def influxDB = new InfluxDB();
            influxDB.addMeasurement("foo", [some_tag:"tag_value", some_other_tag:"another_tag_value"], [a_float: ["f", 123.45]]
        )

        def measurements = influxDB.getMeasurements()
        assertEquals "With Multiple Tags:", /foo,some_tag=tag_value,some_other_tag=another_tag_value a_float=123.45/, measurements[0].toString()
    }

    @Test
    void addMeasurement_FloatDataType_RemainsAsIs() {
        def influxDB = new InfluxDB();
            influxDB.addMeasurement("foo", [:], [a_float: ["f", 123.45]]
        )

        def measurements = influxDB.getMeasurements()
        assertEquals "Float Datatype:", /foo a_float=123.45/, measurements[0].toString()
    }

    @Test
    void addMeasurement_IntDataType_EndsWithi() {
        def influxDB = new InfluxDB();
            influxDB.addMeasurement("foo", [:], [an_int: ["i", 123]]
        )

        def measurements = influxDB.getMeasurements()
        assertEquals "Int Datatype:", /foo an_int=123i/, measurements[0].toString()
    }

    @Test
    void addMeasurement_StringDataType_HasQuotes() {
        def influxDB = new InfluxDB();
            influxDB.addMeasurement("foo", [:], [a_str: ["s", "some string"]]
        )

        def measurements = influxDB.getMeasurements()
        assertEquals "String Datatype:", /foo a_str="some string"/, measurements[0].toString()
    }

    @Test
    void addMeasurement_WithMultipleFields_FieldsAreJoined() {
        def influxDB = new InfluxDB();
            influxDB.addMeasurement("foo", [:], [a_str: ["s", "some string"], an_int: ["i", 123]]
        )

        def measurements = influxDB.getMeasurements()
        assertEquals "String Datatype:", /foo a_str="some string",an_int=123i/, measurements[0].toString()
    }

}
