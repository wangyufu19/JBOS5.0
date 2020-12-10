package com.application.common.shiro;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * ShiroUtils
 * @author youfu.wang
 * @date 2019-01-31
 */
public class ShiroUtils {
	/**  加密算法 SHA-256,MD5*/
	public final static String hashAlgorithmName = "md5";
	/**  循环次数 */
	public final static int hashIterations = 1;

	public static String md5(String password, String salt) {
		return new Md5Hash(password, salt, hashIterations).toHex();
	}
	public static String sha256(String password, String salt) {
		return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toHex();
	}

	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}
	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}

	public static boolean isLogin() {
		return SecurityUtils.getSubject().getPrincipal() != null;
	}

	public static void logout() {
		SecurityUtils.getSubject().logout();
	}

	public static void main(String[] args) {
		String salt = RandomStringUtils.randomAlphanumeric(20);
		System.out.println("*******salt: "+salt);
		System.out.println("*******pwd: "+ShiroUtils.md5("111111", salt));
	}
}
