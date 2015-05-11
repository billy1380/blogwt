//
//  PagePlanFinishedHandler.java
//  blogwt
//
//  Created by billy1380 on 2 Aug 2013.
//  Copyright © 2013 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.wizard;

import java.util.List;

/**
 * @author billy1380
 * 
 */
public interface PagePlanFinishedHandler {

	void onfinished(List<WizardPage<?>> pages);

}
