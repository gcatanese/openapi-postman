package com.github.openapilab.openapipostman.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class GeneratorServiceTest extends Specification {

    @Autowired
    GeneratorService generatorService

    def "GetPath"() {
        given:
        def filepath = "/folder/anotherfolder/file.txt"
        when:
        def path = generatorService.getPath(filepath)
        then:
        path.equals("/folder/anotherfolder")
    }
}
