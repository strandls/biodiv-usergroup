package com.strandls.userGroup.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NewsletterArrayList extends ArrayList<NewsletterWithParentChildRelationship> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9204312711077181561L;

	@Override
	public boolean add(NewsletterWithParentChildRelationship arg0) {
		int index = Collections.binarySearch(this, arg0, new Comparator<NewsletterWithParentChildRelationship>() {
			@Override
			public int compare(NewsletterWithParentChildRelationship o1,
					NewsletterWithParentChildRelationship o2) {
				return o2.getDisplayOrder().compareTo(o1.getDisplayOrder());
			}
		});

		index = index < 0 ? ~index : index;
		super.add(index, arg0);
		return true;
	}
}
