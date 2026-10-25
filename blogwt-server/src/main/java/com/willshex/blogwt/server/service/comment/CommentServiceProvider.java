//
//  CommentServiceProvider.java
//  blogwt
//
//  Created by William Shakour on October 24, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.comment;

import com.willshex.service.ServiceDiscovery;

final public class CommentServiceProvider {

	public static ICommentService provide () {
		ICommentService commentService = null;

		if ((commentService = (ICommentService) ServiceDiscovery
				.getService(ICommentService.NAME)) == null) {
			commentService = CommentServiceFactory.createNewCommentService();
			ServiceDiscovery.registerService(commentService);
		}

		return commentService;
	}

}
