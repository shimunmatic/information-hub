package de.shimunmatic.informationhub.controller;

import de.shimunmatic.informationhub.model.CountryState;
import de.shimunmatic.informationhub.service.definition.CountryStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://shimunmatic.de"})
@RequestMapping("api/countrystate")
public class CountryStateController {
    private CountryStateService countryStateService;

    @Autowired
    public CountryStateController(CountryStateService countryStateService) {
        this.countryStateService = countryStateService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "all")
    public ResponseEntity<List<CountryState>> getAll() {
        try {
            return ResponseEntity.ok(countryStateService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "all/{processedDateId}")
    public ResponseEntity<List<CountryState>> getCountryAndDate(@PathVariable("processedDateId") Long processedDateId) {
        try {
            return ResponseEntity.ok(countryStateService.getAllForProcessedDate(processedDateId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "country/{countryName}")
    public ResponseEntity<List<CountryState>> getAllForCountry(@PathVariable("countryName") String countryName) {
        try {
            return ResponseEntity.ok(countryStateService.getAllForCountry(countryName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "country/{countryName}/{processedDateId}")
    public ResponseEntity<List<CountryState>> getCountryAndDate(@PathVariable("countryName") String countryName, @PathVariable("processedDateId") Long processedDateId) {
        try {
            return ResponseEntity.ok(countryStateService.getAllForCountryOnDate(countryName, processedDateId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "country")
    public ResponseEntity<List<String>> getCountryNames() {
        try {
            return ResponseEntity.ok(countryStateService.getListOfCountries());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "world")
    public ResponseEntity<List<CountryState>> getAllForWorld() {
        try {
            return ResponseEntity.ok(countryStateService.getAllForWorld());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
