package com.aijia.framework.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.UserManager;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NetworkUtils {
	private static final String TAG = NetworkUtils.class.getSimpleName();
	private static final int TYPE_WIFI_P2P = 13;
	private static final int INVALID_RSSI = -127;
	private static final String STR_NSA = "5G_NSA";
	private static final String STR_SA = "5G_SA";
	public static final int UNAVAILABLE = 2147483647;

	public static final class SignalType {
		public static final String LTE_RSRP = "lteRsrp";
		public static final String LTE_RSRQ = "lteRsrq";
		public static final String LTE_RSSI = "lteRssi";
		public static final String LTE_RSSNR = "lteRssnr";
		public static final String LTE_DBM = "lteDbm";
		public static final String LTE_CQI = "lteCqi";
		public static final String NR_DBM = "nrDbm";
		public static final String NR_SSRSRP = "nrSSRsrp";
		public static final String NR_CSIRSRP = "nrCSIRsrp";
		public static final String NR_SSRSRQ = "nrSSRsrq";
		public static final String NR_CSIRSRQ = "nrCSIRsrq";
		public static final String NR_SSSINR = "nrSSSinr";
		public static final String NR_CSISINR = "nrCSISinr";
	}

	public static final class NetType {
		public static final int TYPE_NO_NETWORK = -1;
		public static final int TYPE_UNKNOWN = 0;
		public static final int TYPE_WIFI = 1;
		public static final int TYPE_2G = 2;
		public static final int TYPE_3G = 3;
		public static final int TYPE_4G = 4;
		public static final int TYPE_4G_NSA = 7;
		public static final int TYPE_5G = 5;
		public static final int TYPE_5G_SA = 8;
		public static final int TYPE_MOBILE = 6;
	}

	@SuppressLint({"MissingPermission"})
	public static NetworkInfo getNetworkInfo(Context context) {
		if (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
			NetworkInfo netInfo = null;
			ConnectivityManager manager = (ConnectivityManager)ContextCompat.getSystemService(context, "connectivity");
			if (null != manager) {
				try {
					netInfo = manager.getActiveNetworkInfo();
				} catch (RuntimeException re) {
					Log.i(TAG, "getActiveNetworkInfo failed, exception:" + re.getClass().getSimpleName() + re.getMessage());
				}
			}

			return netInfo;
		} else {
			return null;
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		if (!ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
			return true;
		} else {
			NetworkInfo info = getNetworkInfo(context);
			return info != null && info.isConnected();
		}
	}

	@SuppressLint({"MissingPermission"})
	private static String[] getDnsServerIpsFromConnectionManager(Context context) {
		LinkedList<String> dnsServers = new LinkedList();
		if (android.os.Build.VERSION.SDK_INT >= 21 && context != null) {
			ConnectivityManager connectivityManager = (ConnectivityManager)ContextCompat.getSystemService(context, "connectivity");
			if (connectivityManager != null) {
				try {
					NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
					if (activeNetworkInfo != null) {
						Network[] networks = connectivityManager.getAllNetworks();
						Network[] var5 = networks;
						int var6 = networks.length;

						for(int var7 = 0; var7 < var6; ++var7) {
							Network network = var5[var7];
							if (network != null) {
								NetworkInfo networkInfo = null;
								networkInfo = connectivityManager.getNetworkInfo(network);
								if (networkInfo != null && networkInfo.getType() == activeNetworkInfo.getType()) {
									LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
									if (linkProperties != null) {
										Iterator var11 = linkProperties.getDnsServers().iterator();

										while(var11.hasNext()) {
											InetAddress addr = (InetAddress)var11.next();
											dnsServers.add(addr.getHostAddress());
										}
									}
								}
							}
						}
					}
				} catch (SecurityException var13) {
					Log.i(TAG, "getActiveNetworkInfo failed, exception:" + var13.getClass().getSimpleName());
				} catch (RuntimeException var14) {
					Log.i(TAG, "getActiveNetworkInfo failed, exception:" + var14.getClass().getSimpleName());
				}
			}
		}

		return dnsServers.isEmpty() ? new String[0] : (String[])dnsServers.toArray(new String[dnsServers.size()]);
	}

	public static String getDnsServerIps(Context context) {
		String[] dnsServers = getDnsServerIpsFromConnectionManager(context);
		return Arrays.toString(dnsServers);
	}

	public static int getLteRsrq(Context context) {
		int rsrq = 2147483647;
		SignalStrength signalStrength = getSignalStrength(context);
		if (signalStrength == null) {
			return rsrq;
		} else {
			try {
				if (android.os.Build.VERSION.SDK_INT <= 28) {
					return getInfoWithReflect(signalStrength, "getLteRsrq");
				}

				List<CellSignalStrengthLte> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthLte.class);
				if (cellSignalStrengthLtes.size() > 0) {
					return ((CellSignalStrengthLte)cellSignalStrengthLtes.get(0)).getRsrq();
				}
			} catch (Throwable var4) {
				Log.i(TAG, "getLteRsrq: throwable:" + var4.getClass());
			}

			return rsrq;
		}
	}

	public static int getLteRssnr(Context context) {
		int rssnr = 2147483647;
		SignalStrength signalStrength = getSignalStrength(context);
		if (signalStrength == null) {
			return rssnr;
		} else {
			try {
				if (android.os.Build.VERSION.SDK_INT <= 28) {
					return getInfoWithReflect(signalStrength, "getLteRssnr");
				}

				List<CellSignalStrengthLte> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthLte.class);
				if (cellSignalStrengthLtes.size() > 0) {
					return ((CellSignalStrengthLte)cellSignalStrengthLtes.get(0)).getRssnr();
				}
			} catch (Throwable var4) {
				Log.i(TAG, "getLteRssnr: throwable:" + var4.getClass());
			}

			return rssnr;
		}
	}

	public static int getLteRsrp(Context context) {
		int rsrp = 2147483647;
		SignalStrength signalStrength = getSignalStrength(context);
		if (signalStrength == null) {
			return rsrp;
		} else {
			try {
				if (android.os.Build.VERSION.SDK_INT <= 28) {
					return getInfoWithReflect(signalStrength, "getLteRsrp");
				}

				List<CellSignalStrengthLte> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthLte.class);
				if (cellSignalStrengthLtes.size() > 0) {
					return ((CellSignalStrengthLte)cellSignalStrengthLtes.get(0)).getRsrp();
				}
			} catch (Throwable var4) {
				Log.i(TAG, "getLteRsrp: throwable:" + var4.getClass());
			}

			return rsrp;
		}
	}

	public static int getLteCqi(Context context) {
		int cqi = 2147483647;
		SignalStrength signalStrength = getSignalStrength(context);
		if (signalStrength == null) {
			return cqi;
		} else {
			try {
				if (android.os.Build.VERSION.SDK_INT <= 28) {
					return getInfoWithReflect(signalStrength, "getLteCqi");
				}

				List<CellSignalStrengthLte> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthLte.class);
				if (cellSignalStrengthLtes.size() > 0) {
					return ((CellSignalStrengthLte)cellSignalStrengthLtes.get(0)).getCqi();
				}
			} catch (Throwable var4) {
				Log.i(TAG, "getLteCqi: throwable:" + var4.getClass());
			}

			return cqi;
		}
	}

	public static int getLteRssi(Context context) {
		int rssi = 2147483647;
		SignalStrength signalStrength = getSignalStrength(context);
		if (signalStrength == null) {
			return rssi;
		} else {
			try {
				if (android.os.Build.VERSION.SDK_INT > 28) {
					List<CellSignalStrengthLte> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthLte.class);
					if (cellSignalStrengthLtes.size() > 0) {
						return ((CellSignalStrengthLte)cellSignalStrengthLtes.get(0)).getRssi();
					}
				}
			} catch (Throwable var4) {
				Log.i(TAG, "getLteRssi: throwable:" + var4.getClass());
			}

			return rssi;
		}
	}

	public static int getInfoWithReflect(SignalStrength signalStrength, String name) {
		try {
			if (android.os.Build.VERSION.SDK_INT <= 28) {
				final Method method = SignalStrength.class.getDeclaredMethod(name);
				AccessController.doPrivileged(new PrivilegedAction() {
					public Object run() {
						method.setAccessible(true);
						return null;
					}
				});
				return (Integer)method.invoke(signalStrength);
			}
		} catch (NoSuchMethodException var3) {
			Log.i(TAG, name + " : function not found");
		} catch (IllegalAccessException var4) {
			Log.i(TAG, name + " : cannot access");
		} catch (InvocationTargetException var5) {
			Log.i(TAG, name + " : InvocationTargetException");
		} catch (Throwable var6) {
			Log.i(TAG, name + " : throwable:" + var6.getClass());
		}

		return 2147483647;
	}

	public static Map<String, Integer> getLteSignalInfo(Context context) {
		Map<String, Integer> map = new HashMap();
		SignalStrength signalStrength = getSignalStrength(context);
		if (signalStrength == null) {
			return map;
		} else {
			try {
				if (android.os.Build.VERSION.SDK_INT > 28) {
					List<CellSignalStrengthLte> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthLte.class);
					if (cellSignalStrengthLtes.size() > 0) {
						map.put("lteDbm", ((CellSignalStrengthLte)cellSignalStrengthLtes.get(0)).getDbm());
						map.put("lteRsrp", ((CellSignalStrengthLte)cellSignalStrengthLtes.get(0)).getRsrp());
						map.put("lteRsrq", ((CellSignalStrengthLte)cellSignalStrengthLtes.get(0)).getRsrq());
						map.put("lteRssnr", ((CellSignalStrengthLte)cellSignalStrengthLtes.get(0)).getRssnr());
						map.put("lteCqi", ((CellSignalStrengthLte)cellSignalStrengthLtes.get(0)).getCqi());
						map.put("lteRssi", ((CellSignalStrengthLte)cellSignalStrengthLtes.get(0)).getRssi());
					}
				} else {
					map.put("lteDbm", getInfoWithReflect(signalStrength, "getDbm"));
					map.put("lteRsrp", getInfoWithReflect(signalStrength, "getLteRsrp"));
					map.put("lteRsrq", getInfoWithReflect(signalStrength, "getLteRsrq"));
					map.put("lteRssnr", getInfoWithReflect(signalStrength, "getLteRssnr"));
					map.put("lteCqi", getInfoWithReflect(signalStrength, "getLteCqi"));
				}
			} catch (Throwable var4) {
				Log.i(TAG, "getLteRssi: throwable:" + var4.getClass());
			}

			return map;
		}
	}

	public static Map<String, Integer> getNrSignalInfo(Context context) {
		Map<String, Integer> map = new HashMap();
		SignalStrength signalStrength = getSignalStrength(context);
		if (signalStrength == null) {
			return map;
		} else {
			try {
				if (android.os.Build.VERSION.SDK_INT > 28) {
					List<CellSignalStrengthNr> cellSignalStrengthNrs = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class);
					if (cellSignalStrengthNrs.size() > 0) {
						map.put("nrDbm", ((CellSignalStrengthNr)cellSignalStrengthNrs.get(0)).getDbm());
						map.put("nrCSIRsrp", ((CellSignalStrengthNr)cellSignalStrengthNrs.get(0)).getCsiRsrp());
						map.put("nrCSIRsrq", ((CellSignalStrengthNr)cellSignalStrengthNrs.get(0)).getCsiRsrq());
						map.put("nrCSISinr", ((CellSignalStrengthNr)cellSignalStrengthNrs.get(0)).getCsiSinr());
						map.put("nrSSRsrp", ((CellSignalStrengthNr)cellSignalStrengthNrs.get(0)).getSsRsrp());
						map.put("nrSSRsrq", ((CellSignalStrengthNr)cellSignalStrengthNrs.get(0)).getSsRsrq());
						map.put("nrSSSinr", ((CellSignalStrengthNr)cellSignalStrengthNrs.get(0)).getSsSinr());
					}
				}
			} catch (Throwable var4) {
				Log.i(TAG, "getLteRssi: throwable:" + var4.getClass());
			}

			return map;
		}
	}

	public static int getNrSsRsrp(Context context) {
		int ssRsrp = 2147483647;

		try {
			if (android.os.Build.VERSION.SDK_INT > 28) {
				SignalStrength signalStrength = getSignalStrength(context);
				if (signalStrength == null) {
					return ssRsrp;
				}

				List<CellSignalStrengthNr> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class);
				if (cellSignalStrengthLtes.size() > 0) {
					return ((CellSignalStrengthNr)cellSignalStrengthLtes.get(0)).getSsRsrp();
				}
			}
		} catch (Throwable var4) {
			Log.i(TAG, "getNrSsRsrp: throwable:" + var4.getClass());
		}

		return ssRsrp;
	}

	public static int getNrCsiRsrp(Context context) {
		int csiRsrp = 2147483647;

		try {
			if (android.os.Build.VERSION.SDK_INT > 28) {
				SignalStrength signalStrength = getSignalStrength(context);
				if (signalStrength == null) {
					return csiRsrp;
				}

				List<CellSignalStrengthNr> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class);
				if (cellSignalStrengthLtes.size() > 0) {
					return ((CellSignalStrengthNr)cellSignalStrengthLtes.get(0)).getCsiRsrp();
				}
			}
		} catch (Throwable var4) {
			Log.i(TAG, "getNrCsiRsrp: throwable:" + var4.getClass());
		}

		return csiRsrp;
	}

	public static int getNrSsRsrq(Context context) {
		int ssRsrq = 2147483647;

		try {
			if (android.os.Build.VERSION.SDK_INT > 28) {
				SignalStrength signalStrength = getSignalStrength(context);
				if (signalStrength == null) {
					return ssRsrq;
				}

				List<CellSignalStrengthNr> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class);
				if (cellSignalStrengthLtes.size() > 0) {
					return ((CellSignalStrengthNr)cellSignalStrengthLtes.get(0)).getSsRsrq();
				}
			}
		} catch (Throwable var4) {
			Log.i(TAG, "getNrSsRsrq: throwable:" + var4.getClass());
		}

		return ssRsrq;
	}

	public static int getNrCsiRsrq(Context context) {
		int csiRsrq = 2147483647;

		try {
			if (android.os.Build.VERSION.SDK_INT > 28) {
				SignalStrength signalStrength = getSignalStrength(context);
				if (signalStrength == null) {
					return csiRsrq;
				}

				List<CellSignalStrengthNr> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class);
				if (cellSignalStrengthLtes.size() > 0) {
					return ((CellSignalStrengthNr)cellSignalStrengthLtes.get(0)).getCsiRsrq();
				}
			}
		} catch (Throwable var4) {
			Log.i(TAG, "getNrCsiRsrq: throwable:" + var4.getClass());
		}

		return csiRsrq;
	}

	public static int getNrSsSinr(Context context) {
		int ssSinr = 2147483647;

		try {
			if (android.os.Build.VERSION.SDK_INT > 28) {
				SignalStrength signalStrength = getSignalStrength(context);
				if (signalStrength == null) {
					return ssSinr;
				}

				List<CellSignalStrengthNr> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class);
				if (cellSignalStrengthLtes.size() > 0) {
					return ((CellSignalStrengthNr)cellSignalStrengthLtes.get(0)).getSsSinr();
				}
			}
		} catch (Throwable var4) {
			Log.i(TAG, "getNrSsSinr: throwable:" + var4.getClass());
		}

		return ssSinr;
	}

	public static int getNrCsiSinr(Context context) {
		int csiSinr = 2147483647;

		try {
			if (android.os.Build.VERSION.SDK_INT > 28) {
				SignalStrength signalStrength = getSignalStrength(context);
				if (signalStrength == null) {
					return csiSinr;
				}

				List<CellSignalStrengthNr> cellSignalStrengthLtes = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class);
				if (cellSignalStrengthLtes.size() > 0) {
					return ((CellSignalStrengthNr)cellSignalStrengthLtes.get(0)).getCsiSinr();
				}
			}
		} catch (Throwable var4) {
			Log.i(TAG, "getNrCsiSinr: throwable:" + var4.getClass());
		}

		return csiSinr;
	}

	private static SignalStrength getSignalStrength(Context context) {
		if (context != null && android.os.Build.VERSION.SDK_INT >= 28) {
			Object object = ContextCompat.getSystemService(context, "phone");
			if (object instanceof TelephonyManager) {
				TelephonyManager telephonyManager = (TelephonyManager)object;
				telephonyManager = telephonyManager.createForSubscriptionId(SubscriptionManager.getDefaultDataSubscriptionId());
				return telephonyManager.getSignalStrength();
			}
		}

		return null;
	}

	public static int getWifiRssi(Context context) {
		int rssi = -127;
		if (context != null) {
			Object object = ContextCompat.getSystemService(context.getApplicationContext(), "wifi");
			if (object instanceof WifiManager) {
				WifiManager wifiManager = (WifiManager)object;

				try {
					WifiInfo wifiInfo = wifiManager.getConnectionInfo();
					if (wifiInfo != null && wifiInfo.getBSSID() != null) {
						rssi = wifiInfo.getRssi();
					}
				} catch (RuntimeException var5) {
					Log.i(TAG, "getWifiRssiLevel did not has permission!" + var5.getClass().getSimpleName() + var5.getMessage());
				}
			}
		}

		return rssi;
	}

	public static int getWifiRssiLevel(Context context) {
		return WifiManager.calculateSignalLevel(getWifiRssi(context), 5);
	}

	public static String getWifiGatewayIp(Context context) {
		String wifiGatewayIp = " ";
		if (context != null) {
			Object object = ContextCompat.getSystemService(context.getApplicationContext(), "wifi");
			if (object instanceof WifiManager) {
				WifiManager wifiManager = (WifiManager)object;

				try {
					DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
					int dhcpGateway = dhcpInfo.gateway;
					byte[] byteaddr = new byte[]{(byte)(dhcpGateway & 255), (byte)(dhcpGateway >> 8 & 255), (byte)(dhcpGateway >> 16 & 255), (byte)(dhcpGateway >> 24 & 255)};
					InetAddress gatewatAddr = InetAddress.getByAddress(byteaddr);
					wifiGatewayIp = gatewatAddr.getHostAddress();
				} catch (UnknownHostException | RuntimeException var8) {
					Log.i(TAG, "getWifiGatewayIp error!" + var8.getClass().getSimpleName() + var8.getMessage());
					return wifiGatewayIp;
				}
			}
		}

		return wifiGatewayIp;
	}

	@SuppressLint({"MissingPermission"})
	public static NetworkInfo.DetailedState getNetworkStatus(Context context) {
		NetworkInfo.DetailedState networkStatus = NetworkInfo.DetailedState.IDLE;
		if (context != null) {
			Object object = ContextCompat.getSystemService(context, "connectivity");
			if (object instanceof ConnectivityManager) {
				try {
					if (!ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
						return networkStatus;
					}

					NetworkInfo netInfo = ((ConnectivityManager)object).getActiveNetworkInfo();
					if (netInfo != null) {
						networkStatus = netInfo.getDetailedState();
					} else {
						Log.i(TAG, "getNetworkStatus networkIsConnected netInfo is null!");
					}
				} catch (RuntimeException var4) {
					Log.i(TAG, "getNetworkStatus exception" + var4.getClass().getSimpleName() + var4.getMessage());
				}
			} else {
				Log.i(TAG, "getNetworkStatus ConnectivityManager is null!");
			}
		}

		return networkStatus;
	}

	@SuppressLint({"MissingPermission"})
	public static int readDataSaverMode(Context context) {
		int dateSaverMode = 0;
		if (context != null && android.os.Build.VERSION.SDK_INT >= 24 && ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
			Object object = ContextCompat.getSystemService(context, "connectivity");
			if (object instanceof ConnectivityManager) {
				ConnectivityManager manager = (ConnectivityManager)object;

				try {
					if (manager.isActiveNetworkMetered()) {
						dateSaverMode = manager.getRestrictBackgroundStatus();
					} else {
						Log.v(TAG, "ConnectType is not Mobile Network!");
					}
				} catch (RuntimeException var5) {
					Log.e(TAG, "SystemServer error:", var5);
				}
			}
		}

		return dateSaverMode;
	}

	public static boolean isSimReady(Context context) {
		TelephonyManager tm = null;
		Object object = ContextCompat.getSystemService(context, "phone");
		if (object instanceof TelephonyManager) {
			tm = (TelephonyManager)object;
		}

		return tm != null && tm.getSimState() == 5;
	}

	public static String getMNC(Context context) {
		String opeType = "unknown";
		if (context == null) {
			return opeType;
		} else if (!isSimReady(context)) {
			return opeType;
		} else {
			TelephonyManager tm = null;
			Object object = ContextCompat.getSystemService(context, "phone");
			if (object instanceof TelephonyManager) {
				tm = (TelephonyManager)object;
			}

			if (tm == null) {
				Log.e(TAG, "getSubscriptionOperatorType: other error!");
				return opeType;
			} else {
				String operator = tm.getNetworkOperator();
				if (!"46001".equals(operator) && !"46006".equals(operator) && !"46009".equals(operator)) {
					if (!"46000".equals(operator) && !"46002".equals(operator) && !"46004".equals(operator) && !"46007".equals(operator)) {
						if (!"46003".equals(operator) && !"46005".equals(operator) && !"46011".equals(operator)) {
							opeType = "other";
						} else {
							opeType = "China_Telecom";
						}
					} else {
						opeType = "China_Mobile";
					}
				} else {
					opeType = "China_Unicom";
				}

				return opeType;
			}
		}
	}

	public static String getHost(String url) {
		if (TextUtils.isEmpty(url)) {
			return "";
		} else {
			URI uri;
			try {
				uri = new URI(url);
			} catch (URISyntaxException var3) {
				Log.w(TAG, var3.getClass().getSimpleName());
				return "";
			}

			return uri.getHost();
		}
	}

	public static boolean isUserUnlocked(Context context) {
		if (android.os.Build.VERSION.SDK_INT >= 24) {
			UserManager userManager = (UserManager)ContextCompat.getSystemService(context, "user");
			if (userManager != null) {
				try {
					return userManager.isUserUnlocked();
				} catch (RuntimeException var3) {
					Log.e(TAG, "dealType rethrowFromSystemServer:", var3);
				}
			}
		}

		return true;
	}

	/** @deprecated */
	@Deprecated
	public static NetworkInfo.DetailedState networkStatus(Context context) {
		return getNetworkStatus(context);
	}

	public static boolean isChangeToConnected(NetworkInfo before, NetworkInfo now) {
		if ((before == null || !before.isConnected()) && now.isConnected()) {
			Log.v(TAG, "Find network state changed to connected");
			return true;
		} else {
			return false;
		}
	}


	/*public static String getNetWorkNSAorSA() {
		String netType = null;

		try {
			HwTelephonyManager hwTelephonyManager = HwTelephonyManager.getDefault();
			int phoneId = hwTelephonyManager.getDefault4GSlotId();
			Log.v(TAG, "phoneId " + phoneId);
			boolean isNsa = hwTelephonyManager.isNsaState(phoneId);
			Log.v(TAG, "isNsa " + isNsa);
			if (isNsa) {
				netType = "5G_NSA";
			} else {
				netType = "5G_SA";
			}
		} catch (Throwable var4) {
			Log.v(TAG, "isNsaState error");
		}

		return netType;
	}*/
}
