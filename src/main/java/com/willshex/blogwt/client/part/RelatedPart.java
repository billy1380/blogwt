//
//  RelatedPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RelatedPart extends Composite {

	private static RelatedPartUiBinder uiBinder = GWT
			.create(RelatedPartUiBinder.class);

	interface RelatedPartUiBinder extends UiBinder<Widget, RelatedPart> {}

	public RelatedPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
