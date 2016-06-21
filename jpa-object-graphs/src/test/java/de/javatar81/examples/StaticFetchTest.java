package de.javatar81.examples;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.ErrorMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.javatar81.examples.config.Config;
import de.javatar81.examples.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Config.class })
@Sql(scripts = {
		"classpath:testdata.sql" }, config = @SqlConfig(commentPrefix = "`", separator = "\n", errorMode = ErrorMode.FAIL_ON_ERROR) )
public class StaticFetchTest {

	@PersistenceContext
	private EntityManager entityManager;

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManagerExtended;

	@Test(expected = LazyInitializationException.class)
	public void testFailLazyAccess() {
		User user = entityManager.find(User.class, 1L);
		assertNotNull(user);
		// Lazy init exception cars is mapped as lazy
		user.getCars().stream().forEach(System.out::println);
	}

	@Test(expected = HibernateException.class)
	public void testFailLazyHibernateInitialize() {
		User user = entityManager.find(User.class, 1L);
		assertNotNull(user);
		// Hibernate exception cars is mapped as lazy and not associated with
		// session
		Hibernate.initialize(user.getCars());
	}

	@Test
	@Transactional(readOnly = true)
	public void testLoadInTx() {
		User user = entityManager.find(User.class, 1L);
		assertNotNull(user);
		// Cars are loaded on demand because entity manager is still open
		// because of the transaction
		user.getCars().stream().forEach(System.out::println);
	}

	@Test
	public void testLoadExtended() {
		User user = entityManagerExtended.find(User.class, 1L);
		assertNotNull(user);
		// Cars are loaded on demand because entity manager is extended
		user.getCars().stream().forEach(System.out::println);
	}

	@Test
	public void testLoadEager() {
		User user = entityManager.find(User.class, 1L);
		assertNotNull(user);
		// Logins are loaded because it is mapped eager
		user.getLogins().stream().forEach(System.out::println);
	}

}
