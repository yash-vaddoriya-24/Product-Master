package com.example.productmaster.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SessionController {

    @GetMapping("/api/checkSession")
    @ResponseBody
    public ResponseEntity<Boolean> checkSession(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }
}
