package com.spike.springdata.jpa.support.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PrePersist;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import com.spike.springdata.jpa.domain.Address;
import com.spike.springdata.jpa.support.spring.ApplicationContextProvider;

@Component
public class EntityEventListener {

	private static final Logger LOG = Logger.getLogger(EntityEventListener.class);

	@Autowired
	private EntityManagerFactory emf;

	/**
	 * usage of {@link @PrePersist}
	 * 
	 * @param address
	 */
	@PrePersist
	public void addAddress(final Address address) {
		LOG.info(System.lineSeparator() + "try to persistent address: " + address);

		TransactionSynchronizationManager.registerSynchronization(//
				new TransactionSynchronizationAdapter() {

					@Override
					public void afterCommit() {
						doSomething(address);
					}
				});
	}

	private void doSomething(Address address) {
		LOG.info(System.lineSeparator() + " do something in entity event callback");

		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();

		Assert.notNull(appContext);
	}

}
