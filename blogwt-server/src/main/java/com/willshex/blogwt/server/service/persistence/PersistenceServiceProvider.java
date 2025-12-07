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
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.TranslatorFactory;
import com.willshex.blogwt.server.helper.EnvironmentHelper;
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

	public static interface Registrar {
		<T> void register (Class<T> type);
	}

	public static interface TranslatorAdder {
		void add (TranslatorFactory<?, ?> trans);
	}

	@WebFilter(filterName = "ObjectifyFilter", urlPatterns = "/*")
	public static final class PersistenceFilter extends ObjectifyFilter {}

	static {
		registerCoreTranslators();
		registerOtherTranslators();

		registerCoreClasses();
		registerOtherClasses();
	}

	public static Objectify provide () {
		return provide(false);
	}

	public static Objectify provide (boolean shared) {
		EnvironmentHelper.selectNamespace(shared);
		return ObjectifyService.ofy();
	}

	private static void registerCoreTranslators () {
		translator().add(new PermissionTypeTypeTranslatorFactory());
		translator().add(new RelationshipTypeTypeTranslatorFactory());
		translator().add(new ResourceTypeTypeTranslatorFactory());
		translator().add(new NotificationModeTypeTranslatorFactory());
	}

	private static void registerOtherTranslators () {}

	private static void registerCoreClasses () {
		registrar().register(User.class);
		registrar().register(Session.class);
		registrar().register(Post.class);
		registrar().register(Resource.class);
		registrar().register(Permission.class);
		registrar().register(Role.class);
		registrar().register(Property.class);
		registrar().register(PostContent.class);
		registrar().register(Page.class);
		registrar().register(Tag.class);
		registrar().register(ArchiveEntry.class);
		registrar().register(Notification.class);
		registrar().register(MetaNotification.class);
		registrar().register(PushToken.class);
		registrar().register(NotificationSetting.class);
		registrar().register(Relationship.class);
		registrar().register(Rating.class);
		registrar().register(GeneratedDownload.class);
	}

	private static void registerOtherClasses () {}

	private static Registrar registrar () {
		return ObjectifyService.factory()::register;
	}

	private static TranslatorAdder translator () {
		return ObjectifyService.factory().getTranslators()::add;
	}

}
