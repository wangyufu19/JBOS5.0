package com.jbosframework.utils;

import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Nullable;
import java.util.*;

/**
 * 字符串帮助类
 * @author youfu.wang
 * @version 1.0
 */
public class StringUtils {
	public static String[] trimArrayElements(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return array;
		} else {
			String[] result = new String[array.length];

			for(int i = 0; i < array.length; ++i) {
				String element = array[i];
				result[i] = element != null ? element.trim() : null;
			}

			return result;
		}
	}
	@Nullable
	public static String getFilenameExtension(@Nullable String path) {
		if (path == null) {
			return null;
		} else {
			int extIndex = path.lastIndexOf(46);
			if (extIndex == -1) {
				return null;
			} else {
				int folderIndex = path.lastIndexOf("/");
				return folderIndex > extIndex ? null : path.substring(extIndex + 1);
			}
		}
	}
	public static boolean endsWithIgnoreCase(@Nullable String str, @Nullable String suffix) {
		return str != null && suffix != null && str.length() >= suffix.length() && str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length());
	}
	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf("/");
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith("/")) {
				newPath = newPath + "/";
			}

			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}
	public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
		if (index + substring.length() > str.length()) {
			return false;
		} else {
			for(int i = 0; i < substring.length(); ++i) {
				if (str.charAt(index + i) != substring.charAt(i)) {
					return false;
				}
			}

			return true;
		}
	}
	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		} else {
			int len = str.length();
			StringBuilder sb = new StringBuilder(str.length());

			for(int i = 0; i < len; ++i) {
				char c = str.charAt(i);
				if (!Character.isWhitespace(c)) {
					sb.append(c);
				}
			}

			return sb.toString();
		}
	}
	@Nullable
	public static String getFilename(@Nullable String path) {
		if (path == null) {
			return null;
		} else {
			int separatorIndex = path.lastIndexOf("/");
			return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
		}
	}
	public static String cleanPath(String path) {
		if (!hasLength(path)) {
			return path;
		} else {
			String pathToUse = replace(path, "\\", "/");
			int prefixIndex = pathToUse.indexOf(58);
			String prefix = "";
			if (prefixIndex != -1) {
				prefix = pathToUse.substring(0, prefixIndex + 1);
				if (prefix.contains("/")) {
					prefix = "";
				} else {
					pathToUse = pathToUse.substring(prefixIndex + 1);
				}
			}

			if (pathToUse.startsWith("/")) {
				prefix = prefix + "/";
				pathToUse = pathToUse.substring(1);
			}

			String[] pathArray = delimitedListToStringArray(pathToUse, "/");
			LinkedList<String> pathElements = new LinkedList();
			int tops = 0;

			int i;
			for(i = pathArray.length - 1; i >= 0; --i) {
				String element = pathArray[i];
				if (!".".equals(element)) {
					if ("..".equals(element)) {
						++tops;
					} else if (tops > 0) {
						--tops;
					} else {
						pathElements.add(0, element);
					}
				}
			}

			for(i = 0; i < tops; ++i) {
				pathElements.add(0, "..");
			}

			if (pathElements.size() == 1 && "".equals(pathElements.getLast()) && !prefix.endsWith("/")) {
				pathElements.add(0, ".");
			}

			return prefix + collectionToDelimitedString(pathElements, "/");
		}
	}

	public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim, String prefix, String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
			Iterator it = coll.iterator();

			while(it.hasNext()) {
				sb.append(prefix).append(it.next()).append(suffix);
				if (it.hasNext()) {
					sb.append(delim);
				}
			}

			return sb.toString();
		}
	}
	public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	public static String collectionToCommaDelimitedString(@Nullable Collection<?> coll) {
		return collectionToDelimitedString(coll, ",");
	}
	public static String delete(String inString, String pattern) {
		return replace(inString, pattern, "");
	}

	public static String deleteAny(String inString, @Nullable String charsToDelete) {
		if (hasLength(inString) && hasLength(charsToDelete)) {
			StringBuilder sb = new StringBuilder(inString.length());

			for(int i = 0; i < inString.length(); ++i) {
				char c = inString.charAt(i);
				if (charsToDelete.indexOf(c) == -1) {
					sb.append(c);
				}
			}

			return sb.toString();
		} else {
			return inString;
		}
	}
	public static String[] commaDelimitedListToStringArray(@Nullable String str) {
		return delimitedListToStringArray(str, ",");
	}
	public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter) {
		return delimitedListToStringArray(str, delimiter, (String)null);
	}

	public static Set<String> commaDelimitedListToSet(@Nullable String str) {
		String[] tokens = commaDelimitedListToStringArray(str);
		return new LinkedHashSet(Arrays.asList(tokens));
	}
	public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter, @Nullable String charsToDelete) {
		if (str == null) {
			return new String[0];
		} else if (delimiter == null) {
			return new String[]{str};
		} else {
			List<String> result = new ArrayList();
			int pos;
			if (delimiter.isEmpty()) {
				for(pos = 0; pos < str.length(); ++pos) {
					result.add(deleteAny(str.substring(pos, pos + 1), charsToDelete));
				}
			} else {
				int delPos;
				for(pos = 0; (delPos = str.indexOf(delimiter, pos)) != -1; pos = delPos + delimiter.length()) {
					result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				}

				if (str.length() > 0 && pos <= str.length()) {
					result.add(deleteAny(str.substring(pos), charsToDelete));
				}
			}

			return toStringArray((Collection)result);
		}
	}

	public static String arrayToDelimitedString(@Nullable Object[] arr, String delim) {
		if (ObjectUtils.isEmpty(arr)) {
			return "";
		} else if (arr.length == 1) {
			return ObjectUtils.nullSafeToString(arr[0]);
		} else {
			StringBuilder sb = new StringBuilder();

			for(int i = 0; i < arr.length; ++i) {
				if (i > 0) {
					sb.append(delim);
				}

				sb.append(arr[i]);
			}

			return sb.toString();
		}
	}
	public static boolean hasLength(CharSequence str) {
		return str != null && str.length() > 0;
	}

	public static boolean hasLength(String str) {
		return str != null && !str.isEmpty();
	}
	/**
	 * 判断是空值
	 * @param str
	 * @return
	 */
	public static boolean isNUll(String str){
		if(null==str||"null".equals(str)||"".equals(str)){
			return true;
		}else
			return false;
	}
	/**
	 * 判断非空值
	 * @param str
	 * @return
	 */
	public static boolean isNotNUll(String str){
		if(null==str||"null".equals(str)||"".equals(str)){
			return false;
		}else
			return true;
	}
	/**
	 * 判断是空值
	 * @param obj
	 * @return
	 */
	public static boolean isNUll(Object obj){
		if(null==obj){
			return true;
		}else
			return false;
	}
	/**
	 * 替换空指针
	 * @param str
	 * @return
	 */
	public static String replaceNull(String str){			
		if(str==null||str.equals("null")){			
			return "";
		}else 
			return str;			
	}
	/**
	 * 替换空指针
	 * @param obj
	 * @return
	 */
	public static String replaceNull(Object obj){		
		if(obj==null||obj.equals("null")){			
			return "";
		}else 
			return String.valueOf(obj);			
	}
	/**
	 * 替换空指针
	 * @param str
	 * @return
	 */
	public static String[] replaceNull(String[] str){
		if(str==null) return null;
		for(int i=0;i<str.length;i++){
			if(str[i]==null||str[i].equals("null")){
				str[i]="";
			}
		}
		return str;		
	}
	public static String stringArrayToTokenize(String[] array, String delimiters) {
		if(isNUll(array)){
			return null;
		}
		String tokenizeString="";
		for(String str:array){
			tokenizeString+=tokenizeString+str+delimiters;
		}
		return tokenizeString;
	}

	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
		if (str == null) {
			return null;
		} else {
			StringTokenizer st = new StringTokenizer(str, delimiters);
			ArrayList tokens = new ArrayList();

			while(true) {
				String token;
				do {
					if (!st.hasMoreTokens()) {
						return toStringArray(tokens);
					}

					token = st.nextToken();
					if (trimTokens) {
						token = token.trim();
					}
				} while(ignoreEmptyTokens && token.length() <= 0);

				tokens.add(token);
			}
		}
	}
	public static String[] toStringArray(Collection collection) {
		return collection == null ? null : (String[])((String[])collection.toArray(new String[collection.size()]));
	}
	public static String[] toStringArray(@Nullable Enumeration<String> enumeration) {
		return enumeration != null ? toStringArray((Collection)Collections.list(enumeration)) : new String[0];
	}
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (hasLength(inString) && hasLength(oldPattern) && newPattern != null) {
			int index = inString.indexOf(oldPattern);
			if (index == -1) {
				return inString;
			} else {
				int capacity = inString.length();
				if (newPattern.length() > oldPattern.length()) {
					capacity += 16;
				}

				StringBuilder sb = new StringBuilder(capacity);
				int pos = 0;

				for(int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
					sb.append(inString.substring(pos, index));
					sb.append(newPattern);
					pos = index + patLen;
				}

				sb.append(inString.substring(pos));
				return sb.toString();
			}
		} else {
			return inString;
		}
	}
	public static boolean hasText(@Nullable String str) {
		return str != null && !str.isEmpty() && containsText(str);
	}
	private static boolean containsText(CharSequence str) {
		int strLen = str.length();

		for(int i = 0; i < strLen; ++i) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}

		return false;
	}
}
