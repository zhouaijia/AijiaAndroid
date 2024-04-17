package com.aijia.webview.webkit;



/**
 * Encapsulates information about errors occured during loading of web resources. See
 */
public abstract class AWebResourceError {
	/**
	 * Gets the error code of the error. The code corresponds to one
	 * of the ERROR_* constants in OneWebViewClient.
	 *
	 * @return The error code of the error
	 */
	public abstract int getErrorCode();

	/**
	 * Gets the string describing the error. Descriptions are localized,
	 * and thus can be used for communicating the problem to the user.
	 *
	 * @return The description of the error
	 */
	public abstract CharSequence getDescription();

	/**
	 * This class can not be subclassed by applications.
	 * @hide
	 */
	public AWebResourceError() {}
}
