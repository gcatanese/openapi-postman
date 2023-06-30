package com.github.openapilab.openapipostman.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class JsonServiceTest extends Specification {

    @Autowired
    private JsonService jsonService

    def "isValidJson"() {
        given:
        def json = "{ \"name\": \"John\", \"age\": 30, \"city\": \"New York\" }"
        when:
        def isJson = jsonService.isValidJson(json)
        then:
        isJson
    }

    def "isValidJsonFalse"() {
        given:
        def json = "{ invalid }"
        when:
        def isJson = jsonService.isValidJson(json)
        then:
        !isJson
    }

    def "IsJson"() {
        given:
        def json = "{ \"name\": \"John\", \"age\": 30, \"city\": \"New York\" }"
        when:
        def isJson = jsonService.isJson(json)
        then:
        isJson
    }

    def "IsJsonFalse"() {
        given:
        def json = "" +
                "name: John Smith\n" +
                "age: 35"
        when:
        def isJson = jsonService.isJson(json)
        then:
        !isJson
    }

    def "PrettifyJson"() {
        given:
        def json = "{ \"name\": \"John\", \"age\": 30, \"city\": \"New York\" }"
        when:
        def prettyJson = jsonService.prettifyJson(json)
        then:
        prettyJson != null
    }
}
