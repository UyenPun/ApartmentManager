package com.company.specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.company.entity.Apartment;
import com.company.form.ApartmentFilterForm;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class ApartmentSpecification {

	public static Specification<Apartment> buildWhere(ApartmentFilterForm filterForm) {
		Specification<Apartment> where = null;

		if (filterForm == null)
			return where;

		if (!StringUtils.isEmpty(filterForm.getSearch())) {
			String search = filterForm.getSearch().trim();
			CustomSpecification searchSpec = new CustomSpecification("search", search);
			where = Specification.where(searchSpec);
		}

		if (filterForm.getMinArea() != null) {
			CustomSpecification minAreaSpec = new CustomSpecification("minArea", filterForm.getMinArea());
			if (where == null) {
				where = minAreaSpec;
			} else {
				where = where.and(minAreaSpec);
			}
		}

		if (filterForm.getMaxArea() != null) {
			CustomSpecification maxAreaSpec = new CustomSpecification("maxArea", filterForm.getMaxArea());
			if (where == null) {
				where = maxAreaSpec;
			} else {
				where = where.and(maxAreaSpec);
			}
		}

		if (filterForm.getMinRooms() != null) {
			CustomSpecification minRoomsSpec = new CustomSpecification("minRooms", filterForm.getMinRooms());
			if (where == null) {
				where = minRoomsSpec;
			} else {
				where = where.and(minRoomsSpec);
			}
		}

		if (filterForm.getMaxRooms() != null) {
			CustomSpecification maxRoomsSpec = new CustomSpecification("maxRooms", filterForm.getMaxRooms());
			if (where == null) {
				where = maxRoomsSpec;
			} else {
				where = where.and(maxRoomsSpec);
			}
		}

		if (filterForm.getMinCreatedDate() != null) {
			CustomSpecification minCreatedDateSpec = new CustomSpecification("minCreatedDate",
					filterForm.getMinCreatedDate());
			if (where == null) {
				where = minCreatedDateSpec;
			} else {
				where = where.and(minCreatedDateSpec);
			}
		}

		if (filterForm.getMaxCreatedDate() != null) {
			CustomSpecification maxCreatedDateSpec = new CustomSpecification("maxCreatedDate",
					filterForm.getMaxCreatedDate());
			if (where == null) {
				where = maxCreatedDateSpec;
			} else {
				where = where.and(maxCreatedDateSpec);
			}
		}

		if (filterForm.getMinYear() != null) {
			CustomSpecification minYearSpec = new CustomSpecification("minYear", filterForm.getMinYear());
			if (where == null) {
				where = minYearSpec;
			} else {
				where = where.and(minYearSpec);
			}
		}

		return where;
	}

	@RequiredArgsConstructor
	static class CustomSpecification implements Specification<Apartment> {

		@NonNull
		private final String field;

		@NonNull
		private final Object value;

		@Override
		public Predicate toPredicate(Root<Apartment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			switch (field) {
			case "search":
				// Sử dụng join để tìm kiếm theo tên cư dân hoặc số căn hộ
				Predicate apartmentNumberPredicate = criteriaBuilder.like(root.get("apartmentNumber"),
						"%" + value.toString() + "%");
				Predicate residentNamePredicate = criteriaBuilder.like(root.join("residents").get("name"),
						"%" + value.toString() + "%");
				return criteriaBuilder.or(apartmentNumberPredicate, residentNamePredicate);

			case "minArea":
				return criteriaBuilder.greaterThanOrEqualTo(root.get("area"), (Float) value);

			case "maxArea":
				return criteriaBuilder.lessThanOrEqualTo(root.get("area"), (Float) value);

			case "minRooms":
				return criteriaBuilder.greaterThanOrEqualTo(root.get("numRooms"), (Integer) value);

			case "maxRooms":
				return criteriaBuilder.lessThanOrEqualTo(root.get("numRooms"), (Integer) value);

			case "minCreatedDate":
				return criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate").as(Date.class), (Date) value);

			case "maxCreatedDate":
				return criteriaBuilder.lessThanOrEqualTo(root.get("createdDate").as(Date.class), (Date) value);

			case "minYear":
				return criteriaBuilder.greaterThanOrEqualTo(
						criteriaBuilder.function("YEAR", Integer.class, root.get("createdDate")), (Integer) value);

			default:
				return null;
			}
		}
	}
}
