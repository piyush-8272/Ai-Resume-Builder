package com.resumeanalyzer.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Forwards all non-API, non-asset requests to index.html so React Router works
 * when the app is served from the Spring Boot JAR.
 */
@Controller
public class SpaController {

    @RequestMapping(value = {"/", "/{path:[^\\.]*}", "/{path:[^\\.]*}/**"})
    public String spa() {
        return "forward:/index.html";
    }
}
