package de.shimunmatic.informationhub.controller;

import de.shimunmatic.informationhub.model.NetDataView;
import de.shimunmatic.informationhub.service.definition.NetDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://shimunmatic.de", "http://18.192.211.250", "https://corona.shimunmatic.de"})
@RequestMapping("api/netdata")
public class NetDataController {
    private final NetDataService netDataService;

    @Autowired
    public NetDataController(NetDataService netDataService) {
        this.netDataService = netDataService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "all/latest")
    public ResponseEntity<List<NetDataView>> getLatestForAll() {
        try {
            return ResponseEntity.ok(netDataService.getLatestForAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "country/{countryName}")
    public ResponseEntity<List<NetDataView>> getAllForCountry(@PathVariable(name = "countryName") String countryName) {
        try {
            return ResponseEntity.ok(netDataService.getAllForCountry(countryName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "country/{countryName}/latest")
    public ResponseEntity<NetDataView> getLatestForCountry(@PathVariable(name = "countryName") String countryName) {
        try {
            return ResponseEntity.ok(netDataService.getLatestForCountry(countryName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
