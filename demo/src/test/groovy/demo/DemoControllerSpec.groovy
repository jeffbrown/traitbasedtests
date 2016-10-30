package demo

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class DemoControllerSpec extends Specification implements ControllerUnitTest<DemoController> {

    void "test action which renders test"() {
        when:
        controller.hello()

        then:
        status == 200
        response.text == 'Hello, World!'
    }

    void 'test invalid request method'() {
        when:
        request.method = 'POST'
        controller.clearDatabase()

        then:
        status == 405
    }

    void 'test valid request method'() {
        when:
        request.method = 'DELETE'
        controller.clearDatabase()

        then:
        status == 200
        response.text == 'Success'
    }
}

