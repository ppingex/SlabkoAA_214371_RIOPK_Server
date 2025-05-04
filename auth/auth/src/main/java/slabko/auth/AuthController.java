package slabko.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import slabko.auth.User;
import slabko.auth.UserRepository;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        User user = userRepo.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (encoder.matches(request.password(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            userRepo.save(user);
            return ResponseEntity.ok(user.getToken());
        }
        return ResponseEntity.status(401).body("Invalid password");
    }

    @PostMapping("/register")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepo.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body("Username занят");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(request.role());
        userRepo.save(user);
        return ResponseEntity.ok("User создан");
    }
}

