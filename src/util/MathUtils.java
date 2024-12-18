package util;

import java.text.DecimalFormat;


public class MathUtils {
	
	public static Double getTwoDouble(Double d) {
		//格式化十进制数字，保留两位小数
		String str = new DecimalFormat("#.00").format(d);
		return Double.parseDouble(str);
		
	}
}
