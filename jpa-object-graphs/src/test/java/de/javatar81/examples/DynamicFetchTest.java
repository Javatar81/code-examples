package de.javatar81.examples;

import java.util.Collections;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.ErrorMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.javatar81.examples.config.Config;
import de.javatar81.examples.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Config.class })
@Sql(scripts = {
		"classpath:testdata.sql" }, config = @SqlConfig(commentPrefix = "`", separator = "\n", errorMode = ErrorMode.FAIL_ON_ERROR) )
public class DynamicFetchTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Test(expected = LazyInitializationException.class)
	public void testFailLazyAccessByExplicitJoinInJPAQL() {
		TypedQuery<User> loadUserQuery = entityManager.createQuery(
				"select usr from User usr left outer join usr.cars cars where cars.licensePlate = :licensePlate",
				User.class);
		loadUserQuery.setParameter("licensePlate", "HIBERNATE");
		User user = loadUserQuery.getSingleResult();
		user.getCars().stream().forEach(System.out::println);
	}

	@Test
	public void testInitByExplicitFetchJoinInJPAQL() {
		TypedQuery<User> loadUserQuery = entityManager
				.createQuery("select usr from User usr left outer join fetch usr.cars where usr.id = :id", User.class);
		loadUserQuery.setParameter("id", 1L);
		User user = loadUserQuery.getSingleResult();
		user.getCars().stream().forEach(System.out::println);
	}

	@Test
	public void testInitByExplicitFetchJoinInJPACriteria() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		root.fetch("cars", JoinType.LEFT).fetch("manufacturer", JoinType.LEFT);
		CriteriaQuery<User> criteriaQuery = query.select(root).where(builder.and(builder.equal(root.get("id"), 1L)));
		User user = entityManager.createQuery(criteriaQuery).getSingleResult();
		user.getCars().stream().forEach(System.out::println);
	}

	@Test
	public void testInitCarsByEntityGraph() {
		User user = entityManager.find(User.class, 1L,
				Collections.singletonMap("javax.persistence.fetchgraph", entityManager.getEntityGraph("user.cars")));
		user.getCars().stream().forEach(System.out::println);
	}

	@Test(expected = LazyInitializationException.class)
	public void testFailLazyInitManufaturersByEntityGraph() {
		User user = entityManager.find(User.class, 1L,
				Collections.singletonMap("javax.persistence.fetchgraph", entityManager.getEntityGraph("user.cars")));
		user.getCars().stream().forEach(car -> System.out.println(car.getManufacturer()));
	}

	@Test
	public void testInitManufaturersByEntityGraph() {
		User user = entityManager.find(User.class, 1L, Collections.singletonMap("javax.persistence.fetchgraph",
				entityManager.getEntityGraph("user.cars.manufacturers")));
		user.getCars().stream().forEach(car -> System.out.println(car.getManufacturer()));
	}

	@Test
	public void testInitByNamedEntityGraphInJPAQL() {
		TypedQuery<User> loadUserQuery = entityManager.createQuery("select usr from User usr where usr.id = :id",
				User.class);
		loadUserQuery.setParameter("id", 1L);
		loadUserQuery.setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("user.cars"));
		User user = loadUserQuery.getSingleResult();
		user.getCars().stream().forEach(System.out::println);
	}

	@Test
	public void testInitByNamedEntityGraphInJPACriteria() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		CriteriaQuery<User> criteriaQuery = query.select(root).where(builder.and(builder.equal(root.get("id"), 1L)));
		User user = entityManager.createQuery(criteriaQuery)
				.setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("user.cars")).getSingleResult();
		user.getCars().stream().forEach(System.out::println);
	}

	@Test
	public void testInitDynamicEntityGraph() {
		EntityGraph<User> graph = entityManager.createEntityGraph(User.class);
		graph.addAttributeNodes("cars");
		User user = entityManager.find(User.class, 1L, Collections.singletonMap("javax.persistence.fetchgraph", graph));
		user.getCars().stream().forEach(System.out::println);
	}

}
