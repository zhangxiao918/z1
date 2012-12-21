package com.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author wangqi
 *
 */
public class IpUtils {

	private IpUtils() {
	}

	/**
	 * 将IP转换为4个byte的整型表示
	 * 
	 * @param ip
	 * @return
	 */
	public static long ip2Long(String ip) {
		if (StringUtils.isEmpty(ip)) {
			return 0L;
		}
		String[] digits = ip.split("\\.");
		long[] temp = null;
		boolean invalid = true;

		if (digits.length == 4) {
			temp = new long[] { Long.parseLong(digits[0]),
					Long.parseLong(digits[1]), Long.parseLong(digits[2]),
					Long.parseLong(digits[3]) };

			invalid = ((temp[0] <= 0 || temp[0] > 255)
					|| (temp[1] < 0 || temp[1] > 255)
					|| (temp[2] < 0 || temp[2] > 255) || (temp[3] < 0 || temp[3] > 255));
		}
		if (invalid) {
			throw new RuntimeException("无效的IP地址 [" + ip + "].");
		}

		temp[0] = temp[0] << 24;
		temp[1] = temp[1] << 16;
		temp[2] = temp[2] << 8;

		return temp[0] | temp[1] | temp[2] | temp[3];
	}

}