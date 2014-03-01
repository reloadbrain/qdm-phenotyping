package edu.mayo.qdm.executor.drools.parser.criteria
import edu.mayo.qdm.patient.Value
import groovy.transform.EqualsAndHashCode
/**
 */
@EqualsAndHashCode
class MeasurementValue extends Value implements Serializable {

    boolean inclusive

    public MeasurementValue(){
        super()
    }

    public MeasurementValue(String value, String unit, boolean inclusive) {
        super(value, unit)
        this.inclusive = inclusive
    }

}