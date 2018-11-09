package com.gs.mall.log;

import java.util.UUID;

/**
 * UUID工具
 * 
 * @author liuy
 *
 */
public class UuidUtils {

	/**
	 * 32位UUID
	 */
	public static String uuid32() {
		// 创建 GUID 对象
		UUID uuid = UUID.randomUUID();
		// 得到对象产生的ID
		String a = uuid.toString();
		// 转换为大写
		a = a.toUpperCase();
		return a.replaceAll("-", "");
	}
}
