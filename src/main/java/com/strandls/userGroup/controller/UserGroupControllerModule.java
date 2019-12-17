/**
 * 
 */
package com.strandls.userGroup.controller;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupControllerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserGroupController.class).in(Scopes.SINGLETON);
	}

}
