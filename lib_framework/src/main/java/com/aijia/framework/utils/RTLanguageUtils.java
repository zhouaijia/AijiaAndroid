package com.aijia.framework.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class RTLanguageUtils {

	/**默认语言：英语*/
	public static final Locale DEFAULT_LOCALE = Locale.US;

	/**支持的语言*/
	static final String[] SUPPORT_LANGUAGES = new String[]{
			DEFAULT_LOCALE.toLanguageTag(),
			Locale.SIMPLIFIED_CHINESE.toLanguageTag(),
			Locale.KOREA.toLanguageTag(),
			Locale.JAPAN.toLanguageTag(),
			Locale.GERMANY.toLanguageTag(),
			Locale.TAIWAN.toLanguageTag(),
			"zh-HK",
			"in-ID",    //印尼语
			"th-TH",    //泰语
			"ru-RU"     //俄语
	};

	public static boolean isSupport(String language) {
		List<String> supportedLanguages = Arrays.asList(SUPPORT_LANGUAGES);
		return supportedLanguages.contains(language);
	}

	/**
	 * 语言切换的代码，在高版本中废弃了updateConfiguration方法，替代方法为createConfigurationContext。
	 * 该方法是返回一个Context，也就是语言需要植入到Context中，每个Context都植入一遍。
	 * 当然不可能在每个Activity中都植入一遍，只需值BaseActivity中统一植入即可
	 *
	 * @param newBase 上下文
	 * @param languageCode 用户当前设置的语言code
	 */
	public static Context attachBaseContext(Context newBase, String languageCode) {
		return setLocale(newBase, languageCode);
	}

	public static Context setLocale(Context context, String languageCode) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			return setLanguage(context, getLocale(context, languageCode));

		} else {
			setLanguageOld(context, getLocale(context, languageCode));
			return context;
		}
	}

	/**如果App不支持，则默认使用英语*/
	public static Locale getLocale(Context context, String languageCode) {
		//如果用户未设置过语言，则取手机的语言
		if (TextUtils.isEmpty(languageCode)) {
			languageCode = getLanguageCode(getSystemLocale());
		}

		//如果语言已经支持，则直接返回
		if (isSupport(languageCode)) {
			return fromLanguageCode(languageCode);
		}

		return DEFAULT_LOCALE;
	}

	public static Locale fromLanguageCode(String languageCode) {
		if (TextUtils.isEmpty(languageCode)) {
			return null;
		}

		String[] arr = languageCode.split("-");
		if (arr.length == 1) {
			return new Locale(arr[0]);
		} else {
			return new Locale(arr[0], arr[1]);
		}
	}

	/**
	 *7.0以上切换语言方法
	 **/
	@RequiresApi(api = Build.VERSION_CODES.N)
	public static Context setLanguage(Context context, Locale locale) {
		Resources resources = context.getResources();
		Configuration configuration = resources.getConfiguration();
		configuration.setLocale(locale);
		configuration.setLocales(new LocaleList(locale));

		return context.createConfigurationContext(configuration);
	}


	/**
	 * 7.0以下设置语言切换方法
	 */
	public static void setLanguageOld(Context context, Locale targetLocale) {
		//mContext是全局的application对象
		Configuration configuration = context.getResources().getConfiguration();
		configuration.setLocale(targetLocale);

		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		resources.updateConfiguration(configuration, dm);//语言更换生效的代码!
	}

	public static Locale getSystemLocale() {
		return Locale.getDefault();
	}

	public static String getLanguageCode(Locale locale) {
		String language = locale.getLanguage();
		String country = locale.getCountry();

		List<String> values = new ArrayList<>();
		values.add(language);
		if (!StringUtils.isNullOrEmpty(country)) {
			values.add(country);
		}

		return TextUtils.join("-", values);
	}

}
