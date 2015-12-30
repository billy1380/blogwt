//
//  SearchIndexServlet.java
//  blogwt
//
//  Created by billy1380 on 30 Dec 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server;

import java.io.IOException;

import javax.servlet.ServletException;

import com.willshex.service.ContextAwareServlet;

/**
 * @author billy1380
 *
 */
public class SearchIndexServlet extends ContextAwareServlet {

	private static final long serialVersionUID = 7829996840917475240L;

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.service.ContextAwareServlet#doGet() */
	@Override
	protected void doGet () throws ServletException, IOException {
		super.doGet();

		
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.service.ContextAwareServlet#doPost() */
	@Override
	protected void doPost () throws ServletException, IOException {
		doGet();
	}

}
