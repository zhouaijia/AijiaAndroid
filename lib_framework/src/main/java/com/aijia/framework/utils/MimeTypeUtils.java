package com.aijia.framework.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;


public class MimeTypeUtils {
	public final static String MIME_TYPE_IMAGE = "image/jpeg";
	public final static String MIME_TYPE_VIDEO = "video/mp4";
	public final static String MIME_TYPE_AUDIO = "audio/mpeg";
	public final static String MIME_TYPE_AUDIO_AMR = "audio/amr";

	public final static String MIME_TYPE_PREFIX_IMAGE = "image";
	public final static String MIME_TYPE_PREFIX_VIDEO = "video";
	public final static String MIME_TYPE_PREFIX_AUDIO = "audio";

	private final static String MIME_TYPE_PNG = "image/png";
	public final static String MIME_TYPE_JPEG = "image/jpeg";
	private final static String MIME_TYPE_JPG = "image/jpg";
	private final static String MIME_TYPE_BMP = "image/bmp";
	private final static String MIME_TYPE_GIF = "image/gif";
	private final static String MIME_TYPE_WEBP = "image/webp";

	private final static String MIME_TYPE_3GP = "video/3gp";
	private final static String MIME_TYPE_MP4 = "video/mp4";
	private final static String MIME_TYPE_MPEG = "video/mpeg";
	private final static String MIME_TYPE_AVI = "video/avi";

	private final static String MIME_TYPE_TXT = "text/plain";
	private final static String MIME_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private final static String MIME_TYPE_WORD = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";


	public final static String TXT = ".txt";
	public final static String XLSX = ".xlsx";
	public final static String WORD = ".docx";
	public final static String JPEG = ".jpeg";
	public final static String JPG = ".jpg";
	public final static String PNG = ".png";
	public final static String WEBP = ".webp";
	public final static String GIF = ".gif";
	public final static String BMP = ".bmp";
	public final static String AMR = ".amr";
	public final static String WAV = ".wav";
	public final static String MP3 = ".mp3";
	public final static String MP4 = ".mp4";
	public final static String AVI = ".avi";
	public final static String JPEG_Q = "image/jpeg";
	public final static String PNG_Q = "image/png";
	public final static String MP4_Q = "video/mp4";
	public final static String AVI_Q = "video/avi";
	public final static String AMR_Q = "audio/amr";
	public final static String WAV_Q = "audio/x-wav";
	public final static String MP3_Q = "audio/mpeg";
	public final static String DCIM = "DCIM/Camera";
	public final static String CAMERA = "Camera";


	/**
	 * 根据文件的path获取uri
	 * @param path 文件path
	 * @return 文件的uri
	 */
	public static Uri getFileUri(String path) {

		return isContent(path) || isHasHttp(path)
				? Uri.parse(path)
				: Uri.fromFile(new File(path));
	}

	/**
	 * 根据文件的path获取mimeType
	 *
	 * @param path 文件的path
	 * @return mime type
	 */
	public static String getMimeTypeFromFilePath(String path) {
		return getMimeTypeFromMediaContentUri(getFileUri(path));
	}

	/**
	 * 根据文件的uri获取mimeType
	 *
	 * @param uri 文件的Uri
	 * @return mime type
	 */
	public static String getMimeTypeFromMediaContentUri(Uri uri) {
		String mimeType;
		if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
			ContentResolver cr = Utils.getApp().getContentResolver();
			mimeType = cr.getType(uri);
		} else {
			String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
			mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
		}
		return mimeType;
	}

	/**
	 * is content://
	 *
	 * @param url
	 * @return
	 */
	public static boolean isContent(String url) {
		if (TextUtils.isEmpty(url)) {
			return false;
		}
		return url.startsWith("content://");
	}

	/**
	 * isGif
	 *
	 * @param mimeType
	 * @return
	 */
	public static boolean isHasGif(String mimeType) {
		return mimeType != null && (mimeType.equals("image/gif") || mimeType.equals("image/GIF"));
	}

	/**
	 * isGif
	 *
	 * @param url
	 * @return
	 */
	public static boolean isUrlHasGif(String url) {
		return url.toLowerCase().endsWith(".gif");
	}

	/**
	 * is has image
	 *
	 * @param url
	 * @return
	 */
	public static boolean isUrlHasImage(String url) {
		return url.toLowerCase().endsWith(".jpg")
				|| url.toLowerCase().endsWith(".jpeg")
				|| url.toLowerCase().endsWith(".png")
				|| url.toLowerCase().endsWith(".heic");
	}

	/**
	 * isWebp
	 *
	 * @param mimeType
	 * @return
	 */
	public static boolean isHasWebp(String mimeType) {
		return mimeType != null && mimeType.equalsIgnoreCase("image/webp");
	}

	/**
	 * isWebp
	 *
	 * @param url
	 * @return
	 */
	public static boolean isUrlHasWebp(String url) {
		return url.toLowerCase().endsWith(".webp");
	}

	/**
	 * isVideo
	 *
	 * @param mimeType
	 * @return
	 */
	public static boolean isHasVideo(String mimeType) {
		return mimeType != null && mimeType.startsWith(MIME_TYPE_PREFIX_VIDEO);
	}

	/**
	 * isVideo
	 *
	 * @param url
	 * @return
	 */
	public static boolean isUrlHasVideo(String url) {
		return url.toLowerCase().endsWith(".mp4");
	}

	/**
	 * isAudio
	 *
	 * @param mimeType
	 * @return
	 */
	public static boolean isHasAudio(String mimeType) {
		return mimeType != null && mimeType.startsWith(MIME_TYPE_PREFIX_AUDIO);
	}

	/**
	 * isAudio
	 *
	 * @param url
	 * @return
	 */
	public static boolean isUrlHasAudio(String url) {
		return url.toLowerCase().endsWith(".amr") || url.toLowerCase().endsWith(".mp3");
	}

	/**
	 * isImage
	 *
	 * @param mimeType
	 * @return
	 */
	public static boolean isHasImage(String mimeType) {
		return mimeType != null && mimeType.startsWith(MIME_TYPE_PREFIX_IMAGE);
	}

	/**
	 * Determine if it is JPG.
	 *
	 * @param mimeType image file mimeType
	 */
	public static boolean isJPEG(String mimeType) {
		if (TextUtils.isEmpty(mimeType)) {
			return false;
		}
		return mimeType.startsWith(MIME_TYPE_JPEG) || mimeType.startsWith(MIME_TYPE_JPG);
	}

	/**
	 * Determine if it is JPG.
	 *
	 * @param mimeType image file mimeType
	 */
	public static boolean isJPG(String mimeType) {
		if (TextUtils.isEmpty(mimeType)) {
			return false;
		}
		return mimeType.startsWith(MIME_TYPE_JPG);
	}


	/**
	 * is Network image
	 *
	 * @param path
	 * @return
	 */
	public static boolean isHasHttp(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		return path.startsWith("http") || path.startsWith("https");
	}

	/**
	 * Picture or video
	 *
	 * @return
	 */
	public static int getMimeType(String mimeType) {
		if (TextUtils.isEmpty(mimeType)) {
			return SelectMimeType.TYPE_IMAGE;
		}
		if (mimeType.startsWith(MIME_TYPE_PREFIX_VIDEO)) {
			return SelectMimeType.TYPE_VIDEO;
		} else if (mimeType.startsWith(MIME_TYPE_PREFIX_AUDIO)) {
			return SelectMimeType.TYPE_AUDIO;
		} else {
			return SelectMimeType.TYPE_IMAGE;
		}
	}

	/**
	 * Get image suffix
	 *
	 * @param mimeType
	 * @return
	 */
	public static String getLastImgSuffix(String mimeType) {
		try {
			return mimeType.substring(mimeType.lastIndexOf("/")).replace("/", ".");
		} catch (Exception e) {
			e.printStackTrace();
			return JPG;
		}
	}

	/**
	 * Get file suffix
	 *
	 * @param mimeType
	 * @return
	 */
	public static String getLastSuffix(String mimeType) {
		try {
			if (mimeType.startsWith(MIME_TYPE_TXT)) {
				return TXT;
			} else if (mimeType.startsWith(MIME_TYPE_XLSX)) {
				return XLSX;
			} else if (mimeType.startsWith(MIME_TYPE_WORD)) {
				return WORD;
			}

			return mimeType.substring(mimeType.lastIndexOf("/")).replace("/", ".");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Is it the same type
	 *
	 * @param oldMimeType 已选的资源类型
	 * @param newMimeType 当次选中的资源类型
	 * @return
	 */
	public static boolean isMimeTypeSame(String oldMimeType, String newMimeType) {
		if (TextUtils.isEmpty(oldMimeType)) {
			return true;
		}
		return getMimeType(oldMimeType) == getMimeType(newMimeType);
	}

}
