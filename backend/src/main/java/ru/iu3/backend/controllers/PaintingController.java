package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Painting;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.repositories.PaintingRepository;
import ru.iu3.backend.repositories.CountryRepository;


import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class PaintingController {
    @Autowired
    PaintingRepository paintingRepository;
    CountryRepository countryRepository;


    @GetMapping("/paintings")
    public List
    getAllPaintings() {
        return paintingRepository.findAll();
    }

    @PostMapping("/paintings")
    public ResponseEntity<Object> createPainting(@RequestBody Painting painting)
            throws Exception {
        try {
            Painting nc = paintingRepository.save(painting);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("Paintings.name_UNIQUE"))
                error = "paintingalreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map =  new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
        }
    }

    @PutMapping("/paintings/{id}")
    public ResponseEntity<Painting> updatePainting(@PathVariable(value = "id") Long PaintingId,
                                               @RequestBody Painting PaintingDetails) {
        Painting painting;
        Optional<Painting>
                cc = paintingRepository.findById(PaintingId);
        if (cc.isPresent()) {
            painting = cc.get();
            painting.name = PaintingDetails.name;
            painting.year = PaintingDetails.year;
            paintingRepository.save(painting);
            return ResponseEntity.ok(painting);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Painting not found");
        }
    }

    @DeleteMapping("/paintings/{id}")
    public ResponseEntity<Object> deletePainting(@PathVariable(value = "id") Long PaintingId) {
        Optional<Painting>
                painting = paintingRepository.findById(PaintingId);
        Map<String, Boolean>
                resp = new HashMap<>();
        if (painting.isPresent()) {
            paintingRepository.delete(painting.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }

}