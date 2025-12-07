//
//  ResourceHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 29 Aug 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.upload.Upload;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourceHelper {

	public static String url (Resource r, String d) {
		String url = descriptionUrl(r);

		if (url == null) {
			url = dataUrl(r);
		}

		if (url == null) {
			url = d;
		}

		return url;
	}

	public static String dataUrl (Resource r) {
		return r == null || r.data == null || r.data.length() <= 5 ? null
				: Upload.PATH + "?blob-key=" + r.data.substring(5);
	}

	public static String descriptionUrl (Resource r) {
		return r == null || r.description == null
				|| !r.description.contains("Static url:") ? null
						: r.description.split("Static url:")[1];
	}

}
