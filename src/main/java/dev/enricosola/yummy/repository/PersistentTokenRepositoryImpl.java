package dev.enricosola.yummy.repository;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.CriteriaBuilder;
import dev.enricosola.yummy.entity.PersistentToken;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.stereotype.Repository;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.Date;

@Repository
@Transactional
public class PersistentTokenRepositoryImpl implements PersistentTokenRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private PersistentToken lookupBySeries(String series){
        try{
            CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<PersistentToken> query = criteriaBuilder.createQuery(PersistentToken.class);
            CriteriaQuery<PersistentToken> filter = query.where(criteriaBuilder.equal(query.from(PersistentToken.class).get("series"), series));
            return this.entityManager.createQuery(filter).getSingleResult();
        }catch(NoResultException exception){
            return null;
        }
    }

    private void flushAndClear(){
        this.entityManager.flush();
        this.entityManager.clear();
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token){
        PersistentToken persistentToken = new PersistentToken();
        persistentToken.setTokenValue(token.getTokenValue());
        persistentToken.setUsername(token.getUsername());
        persistentToken.setSeries(token.getSeries());
        persistentToken.setDate(token.getDate());
        this.entityManager.persist(persistentToken);
        this.flushAndClear();
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed){
        PersistentToken persistentToken = this.lookupBySeries(series);
        if ( persistentToken != null ){
            persistentToken.setTokenValue(tokenValue);
            persistentToken.setDate(lastUsed);
            persistentToken.setSeries(series);
            this.entityManager.merge(persistentToken);
            this.flushAndClear();
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId){
        PersistentToken persistentToken = this.lookupBySeries(seriesId);
        return persistentToken == null ? null : new PersistentRememberMeToken(
            persistentToken.getUsername(),
            persistentToken.getSeries(),
            persistentToken.getTokenValue(),
            persistentToken.getDate()
        );
    }

    @Override
    public void removeUserTokens(String username){
        String JPQL = "DELETE FROM PersistentToken WHERE username = :username";
        Query query = this.entityManager.createQuery(JPQL);
        query.setParameter("username", username);
        query.executeUpdate();
    }
}
