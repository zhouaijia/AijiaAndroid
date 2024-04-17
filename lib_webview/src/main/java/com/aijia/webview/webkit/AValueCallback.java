package com.aijia.webview.webkit;


/**
 * A callback interface used to provide values asynchronously.
 */
public interface AValueCallback<T> {
	/**
	 * Invoked when the value is available.
	 * @param value The value.
	 */
	public void onReceiveValue(T value);
};
