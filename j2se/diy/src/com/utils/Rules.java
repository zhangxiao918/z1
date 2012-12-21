package com.utils;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 构建1到9999的中文数字排序规则
 * 
 * @author 王琪
 * @version 1.0 Aug 13, 2009
 */
public class Rules {
	private static final Log logger = LogFactory.getLog(Rules.class);

	private static RuleBasedCollator collatorIns;

	public static String getChineseNumOrder() {
		StringBuffer ret = new StringBuffer();

		ret.append("Ⅰ< ");
		ret.append("Ⅱ< ");
		ret.append("Ⅲ< ");
		ret.append("Ⅳ< ");
		ret.append("Ⅴ< ");
		ret.append("Ⅵ< ");
		ret.append("Ⅶ< ");
		ret.append("Ⅷ< ");
		ret.append("Ⅸ< ");
		ret.append("Ⅹ< ");
		ret.append("Ⅺ< ");
		ret.append("Ⅻ< ");
		for (int i = 1; i <= 9999; i++) {
			if (i == 9999) {
				ret.append(NumberUtils.numeric2Chinese(i));
			} else {
				ret.append(NumberUtils.numeric2Chinese(i) + "< ");
			}
		}
		
		
		return ret.toString();

	}

	public static RuleBasedCollator getCollator() {

		if (collatorIns == null) {

			RuleBasedCollator collator = (RuleBasedCollator) Collator

			.getInstance(Locale.CHINA);

			try {

				collatorIns = new RuleBasedCollator(collator.getRules()
						.substring(0, 2125)
						+ getChineseNumOrder());

			} catch (Exception e) {
				logger.error("", e);
			}

		}

		return collatorIns;
	}

}
