package demo

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class DemoControllerSpec extends Specification implements ControllerUnitTest<DemoController> {

    void "test action which renders test"() {
        when:
        controller.hello()

        then:
        response.text == 'Hello, World!'
    }
}

