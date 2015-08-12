//
//  DisqusComments.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 12 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class DisqusComments extends Composite {

	private static DisqusCommentsUiBinder uiBinder = GWT
			.create(DisqusCommentsUiBinder.class);

	interface DisqusCommentsUiBinder extends UiBinder<Widget, DisqusComments> {}

	private String identifier;
	private String url;
	private String title;
	private String tag;

	private String categoryId;
	private String disqusId;

	private Element elDisqusThread = null;
	private boolean installed = false;
	private Timer refreshTimer = (new Timer() {
		@Override
		public void run () {
			if (elDisqusThread == null) {
				elDisqusThread = Document.get().getElementById("disqus_thread");
			}

			if (elDisqusThread != null) {
				if (!installed) {
					installDisqus(disqusId, categoryId, identifier, url, title,
							tag);
					installed = true;
				} else {
					resetDisqus(identifier, url, title, tag);
				}

				this.cancel();
			}
		}
	});

	public DisqusComments (String categoryId, String disqusId) {
		this.categoryId = categoryId;
		this.disqusId = disqusId;

		initWidget(uiBinder.createAndBindUi(this));
	}

	private static native void installDisqus (String discusShortName,
			String categoryId, String postId, String url, String title,
			String category) /*-{
								$wnd.disqus_shortname = discusShortName;

								// $wnd.disqus_identifier = postId;
								$wnd.disqus_url = url;
								$wnd.disqus_title = title;
								$wnd.disqus_category_id = categoryId;

								($wnd.installDisqus = function() {
								var dsq = $wnd.document.createElement('script');
								dsq.type = 'text/javascript';
								dsq.async = true;
								dsq.src = '//' + $wnd.disqus_shortname + '.disqus.com/embed.js';
								($wnd.document.getElementsByTagName('head')[0] || $wnd.document
								.getElementsByTagName('body')[0]).appendChild(dsq);
								})();

								$wnd.reset = function(resetPostId, resetUrl, resetTitle, categoryId) {
								$wnd.DISQUS.reset({
								reload : true,
								config : function() {
								// this.page.identifier = resetPostId;
								this.page.url = resetUrl;
								this.page.title = resetTitle;
								this.page.category_id = categoryId;
								}
								});
								};
								}-*/;

	private static native void resetDisqus (String postId, String url,
			String title, String categoryId) /*-{
												$wnd.reset(postId, url, title, categoryId);
												}-*/;

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier (String identifier) {
		this.identifier = identifier;
		refresh();
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl (String url) {
		this.url = url;
		refresh();
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle (String title) {
		this.title = title;
		refresh();
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag (String tag) {
		this.tag = tag;
		refresh();
	}

	public void refresh () {
		if (refreshTimer.isRunning()) {
			refreshTimer.cancel();
		}

		refreshTimer.scheduleRepeating(100);
	}

}
