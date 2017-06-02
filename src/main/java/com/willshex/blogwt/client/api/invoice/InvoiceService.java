// 
package com.willshex.blogwt.client.api.invoice;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.shared.api.invoice.call.AddCustomerRequest;
import com.willshex.blogwt.shared.api.invoice.call.AddCustomerResponse;
import com.willshex.blogwt.shared.api.invoice.call.CreateInvoiceRequest;
import com.willshex.blogwt.shared.api.invoice.call.CreateInvoiceResponse;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrenciesRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrenciesResponse;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrentVendorRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrentVendorResponse;
import com.willshex.blogwt.shared.api.invoice.call.GetCustomersRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCustomersResponse;
import com.willshex.blogwt.shared.api.invoice.call.GetInvoicesRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetInvoicesResponse;
import com.willshex.blogwt.shared.api.invoice.call.SetInvoiceStatusRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetInvoiceStatusResponse;
import com.willshex.blogwt.shared.api.invoice.call.SetupVendorRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetupVendorResponse;
import com.willshex.gson.web.service.client.HttpException;
import com.willshex.gson.web.service.client.JsonService;

public final class InvoiceService extends JsonService {
	public static final String InvoiceMethodCreateInvoice = "CreateInvoice";

	public Request createInvoice (final CreateInvoiceRequest input,
			final AsyncCallback<CreateInvoiceResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(InvoiceMethodCreateInvoice, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								CreateInvoiceResponse outputParameter = new CreateInvoiceResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(InvoiceService.this,
										InvoiceMethodCreateInvoice, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(InvoiceService.this,
										InvoiceMethodCreateInvoice, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(InvoiceService.this,
									InvoiceMethodCreateInvoice, input,
									exception);
						}
					});
			onCallStart(InvoiceService.this, InvoiceMethodCreateInvoice, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(InvoiceService.this, InvoiceMethodCreateInvoice,
					input, exception);
		}
		return handle;
	}

	public static final String InvoiceMethodSetInvoiceStatus = "SetInvoiceStatus";

	public Request setInvoiceStatus (final SetInvoiceStatusRequest input,
			final AsyncCallback<SetInvoiceStatusResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(InvoiceMethodSetInvoiceStatus, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								SetInvoiceStatusResponse outputParameter = new SetInvoiceStatusResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(InvoiceService.this,
										InvoiceMethodSetInvoiceStatus, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(InvoiceService.this,
										InvoiceMethodSetInvoiceStatus, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(InvoiceService.this,
									InvoiceMethodSetInvoiceStatus, input,
									exception);
						}
					});
			onCallStart(InvoiceService.this, InvoiceMethodSetInvoiceStatus,
					input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(InvoiceService.this, InvoiceMethodSetInvoiceStatus,
					input, exception);
		}
		return handle;
	}

	public static final String InvoiceMethodGetCurrentVendor = "GetCurrentVendor";

	public Request getCurrentVendor (final GetCurrentVendorRequest input,
			final AsyncCallback<GetCurrentVendorResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(InvoiceMethodGetCurrentVendor, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetCurrentVendorResponse outputParameter = new GetCurrentVendorResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(InvoiceService.this,
										InvoiceMethodGetCurrentVendor, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(InvoiceService.this,
										InvoiceMethodGetCurrentVendor, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(InvoiceService.this,
									InvoiceMethodGetCurrentVendor, input,
									exception);
						}
					});
			onCallStart(InvoiceService.this, InvoiceMethodGetCurrentVendor,
					input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(InvoiceService.this, InvoiceMethodGetCurrentVendor,
					input, exception);
		}
		return handle;
	}

	public static final String InvoiceMethodAddCustomer = "AddCustomer";

	public Request addCustomer (final AddCustomerRequest input,
			final AsyncCallback<AddCustomerResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(InvoiceMethodAddCustomer, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								AddCustomerResponse outputParameter = new AddCustomerResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(InvoiceService.this,
										InvoiceMethodAddCustomer, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(InvoiceService.this,
										InvoiceMethodAddCustomer, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(InvoiceService.this,
									InvoiceMethodAddCustomer, input, exception);
						}
					});
			onCallStart(InvoiceService.this, InvoiceMethodAddCustomer, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(InvoiceService.this, InvoiceMethodAddCustomer, input,
					exception);
		}
		return handle;
	}

	public static final String InvoiceMethodSetupVendor = "SetupVendor";

	public Request setupVendor (final SetupVendorRequest input,
			final AsyncCallback<SetupVendorResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(InvoiceMethodSetupVendor, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								SetupVendorResponse outputParameter = new SetupVendorResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(InvoiceService.this,
										InvoiceMethodSetupVendor, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(InvoiceService.this,
										InvoiceMethodSetupVendor, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(InvoiceService.this,
									InvoiceMethodSetupVendor, input, exception);
						}
					});
			onCallStart(InvoiceService.this, InvoiceMethodSetupVendor, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(InvoiceService.this, InvoiceMethodSetupVendor, input,
					exception);
		}
		return handle;
	}

	public static final String InvoiceMethodGetCurrencies = "GetCurrencies";

	public Request getCurrencies (final GetCurrenciesRequest input,
			final AsyncCallback<GetCurrenciesResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(InvoiceMethodGetCurrencies, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetCurrenciesResponse outputParameter = new GetCurrenciesResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(InvoiceService.this,
										InvoiceMethodGetCurrencies, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(InvoiceService.this,
										InvoiceMethodGetCurrencies, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(InvoiceService.this,
									InvoiceMethodGetCurrencies, input,
									exception);
						}
					});
			onCallStart(InvoiceService.this, InvoiceMethodGetCurrencies, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(InvoiceService.this, InvoiceMethodGetCurrencies,
					input, exception);
		}
		return handle;
	}

	public static final String InvoiceMethodGetCustomers = "GetCustomers";

	public Request getCustomers (final GetCustomersRequest input,
			final AsyncCallback<GetCustomersResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(InvoiceMethodGetCustomers, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetCustomersResponse outputParameter = new GetCustomersResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(InvoiceService.this,
										InvoiceMethodGetCustomers, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(InvoiceService.this,
										InvoiceMethodGetCustomers, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(InvoiceService.this,
									InvoiceMethodGetCustomers, input,
									exception);
						}
					});
			onCallStart(InvoiceService.this, InvoiceMethodGetCustomers, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(InvoiceService.this, InvoiceMethodGetCustomers, input,
					exception);
		}
		return handle;
	}

	public static final String InvoiceMethodGetInvoices = "GetInvoices";

	public Request getInvoices (final GetInvoicesRequest input,
			final AsyncCallback<GetInvoicesResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(InvoiceMethodGetInvoices, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetInvoicesResponse outputParameter = new GetInvoicesResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(InvoiceService.this,
										InvoiceMethodGetInvoices, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(InvoiceService.this,
										InvoiceMethodGetInvoices, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(InvoiceService.this,
									InvoiceMethodGetInvoices, input, exception);
						}
					});
			onCallStart(InvoiceService.this, InvoiceMethodGetInvoices, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(InvoiceService.this, InvoiceMethodGetInvoices, input,
					exception);
		}
		return handle;
	}
}