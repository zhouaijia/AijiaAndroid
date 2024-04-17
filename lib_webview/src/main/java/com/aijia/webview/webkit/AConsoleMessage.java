package com.aijia.webview.webkit;

/**
 * Public class representing a JavaScript console message from WebCore. This could be a issued
 * by a call to one of the <code>console</code> logging functions (e.g.
 * <code>console.log('...')</code>) or a JavaScript error on the  page. To receive notifications
 * of these messages, override the
 * {@link AWebChromeClient#onConsoleMessage(AConsoleMessage)} function.
 */
public class AConsoleMessage {

	// This must be kept in sync with the WebCore enum in WebCore/page/Console.h
	public enum MessageLevel {
		TIP,
		LOG,
		WARNING,
		ERROR,
		DEBUG
	}

	private final MessageLevel mLevel;
	private final String mMessage;
	private final String mSourceId;
	private final int mLineNumber;

	public AConsoleMessage(String message, String sourceId, int lineNumber, int msgLevel) {
		mMessage = message;
		mSourceId = sourceId;
		mLineNumber = lineNumber;
		mLevel = getLevel(msgLevel);
	}

	public MessageLevel messageLevel() {
		return mLevel;
	}

	public String message() {
		return mMessage;
	}

	public String sourceId() {
		return mSourceId;
	}

	public int lineNumber() {
		return mLineNumber;
	}

	private MessageLevel getLevel (int msgLevel) {
		MessageLevel level = MessageLevel.LOG;

		switch (msgLevel) {
			case 0:
				level = MessageLevel.TIP;
				break;

			case 1:
				level = MessageLevel.LOG;
				break;

			case 2:
				level = MessageLevel.WARNING;
				break;

			case 3:
				level = MessageLevel.ERROR;
				break;

			case 4:
				level = MessageLevel.DEBUG;
				break;
		}

		return level;
	}
}
