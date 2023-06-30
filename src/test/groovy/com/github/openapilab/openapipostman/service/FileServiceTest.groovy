package com.github.openapilab.openapipostman.service


import com.github.openapilab.openapipostman.ApplicationProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FileServiceTest extends spock.lang.Specification {

    @Autowired
    FileService fileService
    @Autowired
    ApplicationProperty applicationProperty;

    def "Save"() {
        given:
        def content = "bla bla bla"
        when:
        def filename = fileService.save(content)
        then:
        filename != null
    }

    def "MakeDir"() {
        // override root folde during unit testing
        applicationProperty.setRootFolder(System.getProperty("java.io.tmpdir"))
        when:
        def folder = fileService.makeDir()
        then:
        folder != null
        folder.contains("/postman_")

    }

    def "Read"() {
        given:
        def filename = "src/test/resources/Basic.yaml"
        when:
        def result = fileService.read(filename)
        then:
        result.startsWith("openapi: 3.0.0")
    }
}
