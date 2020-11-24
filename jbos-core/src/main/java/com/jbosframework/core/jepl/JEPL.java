package com.jbosframework.core.jepl;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JEPL表达式语言
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-3
 */
public class JEPL {
	
	public static final String JEPL_PATTERN = "^\\$\\{[a-zA-Z][a-zA-Z.0-9]{2,}\\}$";
	public static final String JEPL_PATTERN_PREFIX="${";
	public static final String JEPL_PATTERN_SUFFIX="}";
	/**
	 * 编译给定的字符串与表达式匹配
	 * @param s
	 * @return
	 */
	public static boolean matches(String s) {
		boolean b = false;
		if(s==null)
			return b;
		Pattern p = Pattern.compile(JEPL_PATTERN);
		Matcher m = p.matcher(s);	
		b = m.matches();
		return b;
	}
}
