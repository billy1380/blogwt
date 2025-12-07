//
//  KeylessMultiSelectionModel.java
//  blogwt
//
//  Created by billy1380 on 9 Jul 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.gwt.view.client;

import java.util.function.Predicate;

import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.helper.DataTypeHelper;
import com.willshex.blogwt.shared.util.LongSparseArray;

/**
 * @author billy1380
 *
 */
public class KeylessMultiSelectionModel<T extends DataType>
		extends AbstractSelectionModel<T> {

	private LongSparseArray<T> selection = new LongSparseArray<>();
	private Predicate<T> enabled = null;

	public KeylessMultiSelectionModel (Predicate<T> enabled) {
		super(DataTypeHelper::id);
		this.enabled = enabled;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.SelectionModel#isSelected(java.lang.Object) */
	@Override
	public boolean isSelected (T object) {
		return selection.containsKey(object.id.longValue());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.SelectionModel#setSelected(java.lang.Object,
	 * boolean) */
	@Override
	public void setSelected (T object, boolean selected) {
		if (enabled.test(object)) {
			if (selected) {
				if (selection.containsKey(getSparseKey(object))) {
					setSelected(object, false);
				} else {
					selection.put(getSparseKey(object), object);
					scheduleSelectionChangeEvent();
				}
			} else {
				selection.remove(((Long) this.getKey(object)).longValue());
				scheduleSelectionChangeEvent();
			}
		}
	}

	public LongSparseArray<T> getSelectionObjects () {
		return selection;
	}

	public void clear () {
		selection.clear();
	}

	private long getSparseKey (T object) {
		return ((Long) this.getKey(object)).longValue();
	}

}
