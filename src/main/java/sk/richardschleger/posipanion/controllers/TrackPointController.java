package sk.richardschleger.posipanion.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sk.richardschleger.posipanion.entities.TrackPoint;
import sk.richardschleger.posipanion.repositories.TrackPointRepository;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/trackpoint")
public class TrackPointController {
    
    @Autowired
    TrackPointRepository trackPointRepository;

    @GetMapping("/")
    public List<TrackPoint> getAllTrackpoints(){
        return trackPointRepository.findAll();
    }

    @PostMapping("/")
    public void saveNewTrackPoint(@RequestBody TrackPoint trackPoint){
        trackPointRepository.save(trackPoint);
    }

}
