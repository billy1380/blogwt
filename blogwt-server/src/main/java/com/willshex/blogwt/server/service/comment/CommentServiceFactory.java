//
//  CommentServiceFactory.java
//  blogwt
//
//  Created by William Shakour on October 24, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.comment;

final class CommentServiceFactory {

	public static ICommentService createNewCommentService () {
		return new CommentService();
	}

}
