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
		bind(CustomFieldDao.class).in(Scopes.SINGLETON);
		bind(CustomFieldsDao.class).in(Scopes.SINGLETON);
		bind(CustomFieldUG18Dao.class).in(Scopes.SINGLETON);
		bind(CustomFieldUG37Dao.class).in(Scopes.SINGLETON);
		bind(CustomFieldValuesDao.class).in(Scopes.SINGLETON);
		bind(UserGroupCustomFieldMappingDao.class).in(Scopes.SINGLETON);
		bind(ObservationCustomFieldDao.class).in(Scopes.SINGLETON);

	}

}
