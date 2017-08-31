//
//  PeristenceService.java
//  blogwt
//
//  Created by William Shakour on 10 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.persistence;

import javax.servlet.annotation.WebFilter;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.Translators;
import com.willshex.blogwt.server.service.persistence.translator.NotificationModeTypeTranslatorFactory;
import com.willshex.blogwt.server.service.persistence.translator.PermissionTypeTypeTranslatorFactory;
import com.willshex.blogwt.server.service.persistence.translator.RelationshipTypeTypeTranslatorFactory;
import com.willshex.blogwt.server.service.persistence.translator.ResourceTypeTypeTranslatorFactory;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.Notification;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.PushToken;
import com.willshex.blogwt.shared.api.datatype.Rating;
import com.willshex.blogwt.shared.api.datatype.Relationship;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.api.datatype.User;

/**
 * @author billy1380
 * 
 */
public class PersistenceServiceProvider {

	@WebFilter(filterName = "ObjectifyFilter", urlPatterns = "/*")
	public static final class PersistenceFilter extends ObjectifyFilter {}

	static {
		Translators translators = factory().getTranslators();
		translators.add(new PermissionTypeTypeTranslatorFactory());
		translators.add(new RelationshipTypeTypeTranslatorFactory());
		translators.add(new ResourceTypeTypeTranslatorFactory());
		translators.add(new NotificationModeTypeTranslatorFactory());

		factory().register(User.class);
		factory().register(Session.class);
		factory().register(Post.class);
		factory().register(Resource.class);
		factory().register(Permission.class);
		factory().register(Role.class);
		factory().register(Property.class);
		factory().register(PostContent.class);
		factory().register(Page.class);
		factory().register(Tag.class);
		factory().register(ArchiveEntry.class);
		factory().register(Notification.class);
		factory().register(MetaNotification.class);
		factory().register(PushToken.class);
		factory().register(NotificationSetting.class);
		factory().register(Relationship.class);
		factory().register(Rating.class);
		factory().register(GeneratedDownload.class);
	}

	public static Objectify provide () {
		return ObjectifyService.ofy();
	}

	private static ObjectifyFactory factory () {
		return ObjectifyService.factory();
	}

}
