/**
 * 
 */
package com.strandls.userGroup.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupDaoModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserGroupDao.class).in(Scopes.SINGLETON);
		bind(UserGroupObservationDao.class).in(Scopes.SINGLETON);
		bind(FeaturedDao.class).in(Scopes.SINGLETON);
	}

}
