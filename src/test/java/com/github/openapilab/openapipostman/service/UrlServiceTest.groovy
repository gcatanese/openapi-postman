package com.github.openapilab.openapipostman.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class UrlServiceTest extends Specification {

    @Autowired
    UrlService urlService

    def "FetchUrl"() {
        given:
        def url = "https://github.com/gcatanese"
        when:
        def content = urlService.fetchUrl(url)
        then:
        content != null
    }
}
