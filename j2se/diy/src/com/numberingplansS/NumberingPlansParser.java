package com.numberingplansS;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.util.NodeList;

public class NumberingPlansParser {

	static String PAGE_URL = "https://www.numberingplans.com/?page=plans&sub=imeinr&alpha_2_input={1}&current_page={2}";

	//
	static int[] SEEDS = {1,10,30,31,33,35,44,45,49,50,51,52,53,54,86,91,97,98,99};
	
	public static void main(String args[]) {
		try {
//			catalog();
			doSort();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void catalog() throws Exception { // WebsiteBean bean
		Parser parser = new Parser();
		parser
				.setResource("E:\\2.WS\\1.PRODUCT\\diy_page\\src\\com\\numberingplans\\index.html");
		NodeFilter fileter = new NodeClassFilter(SelectTag.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "alpha_2_input")); // id
																		// makes_list_module
		StringBuffer sb = new StringBuffer("static int[] SEEDS = {");
		if (null != list && list.size() > 0) {
			SelectTag table = (SelectTag) list.elementAt(0);
			OptionTag[] options = table.getOptionTags();
			for (OptionTag op : options) {
				if (null != op.getValue() && !"".equals(op.getValue())) {
					System.out.println(" >> value:" + op.getValue());
					sb.append(op.getValue()).append(",");
				}
			}
		}
		sb.append("};");
		if (null != parser)
			parser = null;
		System.out.println("" + sb.toString());
	}
	
	static void doSort(){
		StringBuffer sb = new StringBuffer("static int[] SEEDS = {");
		sort(SEEDS);
		for(int i:SEEDS){
			System.out.println(" >> "+i);
			sb.append(i).append(",");
		}
		sb.append("};");
		System.out.println("" + sb.toString());
	}

	public static void sort(int[] values) {
		int temp;
		for (int i = 0; i < values.length; ++i) {
			for (int j = 0; j < values.length - i - 1; ++j) {
				if (values[j] > values[j + 1]) {
					temp = values[j];
					values[j] = values[j + 1];
					values[j + 1] = temp;
				}
			}
		}
	}
}
