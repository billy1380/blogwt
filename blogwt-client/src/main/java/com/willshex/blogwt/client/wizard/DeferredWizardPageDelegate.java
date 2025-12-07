//
//  DeferredWizardPageDelegate.java
//  codegen
//
//  Created by William Shakour (billy1380) on 14 Aug 2013.
//  Copyright © 2013 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.client.wizard;

import java.util.List;

/**
 * @author William Shakour (billy1380)
 * 
 */
public interface DeferredWizardPageDelegate {

	/**
	 * @param pages
	 * @param current
	 * @return
	 */
	List<WizardPage<?>> getPages (List<WizardPage<?>> pages, int current);

}
