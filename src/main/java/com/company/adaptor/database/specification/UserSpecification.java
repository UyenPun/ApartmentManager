package com.company.adaptor.database.specification;

import com.company.adaptor.database.form.UserFilterForm;
import com.company.domain.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification implements Specification<User> {

    private final UserFilterForm filterForm;

    public UserSpecification(UserFilterForm filterForm) {
        this.filterForm = filterForm;
    }

    public static Specification<User> buildWhere(UserFilterForm filterForm) {
        return new UserSpecification(filterForm);
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (filterForm.getUsername() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("username"), filterForm.getUsername()));
        }

        if (filterForm.getRole() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("role"), filterForm.getRole()));
        }

        return predicate;
    }
}
