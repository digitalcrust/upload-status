package gov.sciencebase.files.upload.status

import spock.lang.Specification

class PullStatusUpdateSpec extends Specification {
    def "withProgress should return a valid percentage"() {
        given:
        def statusUpdate = PullStatusUpdate.createStatusUpdate(PullStatus.STARTED)

        expect:
        statusUpdate.withProgress(50, 100).percentComplete == 50.0f
    }
}
