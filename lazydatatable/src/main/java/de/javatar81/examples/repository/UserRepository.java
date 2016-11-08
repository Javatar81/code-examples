package de.javatar81.examples.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import de.javatar81.examples.domain.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User>{
	public Page<User> findByLogin(String login, Pageable pageable);
}
