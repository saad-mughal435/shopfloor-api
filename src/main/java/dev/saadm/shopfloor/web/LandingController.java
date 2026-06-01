package dev.saadm.shopfloor.web;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Serves the branded landing page at the root with a plain 200.
 *
 * <p>We return the static file's bytes directly rather than relying on Spring
 * Boot's welcome-page mechanism, which forwards "/" → "/index.html"; that
 * forward gets re-evaluated by Spring Security and rejected as 401. Returning
 * the resource from a controller means only the (permitted) "/" request is
 * authorized, so the page is publicly reachable.
 */
@RestController
public class LandingController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Resource> landing() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(new ClassPathResource("static/index.html"));
    }
}
