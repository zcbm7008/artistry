package com.artistry.artistry.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/api/docs")
@Controller
public class DocsController {

    private static final String API_DOCS_PATH = "generated-docs/api-docs.html";

    @GetMapping("/")
    public String getApiDocs() {return API_DOCS_PATH; }
}
