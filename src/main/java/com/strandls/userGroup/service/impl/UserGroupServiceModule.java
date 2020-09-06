/**
 * 
 */
package com.strandls.userGroup.service.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.strandls.userGroup.service.CustomFieldServices;
import com.strandls.userGroup.service.NewsletterSerivce;
import com.strandls.userGroup.service.UserGroupFilterService;
import com.strandls.userGroup.service.UserGroupMemberService;
import com.strandls.userGroup.service.UserGroupSerivce;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserGroupSerivce.class).to(UserGroupServiceImpl.class).in(Scopes.SINGLETON);
		bind(CustomFieldServices.class).to(CustomFieldServiceImpl.class).in(Scopes.SINGLETON);
		bind(LogActivities.class).in(Scopes.SINGLETON);
		bind(CustomFieldMigrationThread.class).in(Scopes.SINGLETON);
		bind(RabbitMQProducer.class).in(Scopes.SINGLETON);
		bind(NewsletterSerivce.class).to(NewsletterServiceImpl.class).in(Scopes.SINGLETON);
		bind(EncryptionUtils.class).in(Scopes.SINGLETON);
		bind(UserGroupFilterService.class).to(UserGroupFilterServiceImpl.class).in(Scopes.SINGLETON);
		bind(MailUtils.class).in(Scopes.SINGLETON);
		bind(UserGroupMemberService.class).to(UserGroupMemberServiceImpl.class).in(Scopes.SINGLETON);
	}
}
