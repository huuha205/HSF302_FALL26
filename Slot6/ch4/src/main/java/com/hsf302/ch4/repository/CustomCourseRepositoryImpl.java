package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class CustomCourseRepositoryImpl implements CustomCourseRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Course> findCoursesByDynamicFilter(String namePart, Integer minCredits, String semester) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> query = cb.createQuery(Course.class);
        Root<Course> root = query.from(Course.class);

        List<Predicate> predicates = new ArrayList<>();

        if (namePart != null && !namePart.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + namePart.toLowerCase() + "%"));
        }
        if (minCredits != null) {
            predicates.add(cb.ge(root.get("credits"), minCredits));
        }
        if (semester != null && !semester.isEmpty()) {
            predicates.add(cb.equal(root.get("semester"), semester));
        }

        query.where(predicates.toArray(new Predicate[0]))
             .orderBy(cb.asc(root.get("code")));

        return em.createQuery(query).getResultList();
    }
}
