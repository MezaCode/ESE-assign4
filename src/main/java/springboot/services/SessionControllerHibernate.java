package springboot.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@RestController
public class SessionControllerHibernate {
    private static final Logger logger = LogManager.getLogger();

    private SessionRepository sessionRepository;

    public SessionControllerHibernate(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/sessions")
    public ResponseEntity<Session> fetchSessionById(@PathVariable String token, @RequestHeader Map<String, String> headers) {
        Optional<Session> session = sessionRepository.findByToken(token);
        if(session.isPresent())
            return new ResponseEntity<>(session.get(), HttpStatus.valueOf(200));
        else
            return new ResponseEntity<>(new Session(), HttpStatus.valueOf(404));


    }
//fetch by session to get user id
    @PostMapping("/Login/{login}")
    @ResponseBody
    public String addSession(@RequestHeader Session session){
        session = sessionRepository.save(session);
        return "ok";
    }

}
