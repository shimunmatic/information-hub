package de.shimunmatic.informationhub.controller;

import de.shimunmatic.informationhub.service.definition.AdminService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "https://shimunmatic.de"})
@RequestMapping("api/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping("deleteProcessedDate/{processedDate}")
    public ResponseEntity removeAllForPorcessedDate(@PathVariable("processedDate") String processedDate){
        try{
            log.info("removeAllForPorcessedDate: {}", processedDate);
            adminService.deleteAllForProcessedDate(processedDate);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("Error while removeAllForPorcessedDate", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping("forcefetch")
    public ResponseEntity forceFetch(){
        try{
            log.info("forceFetch");
            adminService.forceFetch();
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("Error while forceFetch", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping("cache/evict/daily")
    public ResponseEntity evictDailyCache(){
        try{
            log.info("evictDailyCache");
            adminService.forceRemoveCacheForDailyUpdate();
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("Error while evictDailyCache", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping("cache/evict/all")
    public ResponseEntity evictAllCache(){
        try{
            log.info("evictAllCache");
            adminService.forceRemoveAllCache();
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("Error while evictAllCache", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
