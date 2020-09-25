package de.shimunmatic.informationhub.controller;

import de.shimunmatic.informationhub.model.ProcessedDate;
import de.shimunmatic.informationhub.service.definition.ProcessedDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://shimunmatic.de", "http://18.192.211.250", "https://corona.shimunmatic.de"})
@RequestMapping("api/processeddate")
public class ProcessedDateController {
    private ProcessedDateService processedDateService;

    @Autowired
    public ProcessedDateController(ProcessedDateService processedDateService) {
        this.processedDateService = processedDateService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "all")
    public ResponseEntity<List<ProcessedDate>> getAll() {
        try {
            return ResponseEntity.ok(processedDateService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
