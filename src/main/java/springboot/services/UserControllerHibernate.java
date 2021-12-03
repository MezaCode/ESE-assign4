package springboot.services;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.model.Session;
import springboot.model.User;
import springboot.repository.SessionRepository;
import springboot.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import utils.HashUtils;
@RestController
public class UserControllerHibernate {
    private static final Logger logger = LogManager.getLogger();

    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    public UserControllerHibernate(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> fetchUsers(@RequestHeader Map<String, String> headers) {
        // get the list of cat models from the orm's repository
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.valueOf(200));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> fetchUserById(@RequestBody Map<String, String> json) {
        Optional<User> user = userRepository.findBylogin(json.get("username"));
        Session sess;
        if(user.isPresent()) {
            if(user.get().getPassword().equals(json.get("password"))) {
                sess = new Session(user.get(), HashUtils.getCryptoHash(user.get().getLogIn() + UUID.randomUUID(), "SHA-256"));
                sessionRepository.save(sess);
                JSONObject formData = new JSONObject();
                formData.put("session_id", sess);
                String formDataString = formData.toString();
                return new ResponseEntity<>(formDataString, HttpStatus.valueOf(200));
            }
            else
                return new ResponseEntity<>("", HttpStatus.valueOf(401));
        }
        else
            return new ResponseEntity<>("", HttpStatus.valueOf(401));


    }

}
