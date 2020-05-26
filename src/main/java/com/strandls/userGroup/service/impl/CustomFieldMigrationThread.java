/**
 * 
 */
package com.strandls.userGroup.service.impl;

import javax.inject.Inject;

import com.strandls.userGroup.service.CustomFieldServices;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldMigrationThread implements Runnable {

	@Inject
	private CustomFieldServices cfService;

	@Override
	public void run() {
		cfService.migrateCustomField();
	}

}
