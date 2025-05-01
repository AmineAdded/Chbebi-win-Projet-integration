package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.UserSousChapitreProgress.UserSousChapitreProgress;
import app.chbebiwin.backend.entities.UserSousChapitreProgress.UserSousChapitreProgressRequest;
import app.chbebiwin.backend.repositories.UserSousChapitreProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSousChapitreProgressService {
    @Autowired
    private UserSousChapitreProgressRepository userSousChapitreProgressRepository;

    public UserSousChapitreProgress getLastReadPage( UserSousChapitreProgressRequest request) {
        return userSousChapitreProgressRepository.findByUtilisateurIdAndSousChapitreId(request.getUserId(), request.getSousChapitreId());
    }
    public UserSousChapitreProgress setLastPageRead(UserSousChapitreProgressRequest request) {
        UserSousChapitreProgress u = userSousChapitreProgressRepository.findByUtilisateurIdAndSousChapitreId(request.getUserId(),request.getSousChapitreId());
        u.setLastPageRead(request.getLastPageRead());
        u.setPourcentage(request.getPourcentage());
        userSousChapitreProgressRepository.save(u);

        return u;
    }
    public List<UserSousChapitreProgress> findAllByUserIdAndSousChapitreIds(Long userId, List<Long> sousChapitreIds) {
        return userSousChapitreProgressRepository.findAllByUtilisateurIdAndSousChapitreIdIn(userId, sousChapitreIds);
    }
}
