package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Artist;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.repositories.ArtistRepository;
import ru.iu3.backend.repositories.CountryRepository;


import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class ArtistController {
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    CountryRepository countryRepository;


    @GetMapping("/artists")
    public List
    getAllartists() {
        return artistRepository.findAll();
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@RequestBody Artist artist)
            throws Exception {
        try {
            Optional<Country>
                    cc = countryRepository.findById(artist.country.id);
            if (cc.isPresent()) {
                artist.country = cc.get();
            }
            Artist nc = artistRepository.save(artist);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("artists.name_UNIQUE"))
                error = "artistalreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map =  new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
        }
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable(value = "id") Long ArtistId,
                                                 @RequestBody Artist ArtistDetails) {
        Artist Artist = null;
        Optional<Artist>
                cc = artistRepository.findById(ArtistId);
        if (cc.isPresent()) {
            Artist = cc.get();
            Artist.name = ArtistDetails.name;
            artistRepository.save(Artist);
            return ResponseEntity.ok(Artist);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
        }
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable(value = "id") Long ArtistId) {
        Optional<Artist>
                Artist = artistRepository.findById(ArtistId);
        Map<String, Boolean>
                resp = new HashMap<>();
        if (Artist.isPresent()) {
            artistRepository.delete(Artist.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
    
}