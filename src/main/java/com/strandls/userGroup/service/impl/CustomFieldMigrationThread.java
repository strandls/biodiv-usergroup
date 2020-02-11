/**
 * 
 */
package com.strandls.userGroup.service.impl;

import com.google.inject.Inject;
import com.strandls.userGroup.service.UserGroupSerivce;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldMigrationThread implements Runnable {

	@Inject
	private UserGroupSerivce ugService;

	@Override
	public void run() {
		ugService.migrateCustomField();
	}

}
