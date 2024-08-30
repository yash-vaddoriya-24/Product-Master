package com.example.productmaster.Controller;

import com.example.productmaster.Repository.RegisterRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    private RegisterRepository registerRepository;

    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<String> checkLogin(@RequestBody UserDetails userDetails, HttpServletRequest request) {

        try {
            String correctPassword = registerRepository.checkPassword(userDetails.getUname());
            log.info("Correct password retrieved for user {}: {}", userDetails.getUname(), correctPassword);

            if (registerRepository.existsByUname(userDetails.getUname()) && correctPassword.equals(userDetails.getPassword())) {
                HttpSession session = request.getSession(true);
                session.setAttribute("username", userDetails.getUname());
                session.setAttribute("password", userDetails.getPassword());
                log.info("User {} logged in successfully.", userDetails.getUname());
                return ResponseEntity.ok("Login Successfully");
            } else {
                log.warn("Login failed for user {}: Incorrect username or password.", userDetails.getUname());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Username or password wrong");
            }
        } catch (Exception e) {
            log.error("An error occurred during login for user {}: {}", userDetails.getUname(), e.getMessage(), e);
            return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR)).body("Something went wrong while Login");
        }
    }

    @PostMapping("/api/logout")
    @ResponseBody
    public ResponseEntity<String> logout(HttpSession session) {
        try {
            if (session != null) {
                session.invalidate();
                log.info("User logged out successfully.");
                return ResponseEntity.ok("Logout Successfully");
            } else {
                log.warn("Logout attempt failed: No active session found.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login Required");
            }
        } catch (Exception e) {
            log.error("An error occurred during logout: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong while Logout");
        }
    }


    @GetMapping("{fileName}")
    public String getHtmlPage(@PathVariable String fileName) {
        log.info("Serving HTML page: {}", fileName);
        return fileName;
    }
}

class UserDetails {
    private String uname;
    private String password;
    private boolean active;

    public UserDetails(String username, String password, boolean active) {
        this.uname = username;
        this.password = password;
        this.active = active;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
