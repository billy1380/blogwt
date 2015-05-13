//  
//  PropertyServiceFactory.java
//  blogwt
//
//  Created by William Shakour on May 13, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.property;

/**
 * 
 * @author William Shakour (billy1380)
 *
 */
final class PropertyServiceFactory {

	/**
	* @return
	*/
	public static IPropertyService createNewPropertyService () {
		return new PropertyService();
	}

}