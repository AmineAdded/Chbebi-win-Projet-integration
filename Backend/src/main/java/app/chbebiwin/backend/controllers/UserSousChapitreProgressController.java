package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.UserSousChapitreProgress.UserSousChapitreProgress;
import app.chbebiwin.backend.entities.UserSousChapitreProgress.UserSousChapitreProgressRequest;
import app.chbebiwin.backend.services.UserSousChapitreProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/UserSousChapitreProgress")
@CrossOrigin(origins = "*")
public class UserSousChapitreProgressController {
    @Autowired
    private UserSousChapitreProgressService userSousChapitreProgressService;
    @PutMapping("/setLastPageRead")
    public UserSousChapitreProgress setPage(@RequestBody UserSousChapitreProgressRequest request){
        return userSousChapitreProgressService.setLastPageRead(request);
    }

    @PostMapping("/getLastReadPage")
    public UserSousChapitreProgress getLastReadPage(@RequestBody UserSousChapitreProgressRequest request){
        return userSousChapitreProgressService.getLastReadPage(request);
    }
    @PostMapping("/get/{userId}")
    public List<UserSousChapitreProgress> getAll(@PathVariable Long userId, @RequestBody List<Long> sousChapitreIds) {
        return userSousChapitreProgressService.findAllByUserIdAndSousChapitreIds(userId, sousChapitreIds);
    }
}
