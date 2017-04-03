package gov.sciencebase.files.upload.status

import spock.lang.Specification

class PullStatusUpdateSpec extends Specification {
    def "withProgress should return a valid percentage"() {
        given:
        def statusUpdate = PullStatusUpdate.createStatusUpdate(PullStatus.STARTED, new UserLink("user", "http://www.sciencebase.gov", "fileName"))

        expect:
        statusUpdate.withProgress(50L, 100L).percentComplete == 50.0
    }
}
