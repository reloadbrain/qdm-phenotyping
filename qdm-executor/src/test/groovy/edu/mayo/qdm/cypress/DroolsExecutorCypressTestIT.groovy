package edu.mayo.qdm.cypress
import edu.mayo.qdm.executor.MeasurementPeriod
import edu.mayo.qdm.executor.drools.DroolsExecutor
import edu.mayo.qdm.executor.drools.parser.Qdm2Drools
import groovy.json.JsonSlurper
import org.apache.commons.io.IOUtils
import org.joda.time.DateTime
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import static org.junit.Assert.assertNotNull

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/qdm-executor-context.xml")
public class DroolsExecutorCypressTestIT {

	@Autowired
	private DroolsExecutor executor

    @Autowired
    private Qdm2Drools qdm2Drools

    def cypressHelper = new CypressPatientHelper()

    def slurper = new JsonSlurper()
	
	@Test
	public void TestSetUp(){
		assertNotNull(this.executor)
	}

    /*
     * 127 is fairly simple
     */
    @Test
    public void TestExecute127() throws IOException{
        doExecute("0043")
    }

    /*
     * 165 -- NQF 0018, High Blood Pressure
     */
    @Test
    public void TestExecute165() throws IOException{
        doExecute("0018")
    }

    /*
     * 124 -- NQF 0032, High Blood Pressure
     */
    @Test
    public void TestExecute124() throws IOException{
        doExecute("0032")
    }

    /*
     * 146 -- Some negation issues to work out...
     */
    @Test
    public void TestExecute0002() throws IOException{
        doExecute("0002")
    }

    /*
     * 137 -- Specific occurrences on the DENOM exceptions
     */
    @Test
    public void TestExecute0004() throws IOException{
        doExecute("0004")
    }

    @Test
    public void TestExecute0024() throws IOException{
        doExecute("0024")
    }

    @Test
    public void TestExecute0028() throws IOException{
        doExecute("0028")
    }

    @Test
    public void TestExecute0031() throws IOException{
        doExecute("0031")
    }

    @Test
    public void TestExecute0034() throws IOException{
        doExecute("0034")
    }

    @Test
    public void TestExecute0069() throws IOException{
        doExecute("0069")
    }

    @Test
    public void TestExecuteHIVRNAControl() throws IOException{
        doExecute("HIVRNAControl")
    }

    @Test
    public void TestDementiaCognitive() throws IOException{
        doExecute("DementiaCognitive")
    }

    void doExecute(measureId) throws IOException{
        qdm2Drools.metaClass.getJsonFromQdmFile = {
            xml ->
                slurper.parseText(
                        IOUtils.toString(new ClassPathResource("cypress/measures/ep/$measureId/hqmf_model.json").inputStream))
        }
        def xmlStream = new ClassPathResource("cypress/measures/ep/${measureId}/hqmf1.xml").getInputStream()

        def xmlString = IOUtils.toString(xmlStream, "UTF-8")

        def patientList = cypressHelper.getPatients()

        def results = this.executor.execute(patientList, xmlString, MeasurementPeriod.getCalendarYear(new DateTime(2012,1,1,1,1).toDate()))

        cypressHelper.checkResults(measureId, results,
                {population, expected, actual, message ->
                    println message
                    //assertEquals expected, actual, 0
                })
    }

}
