package de.javatar81.examples.service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import de.javatar81.examples.domain.User;
import de.javatar81.examples.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public Page<User> findByFilter(Map<String, String> filters, Pageable pageable) {
		return userRepository.findAll(getFilterSpecification(filters), pageable);
	}
	
	@Transactional
	public void create(User user) {
		userRepository.save(user);
	}

	private Specification<User> getFilterSpecification(Map<String, String> filterValues) {
		return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			Optional<Predicate> predicate = filterValues.entrySet().stream()
					.filter(v -> v.getValue() != null && v.getValue().length() > 0)
					.map(entry -> {
						Path<?> path = root;
						String key = entry.getKey();
						if (entry.getKey().contains(".")) {
							String[] splitKey = entry.getKey().split("\\.");
							path = root.join(splitKey[0]);
							key = splitKey[1];
						}
						return builder.like(path.get(key).as(String.class), "%" + entry.getValue() + "%");
					})
					.collect(Collectors.reducing((a, b) -> builder.and(a, b)));
			return predicate.orElseGet(() -> alwaysTrue(builder));
		};
	}

	private Predicate alwaysTrue(CriteriaBuilder builder) {
		return builder.isTrue(builder.literal(true));
	}

}
