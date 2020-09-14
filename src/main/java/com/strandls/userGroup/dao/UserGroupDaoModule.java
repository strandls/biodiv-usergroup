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
		bind(NewsletterDao.class).in(Scopes.SINGLETON);
		bind(UserGroupInvitaionDao.class).in(Scopes.SINGLETON);
		bind(UserGroupFilterRuleDao.class).in(Scopes.SINGLETON);
		bind(UserGroupSpatialDataDao.class).in(Scopes.SINGLETON);
		bind(UserGroupTaxonomicRuleDao.class).in(Scopes.SINGLETON);
		bind(UserGroupCreatedOnDateRuleDao.class).in(Scopes.SINGLETON);
		bind(UserGroupObservedOnDateRuleDao.class).in(Scopes.SINGLETON);
		bind(StatsDao.class).in(Scopes.SINGLETON);
		bind(UserGroupHabitatDao.class).in(Scopes.SINGLETON);
		bind(UserGroupJoinRequestDao.class).in(Scopes.SINGLETON);
		bind(UserGroupMemberRoleDao.class).in(Scopes.SINGLETON);
		bind(GroupGallerySliderDao.class).in(Scopes.SINGLETON);
	}

}
