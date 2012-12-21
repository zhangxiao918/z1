package com.utils;

import org.apache.commons.logging.Log;

public class LogUtils {

	public static void dumpBytesAsHEX(Log logger, byte[] bytes, int maxShowBytes) {
		if (bytes == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("bytes is null");
			}
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("bytes size is:[" + bytes.length + "]");

			int idx = 0;
			StringBuilder body = new StringBuilder();
			for (byte b : bytes) {
				int hex = ((int) b) & 0xff;
				String shex = Integer.toHexString(hex).toUpperCase();
				if (1 == shex.length()) {
					body.append("0");
				}
				body.append(shex);
				body.append(" ");
				idx++;
				if (16 == idx) {
					logger.debug(body.toString());
					body = new StringBuilder();
					idx = 0;
				}
				maxShowBytes--;
				if (maxShowBytes <= 0) {
					if (idx != 0) {
						logger.debug(body.toString());
					}
					return;
				}
			}
			if (idx != 0) {
				logger.debug(body.toString());
			}
		}
	}
}
