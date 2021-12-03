package springboot.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.model.People;
import springboot.model.Session;
import springboot.model.AuditTrail;
import springboot.repository.PeopleRepository;
import springboot.repository.SessionRepository;
import springboot.repository.AuditTrailRepository;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PeopleControllerHibernate {
    private static final Logger logger = LogManager.getLogger();

    private PeopleRepository peopleRepository;
    private SessionRepository sessionRepository;
    private AuditTrailRepository auditTrailRepository;
    public PeopleControllerHibernate(PeopleRepository peopleRepository, SessionRepository sessionRepository) {
        this.peopleRepository = peopleRepository;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/people")
    public ResponseEntity<List<People>> fetchPeople(@RequestHeader("Authorization") String headers) {
        // get the list of cat models from the orm's repository
        List<People> people;
        System.out.println(headers);
        if (headers != null) {
            if (sessionRepository.findByToken(headers).isPresent()) {
                people = peopleRepository.findAll();
                return new ResponseEntity<>(people, HttpStatus.valueOf(200));
            }
            else{
                people = new ArrayList<People>();
                return new ResponseEntity<>(people, HttpStatus.valueOf(401));
            }
        }
        else{
            people = new ArrayList<People>();
            return new ResponseEntity<>(people, HttpStatus.valueOf(401));
        }

    }

    @GetMapping("/people/{id}")
    public ResponseEntity<People> fetchPersonById(@PathVariable("id") Long id, @RequestHeader("Authorization") String headers) {
        Optional<People> people;
        if (peopleRepository.findById(id).isPresent()) {
            if (sessionRepository.findByToken(headers).isPresent()) {
                people = peopleRepository.findById(id);
                return new ResponseEntity<>(people.get(), HttpStatus.valueOf(200));
            }
            else
                return new ResponseEntity<>(new People(), HttpStatus.valueOf(401));
        }
        else{
            return new ResponseEntity<>(new People(), HttpStatus.valueOf(404));
        }

    }

    @PostMapping("/people")
    @ResponseBody
    public ResponseEntity<People> addPerson(@RequestBody Map<String, String> requestBody, @RequestHeader("Authorization") String headers){
        People person = new People();
        if (headers != null) {
            if (sessionRepository.findByToken(headers).isPresent()) {
                if (requestBody.containsKey("first_name") && requestBody.containsKey("last_name") && requestBody.containsKey("dob")) {
                    person.setFirst_name(requestBody.get("first_name"));
                    person.setLast_name(requestBody.get("last_name"));
                    person.setdob(Date.valueOf(requestBody.get("dob")));
                    person = peopleRepository.save(person);
                    return new ResponseEntity<>(person, HttpStatus.valueOf(200));
                }
                else
                    return new ResponseEntity<>(person, HttpStatus.valueOf(400));
            }
            else
                return new ResponseEntity<>(person, HttpStatus.valueOf(401));
        }
        else
            return new ResponseEntity<>(person, HttpStatus.valueOf(401));
    }

    @PutMapping("/people/{id}")
    @ResponseBody
    public ResponseEntity<People> updatePerson(@PathVariable("id") long personId, @RequestBody Map<String, String> requestBody, @RequestHeader("Authorization") String headers){
        People personToSave;
        if( peopleRepository.findById(personId).isPresent()) {
            personToSave =peopleRepository.findById(personId).get();
            if (headers != null) {
                if (sessionRepository.findByToken(headers).isPresent()) {
                    if (requestBody.containsKey("first_name") || requestBody.containsKey("last_name") || requestBody.containsKey("dob")) {
                        if (requestBody.containsKey("first_name"))
                            personToSave.setFirst_name(requestBody.get("first_name"));
                        if (requestBody.containsKey("last_name"))
                            personToSave.setLast_name(requestBody.get("last_name"));
                        if (requestBody.containsKey("dob"))
                            personToSave.setdob(Date.valueOf(requestBody.get("dob")));

                        personToSave.setId(personId);
                        People savedPerson = peopleRepository.save(personToSave);
                        return new ResponseEntity<>(savedPerson, HttpStatus.valueOf(200));
                    } else
                        return new ResponseEntity<>(personToSave, HttpStatus.valueOf(400));
                } else
                    return new ResponseEntity<>(personToSave, HttpStatus.valueOf(401));
            }
            else
                return new ResponseEntity<>(personToSave, HttpStatus.valueOf(401));
        }
        else {
            personToSave = new People();
            return new ResponseEntity<>(personToSave, HttpStatus.valueOf(404));
        }
    }

    @DeleteMapping("people/{id}")
    public ResponseEntity<People> deletePerson(@PathVariable("id") long personId, @RequestHeader("Authorization") String headers){
        Optional<People> people;
        if (peopleRepository.findById(personId).isPresent()) {
            if (sessionRepository.findByToken(headers).isPresent()) {
                people = peopleRepository.findById(personId);
                peopleRepository.deleteById(personId);
                return new ResponseEntity<>(people.get(), HttpStatus.valueOf(200));
            }
            else
                return new ResponseEntity<>(new People(), HttpStatus.valueOf(401));
        }
        else{
            return new ResponseEntity<>(new People(), HttpStatus.valueOf(404));
        }
    }

    @GetMapping("/people/{id}/audittrail")
    public ResponseEntity<List<AuditTrail>> fetchAuditTrailByPersonId(@PathVariable("id") long id,@RequestHeader("Authorization") String headers) {
        // get the list of cat models from the orm's repository
        List<AuditTrail> auditTrail;
        if (headers != null) {
            if (sessionRepository.findByToken(headers).isPresent()) {
                auditTrail = auditTrailRepository.findBypersonId(id);
                return new ResponseEntity<>(auditTrail, HttpStatus.valueOf(200));
            }
            else{
                auditTrail = new ArrayList<AuditTrail>();
                return new ResponseEntity<>(auditTrail, HttpStatus.valueOf(401));
            }
        }
        else{
            auditTrail = new ArrayList<AuditTrail>();
            return new ResponseEntity<>(auditTrail, HttpStatus.valueOf(401));
        }

    }

    @PostMapping("/people/{id}/audittrail")
    @ResponseBody
    public ResponseEntity<AuditTrail> addAudit(@PathVariable("id") long id, @RequestBody Map<String, String> requestBody, @RequestHeader("Authorization") String headers){
        AuditTrail auditTrail = new AuditTrail();
        People personId= peopleRepository.findById(id).get();
        if (headers != null) {
            if (sessionRepository.findByToken(headers).isPresent()) {
                if (requestBody.containsKey("changed_msg")) {
                    auditTrail.setChanged_msg(requestBody.get("changed_msg"));
                    auditTrail.setPersonId(personId);
                    Session user = sessionRepository.findByToken(headers).get();
                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                    Date date = new Date(System.currentTimeMillis());
                    auditTrail.setWhen(date);
                    auditTrail.setChanged_by(user.getUser());
                    auditTrail.setId((long) 2);
                    AuditTrail temp;
                    try {
                        temp = auditTrailRepository.save(auditTrail);
                        System.out.println(temp.toString());
                    }catch (Exception e){
                        //System.out.println(temp);
                        e.printStackTrace(System.out);
                    }
                    return new ResponseEntity<>(auditTrail, HttpStatus.valueOf(200));
                }
                else
                    return new ResponseEntity<>(auditTrail, HttpStatus.valueOf(400));
            }
            else
                return new ResponseEntity<>(auditTrail, HttpStatus.valueOf(401));
        }
        else
            return new ResponseEntity<>(auditTrail, HttpStatus.valueOf(401));
    }


}
