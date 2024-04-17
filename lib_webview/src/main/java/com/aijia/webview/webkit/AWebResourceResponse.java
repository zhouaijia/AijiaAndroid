package com.aijia.webview.webkit;

import java.io.InputStream;
import java.util.Map;

public class AWebResourceResponse {
	private String mMimeType;
	private String mEncoding;
	private int mStatusCode;
	private String mReasonPhrase;
	private Map<String, String> mResponseHeaders;
	private InputStream mInputStream;

	public AWebResourceResponse() {
	}

	public AWebResourceResponse(String mimeType, String encoding, InputStream inputStream) {
		this.mMimeType = mimeType;
		this.mEncoding = encoding;
		this.setData(inputStream);
	}

	public AWebResourceResponse(String mimeType, String encoding, int statusCode, String reasonPhrase,
								Map<String, String> responseHeaders, InputStream inputStream) {
		this(mimeType, encoding, inputStream);
		this.setStatusCodeAndReasonPhrase(statusCode, reasonPhrase);
		this.setResponseHeaders(responseHeaders);
	}

	public void setMimeType(String mimeType) {
		this.mMimeType = mimeType;
	}

	public String getMimeType() {
		return this.mMimeType;
	}

	public void setEncoding(String encoding) {
		this.mEncoding = encoding;
	}

	public String getEncoding() {
		return this.mEncoding;
	}

	public void setStatusCodeAndReasonPhrase(int statusCode, String reasonPhrase) {
		this.mStatusCode = statusCode;
		this.mReasonPhrase = reasonPhrase;
	}

	public int getStatusCode() {
		return this.mStatusCode;
	}

	public String getReasonPhrase() {
		return this.mReasonPhrase;
	}

	public void setResponseHeaders(Map<String, String> responseHeaders) {
		this.mResponseHeaders = responseHeaders;
	}

	public Map<String, String> getResponseHeaders() {
		return this.mResponseHeaders;
	}

	public void setData(InputStream inputStream) {
		this.mInputStream = inputStream;
	}

	public InputStream getData() {
		return this.mInputStream;
	}
}
