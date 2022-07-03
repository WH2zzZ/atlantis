package com.oowanghan.atlantis.util.str;

import com.oowanghan.atlantis.util.collection.ArrayUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 字符串工具类
 *
 * @author wanghan
 */
public class StringUtil extends CharSequenceUtil implements StringConstant {

	// ------------------------------------------------------------------------ Blank

	/**
	 * <p>如果对象是字符串是否为空白，空白的定义如下：</p>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>空字符串：{@code ""}</li>
	 *     <li>空格、全角空格、制表符、换行符，等不可见字符</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isBlankIfStr(null)     // true}</li>
	 *     <li>{@code StrUtil.isBlankIfStr("")       // true}</li>
	 *     <li>{@code StrUtil.isBlankIfStr(" \t\n")  // true}</li>
	 *     <li>{@code StrUtil.isBlankIfStr("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isEmptyIfStr(Object)} 的区别是：
	 * 该方法会校验空白字符，且性能相对于 {@link #isEmptyIfStr(Object)} 略慢。</p>
	 *
	 * @param obj 对象
	 * @return 如果为字符串是否为空串
	 * @see StrUtil#isBlank(CharSequence)
	 *  
	 */
	public static boolean isBlankIfStr(Object obj) {
		if (null == obj) {
			return true;
		} else if (obj instanceof CharSequence) {
			return isBlank((CharSequence) obj);
		}
		return false;
	}
	// ------------------------------------------------------------------------ Empty

	/**
	 * <p>如果对象是字符串是否为空串，空的定义如下：</p><br>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>空字符串：{@code ""}</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isEmptyIfStr(null)     // true}</li>
	 *     <li>{@code StrUtil.isEmptyIfStr("")       // true}</li>
	 *     <li>{@code StrUtil.isEmptyIfStr(" \t\n")  // false}</li>
	 *     <li>{@code StrUtil.isEmptyIfStr("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isBlankIfStr(Object)} 的区别是：该方法不校验空白字符。</p>
	 *
	 * @param obj 对象
	 * @return 如果为字符串是否为空串
	 *  
	 */
	public static boolean isEmptyIfStr(Object obj) {
		if (null == obj) {
			return true;
		} else if (obj instanceof CharSequence) {
			return 0 == ((CharSequence) obj).length();
		}
		return false;
	}

	// ------------------------------------------------------------------------ Trim

	/**
	 * 给定字符串数组全部做去首尾空格
	 *
	 * @param strs 字符串数组
	 */
	public static void trim(String[] strs) {
		if (null == strs) {
			return;
		}
		String str;
		for (int i = 0; i < strs.length; i++) {
			str = strs[i];
			if (null != str) {
				strs[i] = trim(str);
			}
		}
	}

	/**
	 * 将byte数组转为字符串
	 *
	 * @param bytes   byte数组
	 * @param charset 字符集
	 * @return 字符串
	 */
	public static String str(byte[] bytes, String charset) {
		return str(bytes, CharsetUtil.charset(charset));
	}

	/**
	 * 解码字节码
	 *
	 * @param data    字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 解码后的字符串
	 */
	public static String str(byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		if (null == charset) {
			return new String(data);
		}
		return new String(data, charset);
	}

	/**
	 * 将Byte数组转为字符串
	 *
	 * @param bytes   byte数组
	 * @param charset 字符集
	 * @return 字符串
	 */
	public static String str(Byte[] bytes, String charset) {
		return str(bytes, CharsetUtil.charset(charset));
	}

	/**
	 * 解码字节码
	 *
	 * @param data    字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 解码后的字符串
	 */
	public static String str(Byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		byte[] bytes = new byte[data.length];
		Byte dataByte;
		for (int i = 0; i < data.length; i++) {
			dataByte = data[i];
			bytes[i] = (null == dataByte) ? -1 : dataByte;
		}

		return str(bytes, charset);
	}

	/**
	 * 将编码的byteBuffer数据转换为字符串
	 *
	 * @param data    数据
	 * @param charset 字符集，如果为空使用当前系统字符集
	 * @return 字符串
	 */
	public static String str(ByteBuffer data, String charset) {
		if (data == null) {
			return null;
		}

		return str(data, CharsetUtil.charset(charset));
	}

	/**
	 * 将编码的byteBuffer数据转换为字符串
	 *
	 * @param data    数据
	 * @param charset 字符集，如果为空使用当前系统字符集
	 * @return 字符串
	 */
	public static String str(ByteBuffer data, Charset charset) {
		if (null == charset) {
			charset = Charset.defaultCharset();
		}
		return charset.decode(data).toString();
	}

	/**
	 * 调用对象的toString方法，null会返回“null”
	 *
	 * @param obj 对象
	 * @return 字符串
	 *  
	 * @see String#valueOf(Object)
	 */
	public static String toString(Object obj) {
		return String.valueOf(obj);
	}

	/**
	 * 调用对象的toString方法，null会返回{@code null}
	 *
	 * @param obj 对象
	 * @return 字符串 or {@code null}
	 *  
	 */
	public static String toStringOrNull(Object obj) {
		return null == obj ? null : obj.toString();
	}



	/**
	 * 格式化文本，使用 {varName} 占位<br>
	 * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
	 *
	 * @param template 文本模板，被替换的部分用 {key} 表示
	 * @param map      参数值对
	 * @return 格式化后的文本
	 */
	public static String format(CharSequence template, Map<?, ?> map) {
		return format(template, map, true);
	}

	/**
	 * 格式化文本，使用 {varName} 占位<br>
	 * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
	 *
	 * @param template   文本模板，被替换的部分用 {key} 表示
	 * @param map        参数值对
	 * @param ignoreNull 是否忽略 {@code null} 值，忽略则 {@code null} 值对应的变量不被替换，否则替换为""
	 * @return 格式化后的文本
	 *  
	 */
	public static String format(CharSequence template, Map<?, ?> map, boolean ignoreNull) {
		return StringFormatterUtil.format(template, map, ignoreNull);
	}

	/**
	 * 将对象转为字符串<br>
	 *
	 * <pre>
	 * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
	 * 2、对象数组会调用Arrays.toString方法
	 * </pre>
	 *
	 * @param obj 对象
	 * @return 字符串
	 */
	public static String utf8Str(Object obj) {
		return str(obj, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将对象转为字符串
	 * <pre>
	 * 	 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
	 * 	 2、对象数组会调用Arrays.toString方法
	 * </pre>
	 *
	 * @param obj     对象
	 * @param charset 字符集
	 * @return 字符串
	 */
	public static String str(Object obj, Charset charset) {
		if (null == obj) {
			return null;
		}

		if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof byte[]) {
			return str((byte[]) obj, charset);
		} else if (obj instanceof Byte[]) {
			return str((Byte[]) obj, charset);
		} else if (obj instanceof ByteBuffer) {
			return str((ByteBuffer) obj, charset);
		} else if (ArrayUtil.isArray(obj)) {
			return ArrayUtil.toString(obj);
		}

		return obj.toString();
	}


	/**
	 * 替换字符串中的指定字符串
	 *
	 * @param str         字符串
	 * @param searchStr   被查找的字符串
	 * @param replacement 被替换的字符串
	 * @return 替换后的字符串
	 *  
	 */
	public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement) {
		return replace(str, 0, searchStr, replacement, false);
	}

	/**
	 * 替换字符串中的指定字符串
	 *
	 * @param str         字符串
	 * @param searchStr   被查找的字符串
	 * @param replacement 被替换的字符串
	 * @param ignoreCase  是否忽略大小写
	 * @return 替换后的字符串
	 *  
	 */
	public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
		return replace(str, 0, searchStr, replacement, ignoreCase);
	}

	/**
	 * 替换字符串中的指定字符串
	 *
	 * @param str         字符串
	 * @param fromIndex   开始位置（包括）
	 * @param searchStr   被查找的字符串
	 * @param replacement 被替换的字符串
	 * @param ignoreCase  是否忽略大小写
	 * @return 替换后的字符串
	 *  
	 */
	public static String replace(CharSequence str, int fromIndex, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
		if (isEmpty(str) || isEmpty(searchStr)) {
			return str(str);
		}
		if (null == replacement) {
			replacement = EMPTY;
		}

		final int strLength = str.length();
		final int searchStrLength = searchStr.length();
		if (strLength < searchStrLength) {
			// issue#I4M16G@Gitee
			return str(str);
		}

		if (fromIndex > strLength) {
			return str(str);
		} else if (fromIndex < 0) {
			fromIndex = 0;
		}

		final StringBuilder result = new StringBuilder(strLength - searchStrLength + replacement.length());
		if (0 != fromIndex) {
			result.append(str.subSequence(0, fromIndex));
		}

		int preIndex = fromIndex;
		int index;
		while ((index = indexOf(str, searchStr, preIndex, ignoreCase)) > -1) {
			result.append(str.subSequence(preIndex, index));
			result.append(replacement);
			preIndex = index + searchStrLength;
		}

		if (preIndex < strLength) {
			// 结尾部分
			result.append(str.subSequence(preIndex, strLength));
		}
		return result.toString();
	}

	/**
	 * 如果给定字符串不是以suffix结尾的，在尾部补充 suffix
	 *
	 * @param str    字符串
	 * @param suffix 后缀
	 * @return 补充后的字符串
	 * @see #appendIfMissing(CharSequence, CharSequence, CharSequence...)
	 */
	public static String addSuffixIfNot(CharSequence str, CharSequence suffix) {
		return appendIfMissing(str, suffix, suffix);
	}

	public static String appendIfMissing(CharSequence str, CharSequence suffix, CharSequence... suffixes) {
		return appendIfMissing(str, suffix, false, suffixes);
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串
	 *
	 * @param str          被检查的字符串
	 * @param suffix       需要添加到结尾的字符串，不参与检查匹配
	 * @param ignoreCase   检查结尾时是否忽略大小写
	 * @param testSuffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 *  
	 */
	public static String appendIfMissing(CharSequence str, CharSequence suffix, boolean ignoreCase, CharSequence... testSuffixes) {
		if (str == null || isEmpty(suffix) || endWith(str, suffix, ignoreCase)) {
			return str(str);
		}
		if (ArrayUtil.isNotEmpty(testSuffixes)) {
			for (final CharSequence testSuffix : testSuffixes) {
				if (endWith(str, testSuffix, ignoreCase)) {
					return str.toString();
				}
			}
		}
		return str.toString().concat(suffix.toString());
	}
}
