package com.github.openapilab.openapipostman.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class YamlServiceTest extends Specification {

    @Autowired
    private YamlService yamlService;

    def "isValidYaml"() {
        given:
        def yaml = "" +
                "name: John Smith\n" +
                "age: 35"
        when:
        def isYaml = yamlService.isValidYaml(yaml)
        then:
        isYaml
    }

    def "isValidYamlFalse"() {
        given:
        def yaml = "" +
                "name: John Smith\n" +
                "   age: 35"
        when:
        def isYaml = yamlService.isValidYaml(yaml)
        then:
        !isYaml
    }

}
