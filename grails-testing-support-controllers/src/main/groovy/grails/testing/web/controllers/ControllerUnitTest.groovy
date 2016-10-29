package grails.testing.web.controllers

import grails.core.GrailsClass
import grails.core.GrailsControllerClass
import grails.util.GrailsMetaClassUtils
import groovy.transform.CompileStatic
import org.grails.core.artefact.ControllerArtefactHandler
import org.grails.plugins.testing.GrailsMockHttpServletRequest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.grails.testing.GrailsUnitTest
import org.grails.web.servlet.mvc.GrailsWebRequest
import org.grails.web.util.GrailsApplicationAttributes

trait ControllerUnitTest<T>  extends GrailsUnitTest<T> {
    /**
     * Mocks a Grails controller class, providing the needed behavior and defining it in the ApplicationContext
     *
     * @param controllerClass The controller class
     * @return An instance of the controller
     */
    def <T> T mockController(Class<T> controllerClass) {
        GrailsClass controllerArtefact = createAndEnhance(controllerClass)
        defineBeans(true) {
            "$controllerClass.name"(controllerClass) { bean ->
                bean.scope = 'prototype'
                bean.autowire = true
            }
        }

        def callable = {->
            final controller = applicationContext.getBean(controllerClass.name)
            webRequest.controllerName = controllerArtefact.logicalPropertyName
            request.setAttribute(GrailsApplicationAttributes.CONTROLLER, controller)
            controller
        }

        GrailsMetaClassUtils.getExpandoMetaClass(controllerClass).constructor = callable

        return callable.call()
    }

    GrailsWebRequest getWebRequest() {
        (GrailsWebRequest)runtime.getValue("webRequest")
    }

    GrailsMockHttpServletRequest getRequest() {
        return (GrailsMockHttpServletRequest)getWebRequest().getCurrentRequest()
    }

    GrailsMockHttpServletResponse getResponse() {
        return (GrailsMockHttpServletResponse)getWebRequest().getCurrentResponse()
    }

    @CompileStatic
    private GrailsClass createAndEnhance(Class controllerClass) {
        final GrailsControllerClass controllerArtefact = (GrailsControllerClass)grailsApplication.addArtefact(ControllerArtefactHandler.TYPE, controllerClass)
        controllerArtefact.initialize()
        return controllerArtefact
    }

    T getController() {
        getCollaboratorInstance()
    }
}
