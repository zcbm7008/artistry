package com.artistry.artistry.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WebRestController {

    private final Environment env;

    @GetMapping("/profile")
    public String getProfile(){
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> setProfiles = Arrays.asList("set1","set2");
        String defaultProfile = profiles.isEmpty()? "default" : profiles.get(0);

        return profiles.stream()
                .filter(setProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
