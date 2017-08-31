//
//  GeneratedDownloadHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 28 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.blogwt.shared.page.search.Filter;
import com.willshex.blogwt.shared.page.search.Filter.Query;
import com.willshex.service.IService;
import com.willshex.utility.StringUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public class GeneratedDownloadHelper {

	private static final Logger LOG = Logger
			.getLogger(GeneratedDownloadHelper.class.getName());

	private static final String COMMA = ",";

	private static final String CSV_CONTENT_TYPE = "text/csv";
	private static final String CSV_EXTENSION = "csv";

	private static final String PDF_CONTENT_TYPE = "application/pdf";
	private static final String PDF_EXTENSION = "pdf";

	private static final String HTML_PDF_SERVICE_PROPERTY_KEY = "html.to.pdf.endpoint";
	private static final String DEV_HTML_PDF_SERVICE_PROPERTY_KEY = "dev."
			+ HTML_PDF_SERVICE_PROPERTY_KEY;

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"d MMM yyyy");
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"h:mma");

	public static abstract class Column<T> {
		public String heading;

		public Column (String heading) {
			this.heading = heading;
		}

		public abstract String value (T object);

	}

	public static abstract class FilterResultSaver<S extends IService, T extends DataType> {
		public void save (GeneratedDownload generatedDownload, S service,
				Query toQuery, List<? extends Column<T>> columns)
				throws IOException {
			Stack stack = Stack.parse(generatedDownload.parameters);
			Filter filter = Filter.fromStack(stack);

			save(generatedDownload, filter, service, toQuery, columns);
		}

		public void save (GeneratedDownload generatedDownload, Filter filter,
				S service, Query toQuery, List<? extends Column<T>> columns)
				throws IOException {

			List<T> results = null;
			Pager p = PagerHelper.createDefaultPager()
					.count(Integer.valueOf(20));
			String path = path(generatedDownload, filter);

			// create file
			if (LOG.isLoggable(Level.FINER)) {
				LOG.finer("Saving csv to [" + path + "]");
			}

			GcsFileOptions instance = new GcsFileOptions.Builder()
					.mimeType("text/csv").build();
			GcsFilename fileName = new GcsFilename(
					System.getProperty(GcsHelper.BUCKET_NAME_PROPERTY_KEY),
					path);

			try (BufferedOutputStream osw = new BufferedOutputStream(Channels
					.newOutputStream(GcsServiceFactory.createGcsService()
							.createOrReplace(fileName, instance)))) {
				StringBuffer data = new StringBuffer();

				appendHeader(data, columns);

				osw.write(data.toString().getBytes());
				data.setLength(0);

				do {
					results = getResults(service, filter, toQuery, p);
					processResults(results);
					addToFile(results, data, osw, columns);
					PagerHelper.moveForward(p);
				} while (results.size() == p.count.intValue());

			} catch (IOException e) {
				LOG.log(Level.SEVERE,
						"Error occured trying to write json to [" + path + "]",
						e);
				throw e;
			}
		}

		protected abstract List<T> getResults (S service, Filter filter,
				Query toQuery, Pager p);

		protected void processResults (List<T> results) {}
	}

	@FunctionalInterface
	public interface PdfSaver {

		public static byte[] save (GeneratedDownload generatedDownload,
				Filter filter, PdfSaver saver) {
			byte[] data = null;

			if (generatedDownload != null && generatedDownload.url == null) {
				String htmlData = saver.createDocument(generatedDownload,
						filter, loadTemplate(generatedDownload, filter));

				//				if (LOG.isLoggable(Level.FINE)) {
				//					LOG.fine("Html document created [" + htmlData + "]");
				//				}
				//
				//				GcsHelper.save(htmlData.getBytes(), "text/html",
				//						generatedDownload.id.toString() + ".html");

				StringBuffer buffer = new StringBuffer();
				String htmlPdfEndPoint = System
						.getProperty(SystemProperty.environment
								.value() == SystemProperty.Environment.Value.Production
										? HTML_PDF_SERVICE_PROPERTY_KEY
										: DEV_HTML_PDF_SERVICE_PROPERTY_KEY);

				buffer.append("name=");
				buffer.append(filter.type);
				buffer.append(StringUtils.urlencode(" "));
				buffer.append(filter.query);
				buffer.append(".pdf&document=");
				buffer.append(StringUtils.urlencode(htmlData));
				buffer.append("&settings=");
				buffer.append(StringUtils.urlencode(
						"{\"margin-top\":10,\"margin-right\":20,\"margin-bottom\":10,\"margin-left\":20}"));

				data = HttpHelper.curl(htmlPdfEndPoint,
						buffer.toString().getBytes(), HTTPMethod.POST);

				GcsHelper.save(data, "application/pdf",
						path(generatedDownload));
			}

			return data;
		}

		public String createDocument (GeneratedDownload generatedDownload,
				Filter filter, String template);
	}

	public static String loadTemplate (GeneratedDownload generatedDownload,
			Filter filter) {
		char[] buffer = new char[GcsHelper.BUFFER_SIZE];
		int read;
		StringBuffer htmlTemplate = new StringBuffer();
		String template = null;

		try (BufferedReader is = new BufferedReader(
				new InputStreamReader(GeneratedDownloadHelper.class
						.getResourceAsStream(filter.type + ".html")))) {
			while ((read = is.read(buffer)) > 0) {
				htmlTemplate.append(buffer, 0, read);
				template = htmlTemplate.toString();
			}
		} catch (IOException e) {
			if (LOG.isLoggable(Level.WARNING)) {
				LOG.log(Level.WARNING,
						"Error loading template for generated download ["
								+ generatedDownload + "]");
			}

			throw new RuntimeException(e);
		}

		return template;
	}

	/**
	 * @param data
	 */
	public static void appendHeader (StringBuffer data,
			List<? extends Column<?>> columns) {
		data.append("#");

		boolean first = true;
		for (Column<?> column : columns) {
			if (!first) {
				data.append(COMMA);
			}

			data.append(column.heading);
			first = false;
		}

		data.append("\n");
	}

	/**
	 * @param data
	 */
	public static <T> void appendRow (StringBuffer data,
			List<? extends Column<T>> columns, T object) {
		boolean first = true;
		for (Column<T> column : columns) {
			if (!first) {
				data.append(COMMA);
			}

			data.append(column.value(object));
			first = false;
		}

		data.append("\n");
	}

	/**
	 * @param results
	 * @param data
	 * @param osw
	 * @throws IOException 
	 */
	private static <T> void addToFile (List<T> results, StringBuffer data,
			BufferedOutputStream osw, List<? extends Column<T>> columns)
			throws IOException {
		if (!results.isEmpty()) {
			for (T t : results) {
				appendRow(data, columns, t);
			}

			if (data.length() > 0) {
				osw.write(data.toString().getBytes());
				data.setLength(0);
			}
		}
	}

	/**
	 * @param type
	 * @return
	 */
	public static String extension (String type) {
		String extension = CSV_EXTENSION;

		// resolve filter type extension
		if (test()) {
			extension = PDF_EXTENSION;
		}

		return extension;
	}

	/**
	 * @param type
	 * @return
	 */
	public static String contentType (String type) {
		String contentType = CSV_CONTENT_TYPE;

		// TODO: resolve filter type content type
		if (test()) {
			contentType = PDF_CONTENT_TYPE;
		}

		return contentType;
	}

	/**
	 * @param generatedDownload
	 * @return
	 */
	public static String path (GeneratedDownload generatedDownload,
			Filter filter) {
		if (filter == null) {
			filter = Filter
					.fromStack(Stack.parse(generatedDownload.parameters));
		}

		return filter.type + "/" + generatedDownload.id + "."
				+ extension(filter.type);
	}

	public static String path (GeneratedDownload generatedDownload) {
		return path(generatedDownload, null);
	}

	/**
	 * @param generatedDownload
	 * @param data
	 */
	public static void sendEmail (GeneratedDownload generatedDownload,
			Filter filter, byte[] data) {
		User user = UserServiceProvider.provide()
				.getUser(PersistenceHelper.keyToId(generatedDownload.userKey));

		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Sending generated download as attachment to user ["
					+ user.id + "]");
		}

		if (user != null) {
			StringBuffer buffer = new StringBuffer();

			buffer.append("Hi ");
			buffer.append(UserHelper.name(user));
			buffer.append(",\n");
			buffer.append("Your download (" + filter.type
					+ ") is ready. Please find it attached.\n");

			String emailBody = buffer.toString();

			buffer.setLength(0);

			buffer.append("Download ready - ");
			buffer.append(filter.type);
			String emailSubject = buffer.toString();

			Map<String, byte[]> attachments = new HashMap<>();
			attachments.put(fileName(generatedDownload, filter), data);

			EmailHelper.sendEmail(user.email, UserHelper.name(user),
					emailSubject, emailBody, false, attachments);
		}
	}

	/**
	 * @param generatedDownload
	 * @param filter
	 * @return
	 */
	public static String fileName (GeneratedDownload generatedDownload,
			Filter filter) {
		return StringUtils.urldecode(generatedDownload.parameters)
				.replace("/", "_").replace("&", "_").replace(" ", "_") + "."
				+ GeneratedDownloadHelper.extension(filter.type);
	}

	private static boolean test () {
		return false;
	}
}
