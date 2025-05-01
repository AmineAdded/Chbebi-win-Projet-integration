package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.UserSousChapitreProgress;
import app.chbebiwin.backend.repositories.UserSousChapitreProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSousChapitreProgressService {
    @Autowired
    private UserSousChapitreProgressRepository userSousChapitreProgressRepository;

    public UserSousChapitreProgress getLastReadPage(Long userId, Long sousChapitreId) {
        return userSousChapitreProgressRepository.findByUtilisateurIdAndSousChapitreId(userId, sousChapitreId);
    }
//    public UserSousChapitreProgress setLastPageRead(Long userId, Long sousChapitreId) {
//
//    }
}
