package com.company.specification;

import org.springframework.data.jpa.domain.Specification;
import com.company.entity.User;
import com.company.form.UserFilterForm;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {

	private UserFilterForm filterForm;

	public UserSpecification(UserFilterForm filterForm) {
		this.filterForm = filterForm;
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

	public static Specification<User> buildWhere(UserFilterForm filterForm) {
		return new UserSpecification(filterForm);
	}
}
