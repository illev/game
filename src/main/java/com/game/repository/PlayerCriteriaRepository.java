package com.game.repository;

import com.game.entity.Player;
import com.game.entity.PlayerSearchCriteria;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlayerCriteriaRepository {
    private final EntityManagerFactory entityManagerFactory;
    private final CriteriaBuilder criteriaBuilder;
    private final EntityManager entityManager;

    public int findPlayersCount(PlayerSearchCriteria searchCriteria) {
        CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
        Root<Player> root = criteriaQuery.from(Player.class);
        Predicate predicate = getPredicates(root, searchCriteria);
        criteriaQuery.select(root).where(predicate);

        TypedQuery<Player> query = entityManager.createQuery(criteriaQuery);
        long playersCount = getPlayersCount(predicate);
        List<Player> queryResult = query.getResultList();
        return (int) playersCount;
    }

    public Page<Player> findAllByFilter(PlayerSearchCriteria searchCriteria) {
        CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
        Root<Player> root = criteriaQuery.from(Player.class);
        Predicate predicate = getPredicates(root, searchCriteria);
        criteriaQuery.select(root).where(predicate);
        setOrder(searchCriteria, criteriaQuery, root);

        TypedQuery<Player> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(searchCriteria.getPageNumber() * searchCriteria.getPageSize());
        query.setMaxResults(searchCriteria.getPageSize());

        Pageable pageable = getPageable(searchCriteria);
        long playersCount = getPlayersCount(predicate);
        List<Player> queryResult = query.getResultList();

        return new PageImpl<>(queryResult, pageable, playersCount);
    }

    private Predicate getPredicates(Root<Player> root, PlayerSearchCriteria searchCriteria) {
        List<Predicate> predicates = new ArrayList<>();
        if (searchCriteria.getName() != null) predicates.add(criteriaBuilder.like(root.get("name"),
                "%" + searchCriteria.getName() + "%"));
        if (searchCriteria.getTitle() != null) predicates.add(criteriaBuilder.like(root.get("title"),
                "%" + searchCriteria.getTitle() + "%"));
        if (searchCriteria.getRace() != null) predicates.add(criteriaBuilder.equal(root.get("race"),
                searchCriteria.getRace()));
        if (searchCriteria.getProfession() != null) predicates.add(criteriaBuilder.equal(root.get("profession"),
                searchCriteria.getProfession()));
        if (searchCriteria.getAfter() != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"),
                searchCriteria.getAfter()));
        if (searchCriteria.getBefore() != null) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("birthday"),
                searchCriteria.getBefore()));
        if (searchCriteria.getBanned() != null) predicates.add(criteriaBuilder.equal(root.get("banned"),
                searchCriteria.getBanned()));
        if (searchCriteria.getMinExperience() != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("experience"),
                searchCriteria.getMinExperience()));
        if (searchCriteria.getMaxExperience() != null) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("experience"),
                searchCriteria.getMaxExperience()));
        if (searchCriteria.getMinLevel() != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("level"),
                searchCriteria.getMinLevel()));
        if (searchCriteria.getMaxLevel() != null) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("level"),
                searchCriteria.getMaxLevel()));


        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(PlayerSearchCriteria searchCriteria,
                          CriteriaQuery<Player> criteriaQuery,
                          Root<Player> root) {
        if (searchCriteria.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(searchCriteria.getOrder().getFieldName())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(searchCriteria.getOrder().getFieldName())));
        }
    }

    public PlayerCriteriaRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    private Pageable getPageable(PlayerSearchCriteria searchCriteria) {
        Sort sort = Sort.by(searchCriteria.getSortDirection(), searchCriteria.getOrder().getFieldName());
        return PageRequest.of(searchCriteria.getPageNumber(), searchCriteria.getPageSize(), sort);
    }

    private long getPlayersCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Player> countRoot = countQuery.from(Player.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
