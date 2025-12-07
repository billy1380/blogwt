//
//  ResaveServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 10 May 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.background.resave;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.background.resave.input.QueueForResavingAction;
import com.willshex.blogwt.server.background.resave.input.ResaveAction;
import com.willshex.blogwt.server.dev.DevServlet.AllPaged;
import com.willshex.blogwt.server.helper.QueueHelper;
import com.willshex.blogwt.server.helper.QueueHelper.HasQueueAction;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.helper.DataTypeHelper;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.server.ServiceException;
import com.willshex.server.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */

@WebServlet(name = "Resave", description = "Resaves data after attribute changes", urlPatterns = {
		ResaveServlet.URL, ResaveServlet.URL + "/*" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = "admin"))
public class ResaveServlet extends ContextAwareServlet
		implements HasQueueAction {

	private static final class Resaver<T extends DataType, E extends Enum<E>> {
		public final AllPaged<T, E> pagedSupplier;
		public final Function<Iterable<Long>, List<T>> batchSupplier;
		public final Consumer<T> processor;

		@SuppressWarnings("unused")
		Resaver(Function<Iterable<Long>, List<T>> batchSupplier,
				AllPaged<T, E> pagedSupplier, Consumer<T> processor) {
			this.batchSupplier = batchSupplier;
			this.pagedSupplier = pagedSupplier;
			this.processor = processor;
		}
	}

	private static final Logger LOG = Logger
			.getLogger(ResaveServlet.class.getName());

	public static final String URL = "/resave";

	private static final String QUEUE_FOR_RESAVING_ACTION = "QueueForResaving";
	private static final String RESAVE_ACTION = "Resave";

	private static final Integer AT_A_TIME = Integer.valueOf(120);

	private static final Map<String, Resaver<? extends DataType, ?>> RESAVERS = new HashMap<>();

	static {
		// RESAVERS.put();
	}
	@Override
	protected void doPost() throws ServletException, IOException {
		doGet();
	}
	@Override
	protected void doGet() throws ServletException, IOException {
		super.doGet();

		try {
			QueueHelper.processGet(this);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	@Override
	public void processAction(String action, JsonObject json)
			throws Exception {
		if (QUEUE_FOR_RESAVING_ACTION.equals(action)) {
			QueueForResavingAction input = new QueueForResavingAction();
			input.fromJson(json);
			processQueueForResaving(input);
		} else if (RESAVE_ACTION.equals(action)) {
			ResaveAction input = new ResaveAction();
			input.fromJson(json);
			processResave(input);
		}
	}

	private void processQueueForResaving(QueueForResavingAction input)
			throws ServiceException {
		Resaver<? extends DataType, ?> resaver = RESAVERS.get(input.typeName);

		if (resaver == null) {
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine(
						"Resaver for type [" + input.typeName + "] not found.");
			}
		} else {
			List<? extends DataType> data = resaver.pagedSupplier
					.get(input.pager.start, input.pager.count, null, null);

			if (data == null || data.isEmpty()) {
				if (LOG.isLoggable(Level.FINE)) {
					LOG.fine("No more [" + input.typeName + "] data found at ["
							+ input.pager.start + "]");
				}
			} else {
				resave(input.typeName, data, input.pager);
			}
		}
	}

	private void processResave(ResaveAction input) throws ServiceException {
		@SuppressWarnings("unchecked")
		Resaver<DataType, ?> r = (Resaver<DataType, ?>) RESAVERS
				.get(input.typeName);

		List<DataType> data = r.batchSupplier.apply(input.data.stream()
				.map(DataTypeHelper::id).collect(Collectors.toList()));
		Consumer<DataType> p = r.processor;

		if (data != null) {
			data.forEach(d -> p.accept(d));
		}

		if (input.pager != null) {
			if (AT_A_TIME.intValue() != input.pager.count.intValue()) {
				input.pager.count(AT_A_TIME);
			}

			queueForResaving(input.typeName,
					PagerHelper.moveForward(input.pager));
		}
	}

	public static void queueForResaving(String typeName) {
		queueForResaving(typeName,
				PagerHelper.createDefaultPager().count(AT_A_TIME));
	}

	public static void queueForResaving(String typeName, Pager pager) {
		QueueHelper.enqueue(URL, QUEUE_FOR_RESAVING_ACTION,
				new QueueForResavingAction().typeName(typeName).pager(pager));
	}

	public static void resave(String typeName, List<? extends DataType> data,
			Pager pager) {
		QueueHelper.enqueue(URL, RESAVE_ACTION,
				new ResaveAction().typeName(typeName)
						.data(data.stream().map(i -> new DataType().id(i.id))
								.collect(Collectors.toList()))
						.pager(pager));
	}

}
