package com.oowanghan.atlantis.util.str;

import com.oowanghan.atlantis.util.collection.ArrayUtil;
import com.oowanghan.atlantis.util.tool.finder.CharFinder;
import com.oowanghan.atlantis.util.tool.finder.StringFinder;
import com.oowanghan.atlantis.util.number.NumberUtil;
import org.springframework.util.Assert;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * {@link CharSequence} 相关工具类封装
 */
public class CharSequenceUtil {

	public static final int INDEX_NOT_FOUND = -1;

	/**
	 * 字符串常量：{@code "null"} <br>
	 * 注意：{@code "null" != null}
	 */
	public static final String NULL = "null";

	/**
	 * 字符串常量：空字符串 {@code ""}
	 */
	public static final String EMPTY = "";

	/**
	 * 字符串常量：空格符 {@code " "}
	 */
	public static final String SPACE = " ";

	/**
	 * <p>字符串是否为空白，空白的定义如下：</p>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>空字符串：{@code ""}</li>
	 *     <li>空格、全角空格、制表符、换行符，等不可见字符</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isBlank(null)     // true}</li>
	 *     <li>{@code StrUtil.isBlank("")       // true}</li>
	 *     <li>{@code StrUtil.isBlank(" \t\n")  // true}</li>
	 *     <li>{@code StrUtil.isBlank("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isEmpty(CharSequence)} 的区别是：
	 * 该方法会校验空白字符，且性能相对于 {@link #isEmpty(CharSequence)} 略慢。</p>
	 * <br>
	 *
	 * <p>建议：</p>
	 * <ul>
	 *     <li>该方法建议仅对于客户端（或第三方接口）传入的参数使用该方法。</li>
	 *     <li>需要同时校验多个字符串时，建议采用 {@link #hasBlank(CharSequence...)} 或 {@link #isAllBlank(CharSequence...)}</li>
	 * </ul>
	 *
	 * @param str 被检测的字符串
	 * @return 若为空白，则返回 true
	 * @see #isEmpty(CharSequence)
	 */
	public static boolean isBlank(CharSequence str) {
		int length;

		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < length; i++) {
			// 只要有一个非空字符即为非空字符串
			if (false == CharUtil.isBlankChar(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <p>字符串是否为非空白，非空白的定义如下： </p>
	 * <ol>
	 *     <li>不为 {@code null}</li>
	 *     <li>不为空字符串：{@code ""}</li>
	 *     <li>不为空格、全角空格、制表符、换行符，等不可见字符</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isNotBlank(null)     // false}</li>
	 *     <li>{@code StrUtil.isNotBlank("")       // false}</li>
	 *     <li>{@code StrUtil.isNotBlank(" \t\n")  // false}</li>
	 *     <li>{@code StrUtil.isNotBlank("abc")    // true}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isNotEmpty(CharSequence)} 的区别是：
	 * 该方法会校验空白字符，且性能相对于 {@link #isNotEmpty(CharSequence)} 略慢。</p>
	 * <p>建议：仅对于客户端（或第三方接口）传入的参数使用该方法。</p>
	 *
	 * @param str 被检测的字符串
	 * @return 是否为非空
	 * @see #isBlank(CharSequence)
	 */
	public static boolean isNotBlank(CharSequence str) {
		return false == isBlank(str);
	}

	/**
	 * <p>指定字符串数组中，是否包含空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者其中的任意一个元素是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.hasBlank()                  // true}</li>
	 *     <li>{@code StrUtil.hasBlank("", null, " ")     // true}</li>
	 *     <li>{@code StrUtil.hasBlank("123", " ")        // true}</li>
	 *     <li>{@code StrUtil.hasBlank("123", "abc")      // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isAllBlank(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>hasBlank(CharSequence...)            等价于 {@code isBlank(...) || isBlank(...) || ...}</li>
	 *     <li>{@link #isAllBlank(CharSequence...)} 等价于 {@code isBlank(...) && isBlank(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 字符串列表
	 * @return 是否包含空字符串
	 */
	public static boolean hasBlank(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isBlank(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>指定字符串数组中的元素，是否全部为空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者所有元素都是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isAllBlank()                  // true}</li>
	 *     <li>{@code StrUtil.isAllBlank("", null, " ")     // true}</li>
	 *     <li>{@code StrUtil.isAllBlank("123", " ")        // false}</li>
	 *     <li>{@code StrUtil.isAllBlank("123", "abc")      // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #hasBlank(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>{@link #hasBlank(CharSequence...)}   等价于 {@code isBlank(...) || isBlank(...) || ...}</li>
	 *     <li>isAllBlank(CharSequence...)          等价于 {@code isBlank(...) && isBlank(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 字符串列表
	 * @return 所有字符串是否为空白
	 */
	public static boolean isAllBlank(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isNotBlank(str)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>字符串是否为空，空的定义如下：</p>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>空字符串：{@code ""}</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isEmpty(null)     // true}</li>
	 *     <li>{@code StrUtil.isEmpty("")       // true}</li>
	 *     <li>{@code StrUtil.isEmpty(" \t\n")  // false}</li>
	 *     <li>{@code StrUtil.isEmpty("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isBlank(CharSequence)} 的区别是：该方法不校验空白字符。</p>
	 * <p>建议：</p>
	 * <ul>
	 *     <li>该方法建议用于工具类或任何可以预期的方法参数的校验中。</li>
	 *     <li>需要同时校验多个字符串时，建议采用 {@link #hasEmpty(CharSequence...)} 或 {@link #isAllEmpty(CharSequence...)}</li>
	 * </ul>
	 *
	 * @param str 被检测的字符串
	 * @return 是否为空
	 * @see #isBlank(CharSequence)
	 */
	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

	/**
	 * <p>字符串是否为非空白，非空白的定义如下： </p>
	 * <ol>
	 *     <li>不为 {@code null}</li>
	 *     <li>不为空字符串：{@code ""}</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isNotEmpty(null)     // false}</li>
	 *     <li>{@code StrUtil.isNotEmpty("")       // false}</li>
	 *     <li>{@code StrUtil.isNotEmpty(" \t\n")  // true}</li>
	 *     <li>{@code StrUtil.isNotEmpty("abc")    // true}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isNotBlank(CharSequence)} 的区别是：该方法不校验空白字符。</p>
	 * <p>建议：该方法建议用于工具类或任何可以预期的方法参数的校验中。</p>
	 *
	 * @param str 被检测的字符串
	 * @return 是否为非空
	 * @see #isEmpty(CharSequence)
	 */
	public static boolean isNotEmpty(CharSequence str) {
		return false == isEmpty(str);
	}

	/**
	 * 当给定字符串为null时，转换为Empty
	 *
	 * @param str 被检查的字符串
	 * @return 原字符串或者空串
	 * @see #nullToEmpty(CharSequence)
	 *  
	 */
	public static String emptyIfNull(CharSequence str) {
		return nullToEmpty(str);
	}

	/**
	 * 当给定字符串为null时，转换为Empty
	 *
	 * @param str 被转换的字符串
	 * @return 转换后的字符串
	 */
	public static String nullToEmpty(CharSequence str) {
		return nullToDefault(str, EMPTY);
	}

	/**
	 * 如果字符串是 {@code null}，则返回指定默认字符串，否则返回字符串本身。
	 *
	 * <pre>
	 * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
	 * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
	 * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 *
	 * @param str        要转换的字符串
	 * @param defaultStr 默认字符串
	 * @return 字符串本身或指定的默认字符串
	 */
	public static String nullToDefault(CharSequence str, String defaultStr) {
		return (str == null) ? defaultStr : str.toString();
	}

	/**
	 * 如果字符串是{@code null}或者&quot;&quot;，则返回指定默认字符串，否则返回字符串本身。
	 *
	 * <pre>
	 * emptyToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * emptyToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
	 * emptyToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
	 * emptyToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 *
	 * @param str        要转换的字符串
	 * @param defaultStr 默认字符串
	 * @return 字符串本身或指定的默认字符串
	 *  
	 */
	public static String emptyToDefault(CharSequence str, String defaultStr) {
		return isEmpty(str) ? defaultStr : str.toString();
	}

	/**
	 * 如果字符串是{@code null}或者&quot;&quot;或者空白，则返回指定默认字符串，否则返回字符串本身。
	 *
	 * <pre>
	 * blankToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * blankToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
	 * blankToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;default&quot;
	 * blankToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 *
	 * @param str        要转换的字符串
	 * @param defaultStr 默认字符串
	 * @return 字符串本身或指定的默认字符串
	 *  
	 */
	public static String blankToDefault(CharSequence str, String defaultStr) {
		return isBlank(str) ? defaultStr : str.toString();
	}

	/**
	 * 当给定字符串为空字符串时，转换为{@code null}
	 *
	 * @param str 被转换的字符串
	 * @return 转换后的字符串
	 */
	public static String emptyToNull(CharSequence str) {
		return isEmpty(str) ? null : str.toString();
	}

	/**
	 * <p>是否包含空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者其中的任意一个元素是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.hasEmpty()                  // true}</li>
	 *     <li>{@code StrUtil.hasEmpty("", null)          // true}</li>
	 *     <li>{@code StrUtil.hasEmpty("123", "")         // true}</li>
	 *     <li>{@code StrUtil.hasEmpty("123", "abc")      // false}</li>
	 *     <li>{@code StrUtil.hasEmpty(" ", "\t", "\n")   // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isAllEmpty(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>hasEmpty(CharSequence...)            等价于 {@code isEmpty(...) || isEmpty(...) || ...}</li>
	 *     <li>{@link #isAllEmpty(CharSequence...)} 等价于 {@code isEmpty(...) && isEmpty(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 字符串列表
	 * @return 是否包含空字符串
	 */
	public static boolean hasEmpty(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>指定字符串数组中的元素，是否全部为空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者所有元素都是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isAllEmpty()                  // true}</li>
	 *     <li>{@code StrUtil.isAllEmpty("", null)          // true}</li>
	 *     <li>{@code StrUtil.isAllEmpty("123", "")         // false}</li>
	 *     <li>{@code StrUtil.isAllEmpty("123", "abc")      // false}</li>
	 *     <li>{@code StrUtil.isAllEmpty(" ", "\t", "\n")   // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #hasEmpty(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>{@link #hasEmpty(CharSequence...)}   等价于 {@code isEmpty(...) || isEmpty(...) || ...}</li>
	 *     <li>isAllEmpty(CharSequence...)          等价于 {@code isEmpty(...) && isEmpty(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 字符串列表
	 * @return 所有字符串是否为空白
	 */
	public static boolean isAllEmpty(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isNotEmpty(str)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>指定字符串数组中的元素，是否都不为空字符串。</p>
	 * <p>如果指定的字符串数组的长度不为 0，或者所有元素都不是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isAllNotEmpty()                  // false}</li>
	 *     <li>{@code StrUtil.isAllNotEmpty("", null)          // false}</li>
	 *     <li>{@code StrUtil.isAllNotEmpty("123", "")         // false}</li>
	 *     <li>{@code StrUtil.isAllNotEmpty("123", "abc")      // true}</li>
	 *     <li>{@code StrUtil.isAllNotEmpty(" ", "\t", "\n")   // true}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isAllEmpty(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>{@link #isAllEmpty(CharSequence...)}    等价于 {@code isEmpty(...) && isEmpty(...) && ...}</li>
	 *     <li>isAllNotEmpty(CharSequence...)          等价于 {@code !isEmpty(...) && !isEmpty(...) && ...}</li>
	 * </ul>
	 *
	 * @param args 字符串数组
	 * @return 所有字符串是否都不为为空白
	 *  
	 */
	public static boolean isAllNotEmpty(CharSequence... args) {
		return false == hasEmpty(args);
	}

	/**
	 * 是否存都不为{@code null}或空对象或空白符的对象，通过{@link #hasBlank(CharSequence...)} 判断元素
	 *
	 * @param args 被检查的对象,一个或者多个
	 * @return 是否都不为空
	 *  
	 */
	public static boolean isAllNotBlank(CharSequence... args) {
		return false == hasBlank(args);
	}

	/**
	 * 检查字符串是否为null、“null”、“undefined”
	 *
	 * @param str 被检查的字符串
	 * @return 是否为null、“null”、“undefined”
	 *  
	 */
	public static boolean isNullOrUndefined(CharSequence str) {
		if (null == str) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 检查字符串是否为null、“”、“null”、“undefined”
	 *
	 * @param str 被检查的字符串
	 * @return 是否为null、“”、“null”、“undefined”
	 *  
	 */
	public static boolean isEmptyOrUndefined(CharSequence str) {
		if (isEmpty(str)) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 检查字符串是否为null、空白串、“null”、“undefined”
	 *
	 * @param str 被检查的字符串
	 * @return 是否为null、空白串、“null”、“undefined”
	 *  
	 */
	public static boolean isBlankOrUndefined(CharSequence str) {
		if (isBlank(str)) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 是否为“null”、“undefined”，不做空指针检查
	 *
	 * @param str 字符串
	 * @return 是否为“null”、“undefined”
	 */
	private static boolean isNullOrUndefinedStr(CharSequence str) {
		String strString = str.toString().trim();
		return NULL.equals(strString) || "undefined".equals(strString);
	}

	// ------------------------------------------------------------------------ Trim

	/**
	 * 除去字符串头尾部的空白，如果字符串是{@code null}，依然返回{@code null}。
	 *
	 * <p>
	 * 注意，和{@link String#trim()}不同，此方法使用{@link CharUtil#isBlankChar(char)} 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 *
	 * <pre>
	 * trim(null)          = null
	 * trim(&quot;&quot;)            = &quot;&quot;
	 * trim(&quot;     &quot;)       = &quot;&quot;
	 * trim(&quot;abc&quot;)         = &quot;abc&quot;
	 * trim(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 *
	 * @param str 要处理的字符串
	 * @return 除去头尾空白的字符串，如果原字串为{@code null}，则返回{@code null}
	 */
	public static String trim(CharSequence str) {
		return (null == str) ? null : trim(str, 0);
	}

	/**
	 * 除去字符串头尾部的空白，如果字符串是{@code null}，返回{@code ""}。
	 *
	 * <pre>
	 * StrUtil.trimToEmpty(null)          = ""
	 * StrUtil.trimToEmpty("")            = ""
	 * StrUtil.trimToEmpty("     ")       = ""
	 * StrUtil.trimToEmpty("abc")         = "abc"
	 * StrUtil.trimToEmpty("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str 字符串
	 * @return 去除两边空白符后的字符串, 如果为null返回""
	 *  
	 */
	public static String trimToEmpty(CharSequence str) {
		return str == null ? EMPTY : trim(str);
	}

	/**
	 * 除去字符串头尾部的空白，如果字符串是{@code null}或者""，返回{@code null}。
	 *
	 * <pre>
	 * StrUtil.trimToNull(null)          = null
	 * StrUtil.trimToNull("")            = null
	 * StrUtil.trimToNull("     ")       = null
	 * StrUtil.trimToNull("abc")         = "abc"
	 * StrUtil.trimToEmpty("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str 字符串
	 * @return 去除两边空白符后的字符串, 如果为空返回null
	 *  
	 */
	public static String trimToNull(CharSequence str) {
		final String trimStr = trim(str);
		return EMPTY.equals(trimStr) ? null : trimStr;
	}

	/**
	 * 除去字符串头部的空白，如果字符串是{@code null}，则返回{@code null}。
	 *
	 * <p>
	 * 注意，和{@link String#trim()}不同，此方法使用{@link CharUtil#isBlankChar(char)} 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 *
	 * <pre>
	 * trimStart(null)         = null
	 * trimStart(&quot;&quot;)           = &quot;&quot;
	 * trimStart(&quot;abc&quot;)        = &quot;abc&quot;
	 * trimStart(&quot;  abc&quot;)      = &quot;abc&quot;
	 * trimStart(&quot;abc  &quot;)      = &quot;abc  &quot;
	 * trimStart(&quot; abc &quot;)      = &quot;abc &quot;
	 * </pre>
	 *
	 * @param str 要处理的字符串
	 * @return 除去空白的字符串，如果原字串为{@code null}或结果字符串为{@code ""}，则返回 {@code null}
	 */
	public static String trimStart(CharSequence str) {
		return trim(str, -1);
	}

	/**
	 * 除去字符串尾部的空白，如果字符串是{@code null}，则返回{@code null}。
	 *
	 * <p>
	 * 注意，和{@link String#trim()}不同，此方法使用{@link CharUtil#isBlankChar(char)} 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 *
	 * <pre>
	 * trimEnd(null)       = null
	 * trimEnd(&quot;&quot;)         = &quot;&quot;
	 * trimEnd(&quot;abc&quot;)      = &quot;abc&quot;
	 * trimEnd(&quot;  abc&quot;)    = &quot;  abc&quot;
	 * trimEnd(&quot;abc  &quot;)    = &quot;abc&quot;
	 * trimEnd(&quot; abc &quot;)    = &quot; abc&quot;
	 * </pre>
	 *
	 * @param str 要处理的字符串
	 * @return 除去空白的字符串，如果原字串为{@code null}或结果字符串为{@code ""}，则返回 {@code null}
	 */
	public static String trimEnd(CharSequence str) {
		return trim(str, 1);
	}

	/**
	 * 除去字符串头尾部的空白符，如果字符串是{@code null}，依然返回{@code null}。
	 *
	 * @param str  要处理的字符串
	 * @param mode {@code -1}表示trimStart，{@code 0}表示trim全部， {@code 1}表示trimEnd
	 * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
	 */
	public static String trim(CharSequence str, int mode) {
		return trim(str, mode, CharUtil::isBlankChar);
	}

	/**
	 * 按照断言，除去字符串头尾部的断言为真的字符，如果字符串是{@code null}，依然返回{@code null}。
	 *
	 * @param str       要处理的字符串
	 * @param mode      {@code -1}表示trimStart，{@code 0}表示trim全部， {@code 1}表示trimEnd
	 * @param predicate 断言是否过掉字符，返回{@code true}表述过滤掉，{@code false}表示不过滤
	 * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
	 *  
	 */
	public static String trim(CharSequence str, int mode, Predicate<Character> predicate) {
		String result;
		if (str == null) {
			result = null;
		} else {
			int length = str.length();
			int start = 0;
			int end = length;// 扫描字符串头部
			if (mode <= 0) {
				while ((start < end) && (predicate.test(str.charAt(start)))) {
					start++;
				}
			}// 扫描字符串尾部
			if (mode >= 0) {
				while ((start < end) && (predicate.test(str.charAt(end - 1)))) {
					end--;
				}
			}
			if ((start > 0) || (end < length)) {
				result = str.toString().substring(start, end);
			} else {
				result = str.toString();
			}
		}

		return result;
	}

	// ------------------------------------------------------------------------ startWith

	/**
	 * 字符串是否以给定字符开始
	 *
	 * @param str 字符串
	 * @param c   字符
	 * @return 是否开始
	 */
	public static boolean startWith(CharSequence str, char c) {
		if (isEmpty(str)) {
			return false;
		}
		return c == str.charAt(0);
	}

	/**
	 * 是否以指定字符串开头<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
	 *
	 * @param str        被监测字符串
	 * @param prefix     开头字符串
	 * @param ignoreCase 是否忽略大小写
	 * @return 是否以指定字符串开头
	 *  
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
		return startWith(str, prefix, ignoreCase, false);
	}

	/**
	 * 是否以指定字符串开头<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
	 *
	 * @param str          被监测字符串
	 * @param prefix       开头字符串
	 * @param ignoreCase   是否忽略大小写
	 * @param ignoreEquals 是否忽略字符串相等的情况
	 * @return 是否以指定字符串开头
	 *  
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
		if (null == str || null == prefix) {
			if (ignoreEquals) {
				return false;
			}
			return null == str && null == prefix;
		}

		boolean isStartWith;
		if (ignoreCase) {
			isStartWith = str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
		} else {
			isStartWith = str.toString().startsWith(prefix.toString());
		}

		if (isStartWith) {
			return (false == ignoreEquals) || (false == equals(str, prefix, ignoreCase));
		}
		return false;
	}

	/**
	 * 是否以指定字符串开头
	 *
	 * @param str    被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix) {
		return startWith(str, prefix, false);
	}

	/**
	 * 是否以指定字符串开头，忽略相等字符串的情况
	 *
	 * @param str    被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头并且两个字符串不相等
	 */
	public static boolean startWithIgnoreEquals(CharSequence str, CharSequence prefix) {
		return startWith(str, prefix, false, true);
	}

	/**
	 * 是否以指定字符串开头，忽略大小写
	 *
	 * @param str    被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWithIgnoreCase(CharSequence str, CharSequence prefix) {
		return startWith(str, prefix, true);
	}

	/**
	 * 给定字符串是否以任何一个字符串开始<br>
	 * 给定字符串和数组为空都返回false
	 *
	 * @param str      给定字符串
	 * @param prefixes 需要检测的开始字符串
	 * @return 给定字符串是否以任何一个字符串开始
	 *  
	 */
	public static boolean startWithAny(CharSequence str, CharSequence... prefixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
			return false;
		}

		for (CharSequence suffix : prefixes) {
			if (startWith(str, suffix, false)) {
				return true;
			}
		}
		return false;
	}

	// ------------------------------------------------------------------------ endWith

	/**
	 * 字符串是否以给定字符结尾
	 *
	 * @param str 字符串
	 * @param c   字符
	 * @return 是否结尾
	 */
	public static boolean endWith(CharSequence str, char c) {
		if (isEmpty(str)) {
			return false;
		}
		return c == str.charAt(str.length() - 1);
	}

	/**
	 * 是否以指定字符串结尾<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
	 *
	 * @param str          被监测字符串
	 * @param suffix       结尾字符串
	 * @param isIgnoreCase 是否忽略大小写
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWith(CharSequence str, CharSequence suffix, boolean isIgnoreCase) {
		if (null == str || null == suffix) {
			return null == str && null == suffix;
		}

		if (isIgnoreCase) {
			return str.toString().toLowerCase().endsWith(suffix.toString().toLowerCase());
		} else {
			return str.toString().endsWith(suffix.toString());
		}
	}

	/**
	 * 是否以指定字符串结尾
	 *
	 * @param str    被监测字符串
	 * @param suffix 结尾字符串
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWith(CharSequence str, CharSequence suffix) {
		return endWith(str, suffix, false);
	}

	/**
	 * 是否以指定字符串结尾，忽略大小写
	 *
	 * @param str    被监测字符串
	 * @param suffix 结尾字符串
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWithIgnoreCase(CharSequence str, CharSequence suffix) {
		return endWith(str, suffix, true);
	}

	/**
	 * 给定字符串是否以任何一个字符串结尾<br>
	 * 给定字符串和数组为空都返回false
	 *
	 * @param str      给定字符串
	 * @param suffixes 需要检测的结尾字符串
	 * @return 给定字符串是否以任何一个字符串结尾
	 *  
	 */
	public static boolean endWithAny(CharSequence str, CharSequence... suffixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
			return false;
		}

		for (CharSequence suffix : suffixes) {
			if (endWith(str, suffix, false)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 给定字符串是否以任何一个字符串结尾（忽略大小写）<br>
	 * 给定字符串和数组为空都返回false
	 *
	 * @param str      给定字符串
	 * @param suffixes 需要检测的结尾字符串
	 * @return 给定字符串是否以任何一个字符串结尾
	 *  
	 */
	public static boolean endWithAnyIgnoreCase(CharSequence str, CharSequence... suffixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
			return false;
		}

		for (CharSequence suffix : suffixes) {
			if (endWith(str, suffix, true)) {
				return true;
			}
		}
		return false;
	}

	// ------------------------------------------------------------------------ contains

	/**
	 * 指定范围内查找字符串
	 *
	 * @param text       字符串，空则返回-1
	 * @param searchStr  需要查找位置的字符串，空则返回-1
	 * @param from       起始位置（包含）
	 * @param ignoreCase 是否忽略大小写
	 * @return 位置
	 *  
	 */
	public static int indexOf(CharSequence text, CharSequence searchStr, int from, boolean ignoreCase) {
		if (isEmpty(text) || isEmpty(searchStr)) {
			if (StringUtil.equals(text, searchStr)) {
				return 0;
			} else {
				return INDEX_NOT_FOUND;
			}
		}
		return new StringFinder(searchStr, ignoreCase).setText(text).start(from);
	}

	/**
	 * 指定字符是否在字符串中出现过
	 *
	 * @param str        字符串
	 * @param searchChar 被查找的字符
	 * @return 是否包含
	 *  
	 */
	public static boolean contains(CharSequence str, char searchChar) {
		return indexOf(str, searchChar) > -1;
	}

	/**
	 * 指定范围内查找指定字符
	 *
	 * @param str        字符串
	 * @param searchChar 被查找的字符
	 * @return 位置
	 */
	public static int indexOf(CharSequence str, char searchChar) {
		return indexOf(str, searchChar, 0);
	}

	/**
	 * 指定范围内查找指定字符
	 *
	 * @param str        字符串
	 * @param searchChar 被查找的字符
	 * @param start      起始位置，如果小于0，从0开始查找
	 * @return 位置
	 */
	public static int indexOf(CharSequence str, char searchChar, int start) {
		if (str instanceof String) {
			return ((String) str).indexOf(searchChar, start);
		} else {
			return indexOf(str, searchChar, start, -1);
		}
	}

	/**
	 * 指定范围内查找指定字符
	 *
	 * @param text       字符串
	 * @param searchChar 被查找的字符
	 * @param start      起始位置，如果小于0，从0开始查找
	 * @param end        终止位置，如果超过str.length()则默认查找到字符串末尾
	 * @return 位置
	 */
	public static int indexOf(CharSequence text, char searchChar, int start, int end) {
		if (isEmpty(text)) {
			return INDEX_NOT_FOUND;
		}
		return new CharFinder(searchChar).setText(text).setEndIndex(end).start(start);
	}

	/**
	 * 指定字符串是否在字符串中出现过
	 *
	 * @param str       字符串
	 * @param searchStr 被查找的字符串
	 * @return 是否包含
	 *  
	 */
	public static boolean contains(CharSequence str, CharSequence searchStr) {
		if (null == str || null == searchStr) {
			return false;
		}
		return str.toString().contains(searchStr);
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串
	 *
	 * @param str      指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 是否包含任意一个字符串
	 *  
	 */
	public static boolean containsAny(CharSequence str, CharSequence... testStrs) {
		return null != getContainsStr(str, testStrs);
	}

	/**
	 * 给定字符串是否包含空白符（空白符包括空格、制表符、全角空格和不间断空格）<br>
	 * 如果给定字符串为null或者""，则返回false
	 *
	 * @param str 字符串
	 * @return 是否包含空白符
	 */
	public static boolean containsBlank(CharSequence str) {
		if (null == str) {
			return false;
		}
		final int length = str.length();
		if (0 == length) {
			return false;
		}

		for (int i = 0; i < length; i += 1) {
			if (CharUtil.isBlankChar(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串
	 *
	 * @param str      指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 被包含的第一个字符串
	 */
	public static String getContainsStr(CharSequence str, CharSequence... testStrs) {
		if (isEmpty(str) || ArrayUtil.isEmpty(testStrs)) {
			return null;
		}
		for (CharSequence checkStr : testStrs) {
			if (str.toString().contains(checkStr)) {
				return checkStr.toString();
			}
		}
		return null;
	}

	/**
	 * 是否包含特定字符，忽略大小写，如果给定两个参数都为{@code null}，返回true
	 *
	 * @param str     被检测字符串
	 * @param testStr 被测试是否包含的字符串
	 * @return 是否包含
	 */
	public static boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
		if (null == str) {
			// 如果被监测字符串和
			return null == testStr;
		}
		return str.toString().toLowerCase().contains(testStr.toString().toLowerCase());
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串<br>
	 * 忽略大小写
	 *
	 * @param str      指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 是否包含任意一个字符串
	 *  
	 */
	public static boolean containsAnyIgnoreCase(CharSequence str, CharSequence... testStrs) {
		return null != getContainsStrIgnoreCase(str, testStrs);
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串<br>
	 * 忽略大小写
	 *
	 * @param str      指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 被包含的第一个字符串
	 *  
	 */
	public static String getContainsStrIgnoreCase(CharSequence str, CharSequence... testStrs) {
		if (isEmpty(str) || ArrayUtil.isEmpty(testStrs)) {
			return null;
		}
		for (CharSequence testStr : testStrs) {
			if (containsIgnoreCase(str, testStr)) {
				return testStr.toString();
			}
		}
		return null;
	}

	/**
	 * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
	 * 例如：str=setName, preLength=3 =》 return name
	 *
	 * @param str       被处理的字符串
	 * @param preLength 去掉的长度
	 * @return 处理后的字符串，不符合规范返回null
	 */
	public static String removePreAndLowerFirst(CharSequence str, int preLength) {
		if (str == null) {
			return null;
		}
		if (str.length() > preLength) {
			char first = Character.toLowerCase(str.charAt(preLength));
			if (str.length() > preLength + 1) {
				return first + str.toString().substring(preLength + 1);
			}
			return String.valueOf(first);
		} else {
			return str.toString();
		}
	}

	/**
	 * 去掉指定前缀
	 *
	 * @param str    字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
	 */
	public static String removePrefix(CharSequence str, CharSequence prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.startsWith(prefix.toString())) {
			return subSuf(str2, prefix.length());// 截取后半段
		}
		return str2;
	}

	/**
	 * 忽略大小写去掉指定前缀
	 *
	 * @param str    字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
	 */
	public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.toLowerCase().startsWith(prefix.toString().toLowerCase())) {
			return subSuf(str2, prefix.length());// 截取后半段
		}
		return str2;
	}

	/**
	 * 去掉指定后缀
	 *
	 * @param str    字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffix(CharSequence str, CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.endsWith(suffix.toString())) {
			return subPre(str2, str2.length() - suffix.length());// 截取前半段
		}
		return str2;
	}

	/**
	 * 忽略大小写去掉指定后缀
	 *
	 * @param str    字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.toLowerCase().endsWith(suffix.toString().toLowerCase())) {
			return subPre(str2, str2.length() - suffix.length());
		}
		return str2;
	}

	/**
	 * 去除两边的指定字符串
	 *
	 * @param str            被处理的字符串
	 * @param prefixOrSuffix 前缀或后缀
	 * @return 处理后的字符串
	 *  
	 */
	public static String strip(CharSequence str, CharSequence prefixOrSuffix) {
		if (equals(str, prefixOrSuffix)) {
			// 对于去除相同字符的情况单独处理
			return EMPTY;
		}
		return strip(str, prefixOrSuffix, prefixOrSuffix);
	}

	/**
	 * 去除两边的指定字符串
	 *
	 * @param str    被处理的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 处理后的字符串
	 *  
	 */
	public static String strip(CharSequence str, CharSequence prefix, CharSequence suffix) {
		if (isEmpty(str)) {
			return str(str);
		}

		int from = 0;
		int to = str.length();

		String str2 = str.toString();
		if (startWith(str2, prefix)) {
			from = prefix.length();
		}
		if (endWith(str2, suffix)) {
			to -= suffix.length();
		}

		return str2.substring(Math.min(from, to), Math.max(from, to));
	}

	/**
	 * 去除两边的指定字符串，忽略大小写
	 *
	 * @param str            被处理的字符串
	 * @param prefixOrSuffix 前缀或后缀
	 * @return 处理后的字符串
	 *  
	 */
	public static String stripIgnoreCase(CharSequence str, CharSequence prefixOrSuffix) {
		return stripIgnoreCase(str, prefixOrSuffix, prefixOrSuffix);
	}

	/**
	 * 去除两边的指定字符串，忽略大小写
	 *
	 * @param str    被处理的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 处理后的字符串
	 *  
	 */
	public static String stripIgnoreCase(CharSequence str, CharSequence prefix, CharSequence suffix) {
		if (isEmpty(str)) {
			return str(str);
		}
		int from = 0;
		int to = str.length();

		String str2 = str.toString();
		if (startWithIgnoreCase(str2, prefix)) {
			from = prefix.length();
		}
		if (endWithIgnoreCase(str2, suffix)) {
			to -= suffix.length();
		}
		return str2.substring(from, to);
	}

	/**
	 * 切分字符串<br>
	 * a#b#c =》 [a,b,c] <br>
	 * a##b#c =》 [a,"",b,c]
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符字符
	 * @return 切分后的集合
	 */
	public static List<String> split(CharSequence str, char separator) {
		return split(str, separator, 0);
	}

	/**
	 * 切分字符串，如果分隔符不存在则返回原字符串
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符
	 * @return 字符串
	 *  
	 */
	public static String[] splitToArray(CharSequence str, CharSequence separator) {
		if (str == null) {
			return new String[]{};
		}

		return StringSplitter.splitToArray(str.toString(), str(separator), 0, false, false);
	}

	/**
	 * 切分字符串
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符字符
	 * @return 切分后的数组
	 */
	public static String[] splitToArray(CharSequence str, char separator) {
		return splitToArray(str, separator, 0);
	}

	/**
	 * 切分字符串
	 *
	 * @param text      被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit     限制分片数
	 * @return 切分后的数组
	 */
	public static String[] splitToArray(CharSequence text, char separator, int limit) {
		Assert.notNull(text, "Text must be not null!");
		return StringSplitter.splitToArray(text.toString(), separator, limit, false, false);
	}

	/**
	 * 切分字符串，不去除切分后每个元素两边的空白符，不去除空白项
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit     限制分片数，-1不限制
	 * @return 切分后的集合
	 */
	public static List<String> split(CharSequence str, char separator, int limit) {
		return split(str, separator, limit, false, false);
	}

	/**
	 * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符字符
	 * @return 切分后的集合
	 *  
	 */
	public static List<String> splitTrim(CharSequence str, char separator) {
		return splitTrim(str, separator, -1);
	}

	/**
	 * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符字符
	 * @return 切分后的集合
	 *  
	 */
	public static List<String> splitTrim(CharSequence str, CharSequence separator) {
		return splitTrim(str, separator, -1);
	}

	/**
	 * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit     限制分片数，-1不限制
	 * @return 切分后的集合
	 *  
	 */
	public static List<String> splitTrim(CharSequence str, char separator, int limit) {
		return split(str, separator, limit, true, true);
	}

	/**
	 * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit     限制分片数，-1不限制
	 * @return 切分后的集合
	 *  
	 */
	public static List<String> splitTrim(CharSequence str, CharSequence separator, int limit) {
		return split(str, separator, limit, true, true);
	}

	/**
	 * 切分字符串，不限制分片数量
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 *  
	 */
	public static List<String> split(CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
		return split(str, separator, 0, isTrim, ignoreEmpty);
	}

	/**
	 * 切分字符串
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，-1不限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 *  
	 */
	public static List<String> split(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		return StringSplitter.split(str, separator, limit, isTrim, ignoreEmpty);
	}

	/**
	 * 切分字符串
	 *
	 * @param <R>         切分后元素类型
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，-1不限制
	 * @param ignoreEmpty 是否忽略空串
	 * @param mapping     切分后的字符串元素的转换方法
	 * @return 切分后的集合，元素类型是经过 mapping 转换后的
	 *  
	 */
	public static <R> List<R> split(CharSequence str, char separator, int limit, boolean ignoreEmpty, Function<String, R> mapping) {
		return StringSplitter.split(str, separator, limit, ignoreEmpty, mapping);
	}

	/**
	 * 切分字符串，如果分隔符不存在则返回原字符串
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符
	 * @return 字符串
	 *  
	 */
	public static List<String> split(CharSequence str, CharSequence separator) {
		return split(str, separator, false, false);
	}

	/**
	 * 切分字符串
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 *  
	 */
	public static List<String> split(CharSequence str, CharSequence separator, boolean isTrim, boolean ignoreEmpty) {
		return split(str, separator, 0, isTrim, ignoreEmpty);
	}

	/**
	 * 切分字符串
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，-1不限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 *  
	 */
	public static List<String> split(CharSequence str, CharSequence separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		final String separatorStr = (null == separator) ? null : separator.toString();
		return StringSplitter.split(str, separatorStr, limit, isTrim, ignoreEmpty);
	}

	/**
	 * 根据给定长度，将给定字符串截取为多个部分
	 *
	 * @param str 字符串
	 * @param len 每一个小节的长度
	 * @return 截取后的字符串数组
	 * @see StringSplitter#splitByLength(CharSequence, int)
	 */
	public static String[] split(CharSequence str, int len) {
		return StringSplitter.splitByLength(str, len);
	}

	/**
	 * 将字符串切分为N等份
	 *
	 * @param str        字符串
	 * @param partLength 每等份的长度
	 * @return 切分后的数组
	 *  
	 */
	public static String[] cut(CharSequence str, int partLength) {
		if (null == str) {
			return null;
		}
		int len = str.length();
		if (len < partLength) {
			return new String[]{str.toString()};
		}
		int part = NumberUtil.count(len, partLength);
		final String[] array = new String[part];

		final String str2 = str.toString();
		for (int i = 0; i < part; i++) {
			array[i] = str2.substring(i * partLength, (i == part - 1) ? len : (partLength + i * partLength));
		}
		return array;
	}

	// ------------------------------------------------------------------------ sub

	/**
	 * 改进JDK subString<br>
	 * index从0开始计算，最后一个字符为-1<br>
	 * 如果from和to位置一样，返回 "" <br>
	 * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
	 * 如果经过修正的index中from大于to，则互换from和to example: <br>
	 * abcdefgh 2 3 =》 c <br>
	 * abcdefgh 2 -3 =》 cde <br>
	 *
	 * @param str              String
	 * @param fromIndexInclude 开始的index（包括）
	 * @param toIndexExclude   结束的index（不包括）
	 * @return 字串
	 */
	public static String sub(CharSequence str, int fromIndexInclude, int toIndexExclude) {
		if (isEmpty(str)) {
			return str(str);
		}
		int len = str.length();

		if (fromIndexInclude < 0) {
			fromIndexInclude = len + fromIndexInclude;
			if (fromIndexInclude < 0) {
				fromIndexInclude = 0;
			}
		} else if (fromIndexInclude > len) {
			fromIndexInclude = len;
		}

		if (toIndexExclude < 0) {
			toIndexExclude = len + toIndexExclude;
			if (toIndexExclude < 0) {
				toIndexExclude = len;
			}
		} else if (toIndexExclude > len) {
			toIndexExclude = len;
		}

		if (toIndexExclude < fromIndexInclude) {
			int tmp = fromIndexInclude;
			fromIndexInclude = toIndexExclude;
			toIndexExclude = tmp;
		}

		if (fromIndexInclude == toIndexExclude) {
			return EMPTY;
		}

		return str.toString().substring(fromIndexInclude, toIndexExclude);
	}

	/**
	 * 通过CodePoint截取字符串，可以截断Emoji
	 *
	 * @param str       String
	 * @param fromIndex 开始的index（包括）
	 * @param toIndex   结束的index（不包括）
	 * @return 字串
	 */
	public static String subByCodePoint(CharSequence str, int fromIndex, int toIndex) {
		if (isEmpty(str)) {
			return str(str);
		}

		if (fromIndex < 0 || fromIndex > toIndex) {
			throw new IllegalArgumentException();
		}

		if (fromIndex == toIndex) {
			return EMPTY;
		}

		final StringBuilder sb = new StringBuilder();
		final int subLen = toIndex - fromIndex;
		str.toString().codePoints().skip(fromIndex).limit(subLen).forEach(v -> sb.append(Character.toChars(v)));
		return sb.toString();
	}

	/**
	 * 截取部分字符串，这里一个汉字的长度认为是2
	 *
	 * @param str    字符串
	 * @param len    bytes切割到的位置（包含）
	 * @param suffix 切割后加上后缀
	 * @return 切割后的字符串
	 *  
	 */
	public static String subPreGbk(CharSequence str, int len, CharSequence suffix) {
		return subPreGbk(str, len, true) + suffix;
	}

	/**
	 * 截取部分字符串，这里一个汉字的长度认为是2<br>
	 * 可以自定义halfUp，如len为10，如果截取后最后一个字符是半个字符，{@code true}表示保留，则长度是11，否则长度9
	 *
	 * @param str    字符串
	 * @param len    bytes切割到的位置（包含）
	 * @param halfUp 遇到截取一半的GBK字符，是否保留。
	 * @return 切割后的字符串
	 *  
	 */
	public static String subPreGbk(CharSequence str, int len, boolean halfUp) {
		if (isEmpty(str)) {
			return str(str);
		}

		int counterOfDoubleByte = 0;
		final byte[] b = bytes(str, CharsetUtil.CHARSET_GBK);
		if (b.length <= len) {
			return str.toString();
		}
		for (int i = 0; i < len; i++) {
			if (b[i] < 0) {
				counterOfDoubleByte++;
			}
		}

		if (counterOfDoubleByte % 2 != 0) {
			if (halfUp) {
				len += 1;
			} else {
				len -= 1;
			}
		}
		return new String(b, 0, len, CharsetUtil.CHARSET_GBK);
	}

	/**
	 * 切割指定位置之前部分的字符串
	 *
	 * @param string         字符串
	 * @param toIndexExclude 切割到的位置（不包括）
	 * @return 切割后的剩余的前半部分字符串
	 */
	public static String subPre(CharSequence string, int toIndexExclude) {
		return sub(string, 0, toIndexExclude);
	}

	/**
	 * 切割指定位置之后部分的字符串
	 *
	 * @param string    字符串
	 * @param fromIndex 切割开始的位置（包括）
	 * @return 切割后后剩余的后半部分字符串
	 */
	public static String subSuf(CharSequence string, int fromIndex) {
		if (isEmpty(string)) {
			return null;
		}
		return sub(string, fromIndex, string.length());
	}

	/**
	 * 切割指定长度的后部分的字符串
	 *
	 * <pre>
	 * StrUtil.subSufByLength("abcde", 3)      =    "cde"
	 * StrUtil.subSufByLength("abcde", 0)      =    ""
	 * StrUtil.subSufByLength("abcde", -5)     =    ""
	 * StrUtil.subSufByLength("abcde", -1)     =    ""
	 * StrUtil.subSufByLength("abcde", 5)       =    "abcde"
	 * StrUtil.subSufByLength("abcde", 10)     =    "abcde"
	 * StrUtil.subSufByLength(null, 3)               =    null
	 * </pre>
	 *
	 * @param string 字符串
	 * @param length 切割长度
	 * @return 切割后后剩余的后半部分字符串
	 *  
	 */
	public static String subSufByLength(CharSequence string, int length) {
		if (isEmpty(string)) {
			return null;
		}
		if (length <= 0) {
			return EMPTY;
		}
		return sub(string, -length, string.length());
	}

	/**
	 * 截取字符串,从指定位置开始,截取指定长度的字符串<br>
	 * author weibaohui
	 *
	 * @param input     原始字符串
	 * @param fromIndex 开始的index,包括
	 * @param length    要截取的长度
	 * @return 截取后的字符串
	 */
	public static String subWithLength(String input, int fromIndex, int length) {
		return sub(input, fromIndex, fromIndex + length);
	}

	/**
	 * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
	 * 如果分隔字符串为空串""，则返回空串，如果分隔字符串未找到，返回原字符串，举例如下：
	 *
	 * <pre>
	 * StrUtil.subBefore(null, *, false)      = null
	 * StrUtil.subBefore("", *, false)        = ""
	 * StrUtil.subBefore("abc", "a", false)   = ""
	 * StrUtil.subBefore("abcba", "b", false) = "a"
	 * StrUtil.subBefore("abc", "c", false)   = "ab"
	 * StrUtil.subBefore("abc", "d", false)   = "abc"
	 * StrUtil.subBefore("abc", "", false)    = ""
	 * StrUtil.subBefore("abc", null, false)  = "abc"
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 *  
	 */
	public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
		if (isEmpty(string) || separator == null) {
			return null == string ? null : string.toString();
		}

		final String str = string.toString();
		final String sep = separator.toString();
		if (sep.isEmpty()) {
			return EMPTY;
		}
		final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
		if (INDEX_NOT_FOUND == pos) {
			return str;
		}
		if (0 == pos) {
			return EMPTY;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
	 * 如果分隔字符串未找到，返回原字符串，举例如下：
	 *
	 * <pre>
	 * StrUtil.subBefore(null, *, false)      = null
	 * StrUtil.subBefore("", *, false)        = ""
	 * StrUtil.subBefore("abc", 'a', false)   = ""
	 * StrUtil.subBefore("abcba", 'b', false) = "a"
	 * StrUtil.subBefore("abc", 'c', false)   = "ab"
	 * StrUtil.subBefore("abc", 'd', false)   = "abc"
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 *  
	 */
	public static String subBefore(CharSequence string, char separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}

		final String str = string.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
		if (INDEX_NOT_FOUND == pos) {
			return str;
		}
		if (0 == pos) {
			return EMPTY;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""），返回原字符串<br>
	 * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
	 *
	 * <pre>
	 * StrUtil.subAfter(null, *, false)      = null
	 * StrUtil.subAfter("", *, false)        = ""
	 * StrUtil.subAfter(*, null, false)      = ""
	 * StrUtil.subAfter("abc", "a", false)   = "bc"
	 * StrUtil.subAfter("abcba", "b", false) = "cba"
	 * StrUtil.subAfter("abc", "c", false)   = ""
	 * StrUtil.subAfter("abc", "d", false)   = ""
	 * StrUtil.subAfter("abc", "", false)    = "abc"
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 *  
	 */
	public static String subAfter(CharSequence string, CharSequence separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}
		if (separator == null) {
			return EMPTY;
		}
		final String str = string.toString();
		final String sep = separator.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
		if (INDEX_NOT_FOUND == pos || (string.length() - 1) == pos) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""），返回原字符串<br>
	 * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
	 *
	 * <pre>
	 * StrUtil.subAfter(null, *, false)      = null
	 * StrUtil.subAfter("", *, false)        = ""
	 * StrUtil.subAfter("abc", 'a', false)   = "bc"
	 * StrUtil.subAfter("abcba", 'b', false) = "cba"
	 * StrUtil.subAfter("abc", 'c', false)   = ""
	 * StrUtil.subAfter("abc", 'd', false)   = ""
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 *  
	 */
	public static String subAfter(CharSequence string, char separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}
		final String str = string.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
		if (INDEX_NOT_FOUND == pos) {
			return EMPTY;
		}
		return str.substring(pos + 1);
	}

	/**
	 * 截取指定字符串中间部分，不包括标识字符串<br>
	 * <p>
	 * 栗子：
	 *
	 * <pre>
	 * StrUtil.subBetween("wx[b]yz", "[", "]") = "b"
	 * StrUtil.subBetween(null, *, *)          = null
	 * StrUtil.subBetween(*, null, *)          = null
	 * StrUtil.subBetween(*, *, null)          = null
	 * StrUtil.subBetween("", "", "")          = ""
	 * StrUtil.subBetween("", "", "]")         = null
	 * StrUtil.subBetween("", "[", "]")        = null
	 * StrUtil.subBetween("yabcz", "", "")     = ""
	 * StrUtil.subBetween("yabcz", "y", "z")   = "abc"
	 * StrUtil.subBetween("yabczyabcz", "y", "z")   = "abc"
	 * </pre>
	 *
	 * @param str    被切割的字符串
	 * @param before 截取开始的字符串标识
	 * @param after  截取到的字符串标识
	 * @return 截取后的字符串
	 *  
	 */
	public static String subBetween(CharSequence str, CharSequence before, CharSequence after) {
		if (str == null || before == null || after == null) {
			return null;
		}

		final String str2 = str.toString();
		final String before2 = before.toString();
		final String after2 = after.toString();

		final int start = str2.indexOf(before2);
		if (start != INDEX_NOT_FOUND) {
			final int end = str2.indexOf(after2, start + before2.length());
			if (end != INDEX_NOT_FOUND) {
				return str2.substring(start + before2.length(), end);
			}
		}
		return null;
	}

	/**
	 * 截取指定字符串中间部分，不包括标识字符串<br>
	 * <p>
	 * 栗子：
	 *
	 * <pre>
	 * StrUtil.subBetween(null, *)            = null
	 * StrUtil.subBetween("", "")             = ""
	 * StrUtil.subBetween("", "tag")          = null
	 * StrUtil.subBetween("tagabctag", null)  = null
	 * StrUtil.subBetween("tagabctag", "")    = ""
	 * StrUtil.subBetween("tagabctag", "tag") = "abc"
	 * </pre>
	 *
	 * @param str            被切割的字符串
	 * @param beforeAndAfter 截取开始和结束的字符串标识
	 * @return 截取后的字符串
	 *  
	 */
	public static String subBetween(CharSequence str, CharSequence beforeAndAfter) {
		return subBetween(str, beforeAndAfter, beforeAndAfter);
	}

	/**
	 * 截取指定字符串多段中间部分，不包括标识字符串<br>
	 * <p>
	 * 栗子：
	 *
	 * <pre>
	 * StrUtil.subBetweenAll("wx[b]y[z]", "[", "]") 		= ["b","z"]
	 * StrUtil.subBetweenAll(null, *, *)          			= []
	 * StrUtil.subBetweenAll(*, null, *)          			= []
	 * StrUtil.subBetweenAll(*, *, null)          			= []
	 * StrUtil.subBetweenAll("", "", "")          			= []
	 * StrUtil.subBetweenAll("", "", "]")         			= []
	 * StrUtil.subBetweenAll("", "[", "]")        			= []
	 * StrUtil.subBetweenAll("yabcz", "", "")     			= []
	 * StrUtil.subBetweenAll("yabcz", "y", "z")   			= ["abc"]
	 * StrUtil.subBetweenAll("yabczyabcz", "y", "z")   		= ["abc","abc"]
	 * StrUtil.subBetweenAll("[yabc[zy]abcz]", "[", "]");   = ["zy"]           重叠时只截取内部，
	 * </pre>
	 *
	 * @param str    被切割的字符串
	 * @param prefix 截取开始的字符串标识
	 * @param suffix 截取到的字符串标识
	 * @return 截取后的字符串
	 */
	public static String[] subBetweenAll(CharSequence str, CharSequence prefix, CharSequence suffix) {
		if (hasEmpty(str, prefix, suffix) ||
				// 不包含起始字符串，则肯定没有子串
				false == contains(str, prefix)) {
			return new String[0];
		}

		final List<String> result = new LinkedList<>();
		final String[] split = splitToArray(str, prefix);
		if (prefix.equals(suffix)) {
			// 前后缀字符相同，单独处理
			for (int i = 1, length = split.length - 1; i < length; i += 2) {
				result.add(split[i]);
			}
		} else {
			int suffixIndex;
			String fragment;
			for (int i = 1; i < split.length; i++) {
				fragment = split[i];
				suffixIndex = fragment.indexOf(suffix.toString());
				if (suffixIndex > 0) {
					result.add(fragment.substring(0, suffixIndex));
				}
			}
		}

		return result.toArray(new String[0]);
	}

	/**
	 * 截取指定字符串多段中间部分，不包括标识字符串<br>
	 * <p>
	 * 栗子：
	 *
	 * <pre>
	 * StrUtil.subBetweenAll(null, *)          			= []
	 * StrUtil.subBetweenAll(*, null)          			= []
	 * StrUtil.subBetweenAll(*, *)          			= []
	 * StrUtil.subBetweenAll("", "")          			= []
	 * StrUtil.subBetweenAll("", "#")         			= []
	 * StrUtil.subBetweenAll("gotanks", "")     		= []
	 * StrUtil.subBetweenAll("#gotanks#", "#")   		= ["gotanks"]
	 * StrUtil.subBetweenAll("#hello# #world#!", "#")   = ["hello", "world"]
	 * StrUtil.subBetweenAll("#hello# world#!", "#");   = ["hello"]
	 * </pre>
	 *
	 * @param str             被切割的字符串
	 * @param prefixAndSuffix 截取开始和结束的字符串标识
	 * @return 截取后的字符串
	 */
	public static String[] subBetweenAll(CharSequence str, CharSequence prefixAndSuffix) {
		return subBetweenAll(str, prefixAndSuffix, prefixAndSuffix);
	}

	// ------------------------------------------------------------------------ repeat

	/**
	 * 重复某个字符
	 *
	 * <pre>
	 * StrUtil.repeat('e', 0)  = ""
	 * StrUtil.repeat('e', 3)  = "eee"
	 * StrUtil.repeat('e', -2) = ""
	 * </pre>
	 *
	 * @param c     被重复的字符
	 * @param count 重复的数目，如果小于等于0则返回""
	 * @return 重复字符字符串
	 */
	public static String repeat(char c, int count) {
		if (count <= 0) {
			return EMPTY;
		}

		char[] result = new char[count];
		for (int i = 0; i < count; i++) {
			result[i] = c;
		}
		return new String(result);
	}

	/**
	 * 重复某个字符串
	 *
	 * @param str   被重复的字符
	 * @param count 重复的数目
	 * @return 重复字符字符串
	 */
	public static String repeat(CharSequence str, int count) {
		if (null == str) {
			return null;
		}
		if (count <= 0 || str.length() == 0) {
			return EMPTY;
		}
		if (count == 1) {
			return str.toString();
		}

		// 检查
		final int len = str.length();
		final long longSize = (long) len * (long) count;
		final int size = (int) longSize;
		if (size != longSize) {
			throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
		}

		final char[] array = new char[size];
		str.toString().getChars(0, len, array, 0);
		int n;
		for (n = len; n < size - n; n <<= 1) {// n <<= 1相当于n *2
			System.arraycopy(array, 0, array, n, n);
		}
		System.arraycopy(array, 0, array, n, size - n);
		return new String(array);
	}

	/**
	 * 重复某个字符串到指定长度
	 *
	 * @param str    被重复的字符
	 * @param padLen 指定长度
	 * @return 重复字符字符串
	 *  
	 */
	public static String repeatByLength(CharSequence str, int padLen) {
		if (null == str) {
			return null;
		}
		if (padLen <= 0) {
			return StringUtil.EMPTY;
		}
		final int strLen = str.length();
		if (strLen == padLen) {
			return str.toString();
		} else if (strLen > padLen) {
			return subPre(str, padLen);
		}

		// 重复，直到达到指定长度
		final char[] padding = new char[padLen];
		for (int i = 0; i < padLen; i++) {
			padding[i] = str.charAt(i % strLen);
		}
		return new String(padding);
	}

	/**
	 * 重复某个字符串并通过分界符连接
	 *
	 * <pre>
	 * StrUtil.repeatAndJoin("?", 5, ",")   = "?,?,?,?,?"
	 * StrUtil.repeatAndJoin("?", 0, ",")   = ""
	 * StrUtil.repeatAndJoin("?", 5, null) = "?????"
	 * </pre>
	 *
	 * @param str       被重复的字符串
	 * @param count     数量
	 * @param delimiter 分界符
	 * @return 连接后的字符串
	 *  
	 */
	public static String repeatAndJoin(CharSequence str, int count, CharSequence delimiter) {
		if (count <= 0) {
			return EMPTY;
		}
		final StringBuilder builder = new StringBuilder(str.length() * count);
		builder.append(str);
		count--;

		final boolean isAppendDelimiter = isNotEmpty(delimiter);
		while (count-- > 0) {
			if (isAppendDelimiter) {
				builder.append(delimiter);
			}
			builder.append(str);
		}
		return builder.toString();
	}

	// ------------------------------------------------------------------------ equals

	/**
	 * 比较两个字符串（大小写敏感）。
	 *
	 * <pre>
	 * equals(null, null)   = true
	 * equals(null, &quot;abc&quot;)  = false
	 * equals(&quot;abc&quot;, null)  = false
	 * equals(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
	 * </pre>
	 *
	 * @param str1 要比较的字符串1
	 * @param str2 要比较的字符串2
	 * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
	 */
	public static boolean equals(CharSequence str1, CharSequence str2) {
		return equals(str1, str2, false);
	}

	/**
	 * 比较两个字符串（大小写不敏感）。
	 *
	 * <pre>
	 * equalsIgnoreCase(null, null)   = true
	 * equalsIgnoreCase(null, &quot;abc&quot;)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, null)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
	 * </pre>
	 *
	 * @param str1 要比较的字符串1
	 * @param str2 要比较的字符串2
	 * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
	 */
	public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
		return equals(str1, str2, true);
	}

	/**
	 * 比较两个字符串是否相等，规则如下
	 * <ul>
	 *     <li>str1和str2都为{@code null}</li>
	 *     <li>忽略大小写使用{@link String#equalsIgnoreCase(String)}判断相等</li>
	 *     <li>不忽略大小写使用{@link String#contentEquals(CharSequence)}判断相等</li>
	 * </ul>
	 *
	 * @param str1       要比较的字符串1
	 * @param str2       要比较的字符串2
	 * @param ignoreCase 是否忽略大小写
	 * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
	 *  
	 */
	public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
		if (null == str1) {
			// 只有两个都为null才判断相等
			return str2 == null;
		}
		if (null == str2) {
			// 字符串2空，字符串1非空，直接false
			return false;
		}

		if (ignoreCase) {
			return str1.toString().equalsIgnoreCase(str2.toString());
		} else {
			return str1.toString().contentEquals(str2);
		}
	}

	/**
	 * 给定字符串是否与提供的中任一字符串相同（忽略大小写），相同则返回{@code true}，没有相同的返回{@code false}<br>
	 * 如果参与比对的字符串列表为空，返回{@code false}
	 *
	 * @param str1 给定需要检查的字符串
	 * @param strs 需要参与比对的字符串列表
	 * @return 是否相同
	 *  
	 */
	public static boolean equalsAnyIgnoreCase(CharSequence str1, CharSequence... strs) {
		return equalsAny(str1, true, strs);
	}

	/**
	 * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
	 * 如果参与比对的字符串列表为空，返回{@code false}
	 *
	 * @param str1 给定需要检查的字符串
	 * @param strs 需要参与比对的字符串列表
	 * @return 是否相同
	 *  
	 */
	public static boolean equalsAny(CharSequence str1, CharSequence... strs) {
		return equalsAny(str1, false, strs);
	}

	/**
	 * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
	 * 如果参与比对的字符串列表为空，返回{@code false}
	 *
	 * @param str1       给定需要检查的字符串
	 * @param ignoreCase 是否忽略大小写
	 * @param strs       需要参与比对的字符串列表
	 * @return 是否相同
	 *  
	 */
	public static boolean equalsAny(CharSequence str1, boolean ignoreCase, CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return false;
		}

		for (CharSequence str : strs) {
			if (equals(str1, str, ignoreCase)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 字符串指定位置的字符是否与给定字符相同<br>
	 * 如果字符串为null，返回false<br>
	 * 如果给定的位置大于字符串长度，返回false<br>
	 * 如果给定的位置小于0，返回false
	 *
	 * @param str      字符串
	 * @param position 位置
	 * @param c        需要对比的字符
	 * @return 字符串指定位置的字符是否与给定字符相同
	 *  
	 */
	public static boolean equalsCharAt(CharSequence str, int position, char c) {
		if (null == str || position < 0) {
			return false;
		}
		return str.length() > position && c == str.charAt(position);
	}

	/**
	 * 截取两个字符串的不同部分（长度一致），判断截取的子串是否相同<br>
	 * 任意一个字符串为null返回false
	 *
	 * @param str1       第一个字符串
	 * @param start1     第一个字符串开始的位置
	 * @param str2       第二个字符串
	 * @param start2     第二个字符串开始的位置
	 * @param length     截取长度
	 * @param ignoreCase 是否忽略大小写
	 * @return 子串是否相同
	 *  
	 */
	public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, int start2, int length, boolean ignoreCase) {
		if (null == str1 || null == str2) {
			return false;
		}

		return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
	}

	// ------------------------------------------------------------------------ format

	/**
	 * 格式化文本, {} 表示占位符<br>
	 * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
	 * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
	 * 例：<br>
	 * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
	 * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
	 * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
	 *
	 * @param template 文本模板，被替换的部分用 {} 表示，如果模板为null，返回"null"
	 * @param params   参数值
	 * @return 格式化后的文本，如果模板为null，返回"null"
	 */
	public static String format(CharSequence template, Object... params) {
		if (null == template) {
			return NULL;
		}
		if (ArrayUtil.isEmpty(params) || isBlank(template)) {
			return template.toString();
		}
		return StringFormatterUtil.format(template.toString(), params);
	}

	/**
	 * 有序的格式化文本，使用{number}做为占位符<br>
	 * 通常使用：format("this is {0} for {1}", "a", "b") =》 this is a for b<br>
	 *
	 * @param pattern   文本格式
	 * @param arguments 参数
	 * @return 格式化后的文本
	 */
	public static String indexedFormat(CharSequence pattern, Object... arguments) {
		return MessageFormat.format(pattern.toString(), arguments);
	}
	// ------------------------------------------------------------------------ bytes

	/**
	 * 编码字符串，编码为UTF-8
	 *
	 * @param str 字符串
	 * @return 编码后的字节码
	 */
	public static byte[] utf8Bytes(CharSequence str) {
		return bytes(str, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码字符串<br>
	 * 使用系统默认编码
	 *
	 * @param str 字符串
	 * @return 编码后的字节码
	 */
	public static byte[] bytes(CharSequence str) {
		return bytes(str, Charset.defaultCharset());
	}

	/**
	 * 编码字符串
	 *
	 * @param str     字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 编码后的字节码
	 */
	public static byte[] bytes(CharSequence str, String charset) {
		return bytes(str, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}

	/**
	 * 编码字符串
	 *
	 * @param str     字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 编码后的字节码
	 */
	public static byte[] bytes(CharSequence str, Charset charset) {
		if (str == null) {
			return null;
		}

		if (null == charset) {
			return str.toString().getBytes();
		}
		return str.toString().getBytes(charset);
	}

	/**
	 * 字符串转换为byteBuffer
	 *
	 * @param str     字符串
	 * @param charset 编码
	 * @return byteBuffer
	 */
	public static ByteBuffer byteBuffer(CharSequence str, String charset) {
		return ByteBuffer.wrap(bytes(str, charset));
	}

	// ------------------------------------------------------------------------ wrap

	/**
	 * 包装指定字符串<br>
	 * 当前缀和后缀一致时使用此方法
	 *
	 * @param str             被包装的字符串
	 * @param prefixAndSuffix 前缀和后缀
	 * @return 包装后的字符串
	 *  
	 */
	public static String wrap(CharSequence str, CharSequence prefixAndSuffix) {
		return wrap(str, prefixAndSuffix, prefixAndSuffix);
	}

	/**
	 * 包装指定字符串
	 *
	 * @param str    被包装的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 包装后的字符串
	 */
	public static String wrap(CharSequence str, CharSequence prefix, CharSequence suffix) {
		return nullToEmpty(prefix).concat(nullToEmpty(str)).concat(nullToEmpty(suffix));
	}

	/**
	 * 使用单个字符包装多个字符串
	 *
	 * @param prefixAndSuffix 前缀和后缀
	 * @param strs            多个字符串
	 * @return 包装的字符串数组
	 *  
	 */
	public static String[] wrapAllWithPair(CharSequence prefixAndSuffix, CharSequence... strs) {
		return wrapAll(prefixAndSuffix, prefixAndSuffix, strs);
	}

	/**
	 * 包装多个字符串
	 *
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param strs   多个字符串
	 * @return 包装的字符串数组
	 *  
	 */
	public static String[] wrapAll(CharSequence prefix, CharSequence suffix, CharSequence... strs) {
		final String[] results = new String[strs.length];
		for (int i = 0; i < strs.length; i++) {
			results[i] = wrap(strs[i], prefix, suffix);
		}
		return results;
	}

	/**
	 * 包装指定字符串，如果前缀或后缀已经包含对应的字符串，则不再包装
	 *
	 * @param str    被包装的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 包装后的字符串
	 */
	public static String wrapIfMissing(CharSequence str, CharSequence prefix, CharSequence suffix) {
		int len = 0;
		if (isNotEmpty(str)) {
			len += str.length();
		}
		if (isNotEmpty(prefix)) {
			len += prefix.length();
		}
		if (isNotEmpty(suffix)) {
			len += suffix.length();
		}
		StringBuilder sb = new StringBuilder(len);
		if (isNotEmpty(prefix) && false == startWith(str, prefix)) {
			sb.append(prefix);
		}
		if (isNotEmpty(str)) {
			sb.append(str);
		}
		if (isNotEmpty(suffix) && false == endWith(str, suffix)) {
			sb.append(suffix);
		}
		return sb.toString();
	}

	/**
	 * 使用成对的字符包装多个字符串，如果已经包装，则不再包装
	 *
	 * @param prefixAndSuffix 前缀和后缀
	 * @param strs            多个字符串
	 * @return 包装的字符串数组
	 *  
	 */
	public static String[] wrapAllWithPairIfMissing(CharSequence prefixAndSuffix, CharSequence... strs) {
		return wrapAllIfMissing(prefixAndSuffix, prefixAndSuffix, strs);
	}

	/**
	 * 包装多个字符串，如果已经包装，则不再包装
	 *
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param strs   多个字符串
	 * @return 包装的字符串数组
	 *  
	 */
	public static String[] wrapAllIfMissing(CharSequence prefix, CharSequence suffix, CharSequence... strs) {
		final String[] results = new String[strs.length];
		for (int i = 0; i < strs.length; i++) {
			results[i] = wrapIfMissing(strs[i], prefix, suffix);
		}
		return results;
	}

	/**
	 * 去掉字符包装，如果未被包装则返回原字符串
	 *
	 * @param str    字符串
	 * @param prefix 前置字符串
	 * @param suffix 后置字符串
	 * @return 去掉包装字符的字符串
	 *  
	 */
	public static String unWrap(CharSequence str, String prefix, String suffix) {
		if (isWrap(str, prefix, suffix)) {
			return sub(str, prefix.length(), str.length() - suffix.length());
		}
		return str.toString();
	}

	/**
	 * 去掉字符包装，如果未被包装则返回原字符串
	 *
	 * @param str    字符串
	 * @param prefix 前置字符
	 * @param suffix 后置字符
	 * @return 去掉包装字符的字符串
	 *  
	 */
	public static String unWrap(CharSequence str, char prefix, char suffix) {
		if (isEmpty(str)) {
			return str(str);
		}
		if (str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix) {
			return sub(str, 1, str.length() - 1);
		}
		return str.toString();
	}

	/**
	 * 去掉字符包装，如果未被包装则返回原字符串
	 *
	 * @param str             字符串
	 * @param prefixAndSuffix 前置和后置字符
	 * @return 去掉包装字符的字符串
	 *  
	 */
	public static String unWrap(CharSequence str, char prefixAndSuffix) {
		return unWrap(str, prefixAndSuffix, prefixAndSuffix);
	}

	/**
	 * 指定字符串是否被包装
	 *
	 * @param str    字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 是否被包装
	 */
	public static boolean isWrap(CharSequence str, String prefix, String suffix) {
		if (ArrayUtil.hasNull(str, prefix, suffix)) {
			return false;
		}
		final String str2 = str.toString();
		return str2.startsWith(prefix) && str2.endsWith(suffix);
	}

	/**
	 * 指定字符串是否被同一字符包装（前后都有这些字符串）
	 *
	 * @param str     字符串
	 * @param wrapper 包装字符串
	 * @return 是否被包装
	 */
	public static boolean isWrap(CharSequence str, String wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 指定字符串是否被同一字符包装（前后都有这些字符串）
	 *
	 * @param str     字符串
	 * @param wrapper 包装字符
	 * @return 是否被包装
	 */
	public static boolean isWrap(CharSequence str, char wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 指定字符串是否被包装
	 *
	 * @param str        字符串
	 * @param prefixChar 前缀
	 * @param suffixChar 后缀
	 * @return 是否被包装
	 */
	public static boolean isWrap(CharSequence str, char prefixChar, char suffixChar) {
		if (null == str) {
			return false;
		}

		return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
	}

	// ------------------------------------------------------------------------ pad

	/**
	 * 补充字符串以满足指定长度，如果提供的字符串大于指定长度，截断之
	 * 同：leftPad (org.apache.commons.lang3.leftPad)
	 *
	 * <pre>
	 * StrUtil.padPre(null, *, *);//null
	 * StrUtil.padPre("1", 3, "ABC");//"AB1"
	 * StrUtil.padPre("123", 2, "ABC");//"12"
	 * StrUtil.padPre("1039", -1, "0");//"103"
	 * </pre>
	 *
	 * @param str    字符串
	 * @param length 长度
	 * @param padStr 补充的字符
	 * @return 补充后的字符串
	 */
	public static String padPre(CharSequence str, int length, CharSequence padStr) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//如果提供的字符串大于指定长度，截断之
			return subPre(str, length);
		}

		return repeatByLength(padStr, length - strLen).concat(str.toString());
	}

	/**
	 * 补充字符串以满足最小长度，如果提供的字符串大于指定长度，截断之
	 * 同：leftPad (org.apache.commons.lang3.leftPad)
	 *
	 * <pre>
	 * StrUtil.padPre(null, *, *);//null
	 * StrUtil.padPre("1", 3, '0');//"001"
	 * StrUtil.padPre("123", 2, '0');//"12"
	 * </pre>
	 *
	 * @param str     字符串
	 * @param length  长度
	 * @param padChar 补充的字符
	 * @return 补充后的字符串
	 */
	public static String padPre(CharSequence str, int length, char padChar) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//如果提供的字符串大于指定长度，截断之
			return subPre(str, length);
		}

		return repeat(padChar, length - strLen).concat(str.toString());
	}

	/**
	 * 补充字符串以满足最小长度，如果提供的字符串大于指定长度，截断之
	 *
	 * <pre>
	 * StrUtil.padAfter(null, *, *);//null
	 * StrUtil.padAfter("1", 3, '0');//"100"
	 * StrUtil.padAfter("123", 2, '0');//"23"
	 * StrUtil.padAfter("123", -1, '0')//"" 空串
	 * </pre>
	 *
	 * @param str     字符串，如果为{@code null}，直接返回null
	 * @param length  长度
	 * @param padChar 补充的字符
	 * @return 补充后的字符串
	 */
	public static String padAfter(CharSequence str, int length, char padChar) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//如果提供的字符串大于指定长度，截断之
			return sub(str, strLen - length, strLen);
		}

		return str.toString().concat(repeat(padChar, length - strLen));
	}

	/**
	 * 补充字符串以满足最小长度
	 *
	 * <pre>
	 * StrUtil.padAfter(null, *, *);//null
	 * StrUtil.padAfter("1", 3, "ABC");//"1AB"
	 * StrUtil.padAfter("123", 2, "ABC");//"23"
	 * </pre>
	 *
	 * @param str    字符串，如果为{@code null}，直接返回null
	 * @param length 长度
	 * @param padStr 补充的字符
	 * @return 补充后的字符串
	 *  
	 */
	public static String padAfter(CharSequence str, int length, CharSequence padStr) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//如果提供的字符串大于指定长度，截断之
			return subSufByLength(str, length);
		}

		return str.toString().concat(repeatByLength(padStr, length - strLen));
	}

	// ------------------------------------------------------------------------ center

	/**
	 * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
	 *
	 * <pre>
	 * StrUtil.center(null, *)   = null
	 * StrUtil.center("", 4)     = "    "
	 * StrUtil.center("ab", -1)  = "ab"
	 * StrUtil.center("ab", 4)   = " ab "
	 * StrUtil.center("abcd", 2) = "abcd"
	 * StrUtil.center("a", 4)    = " a  "
	 * </pre>
	 *
	 * @param str  字符串
	 * @param size 指定长度
	 * @return 补充后的字符串
	 *  
	 */
	public static String center(CharSequence str, final int size) {
		return center(str, size, CharUtil.SPACE);
	}

	/**
	 * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
	 *
	 * <pre>
	 * StrUtil.center(null, *, *)     = null
	 * StrUtil.center("", 4, ' ')     = "    "
	 * StrUtil.center("ab", -1, ' ')  = "ab"
	 * StrUtil.center("ab", 4, ' ')   = " ab "
	 * StrUtil.center("abcd", 2, ' ') = "abcd"
	 * StrUtil.center("a", 4, ' ')    = " a  "
	 * StrUtil.center("a", 4, 'y')   = "yayy"
	 * StrUtil.center("abc", 7, ' ')   = "  abc  "
	 * </pre>
	 *
	 * @param str     字符串
	 * @param size    指定长度
	 * @param padChar 两边补充的字符
	 * @return 补充后的字符串
	 *  
	 */
	public static String center(CharSequence str, final int size, char padChar) {
		if (str == null || size <= 0) {
			return str(str);
		}
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str.toString();
		}
		str = padPre(str, strLen + pads / 2, padChar);
		str = padAfter(str, size, padChar);
		return str.toString();
	}

	/**
	 * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
	 *
	 * <pre>
	 * StrUtil.center(null, *, *)     = null
	 * StrUtil.center("", 4, " ")     = "    "
	 * StrUtil.center("ab", -1, " ")  = "ab"
	 * StrUtil.center("ab", 4, " ")   = " ab "
	 * StrUtil.center("abcd", 2, " ") = "abcd"
	 * StrUtil.center("a", 4, " ")    = " a  "
	 * StrUtil.center("a", 4, "yz")   = "yayz"
	 * StrUtil.center("abc", 7, null) = "  abc  "
	 * StrUtil.center("abc", 7, "")   = "  abc  "
	 * </pre>
	 *
	 * @param str    字符串
	 * @param size   指定长度
	 * @param padStr 两边补充的字符串
	 * @return 补充后的字符串
	 */
	public static String center(CharSequence str, final int size, CharSequence padStr) {
		if (str == null || size <= 0) {
			return str(str);
		}
		if (isEmpty(padStr)) {
			padStr = SPACE;
		}
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str.toString();
		}
		str = padPre(str, strLen + pads / 2, padStr);
		str = padAfter(str, size, padStr);
		return str.toString();
	}

	// ------------------------------------------------------------------------ str

	/**
	 * {@link CharSequence} 转为字符串，null安全
	 *
	 * @param cs {@link CharSequence}
	 * @return 字符串
	 */
	public static String str(CharSequence cs) {
		return null == cs ? null : cs.toString();
	}

	// ------------------------------------------------------------------------ count

	/**
	 * 统计指定内容中包含指定字符串的数量<br>
	 * 参数为 {@code null} 或者 "" 返回 {@code 0}.
	 *
	 * <pre>
	 * StrUtil.count(null, *)       = 0
	 * StrUtil.count("", *)         = 0
	 * StrUtil.count("abba", null)  = 0
	 * StrUtil.count("abba", "")    = 0
	 * StrUtil.count("abba", "a")   = 2
	 * StrUtil.count("abba", "ab")  = 1
	 * StrUtil.count("abba", "xxx") = 0
	 * </pre>
	 *
	 * @param content      被查找的字符串
	 * @param strForSearch 需要查找的字符串
	 * @return 查找到的个数
	 */
	public static int count(CharSequence content, CharSequence strForSearch) {
		if (hasEmpty(content, strForSearch) || strForSearch.length() > content.length()) {
			return 0;
		}

		int count = 0;
		int idx = 0;
		final String content2 = content.toString();
		final String strForSearch2 = strForSearch.toString();
		while ((idx = content2.indexOf(strForSearch2, idx)) > -1) {
			count++;
			idx += strForSearch.length();
		}
		return count;
	}

	/**
	 * 统计指定内容中包含指定字符的数量
	 *
	 * @param content       内容
	 * @param charForSearch 被统计的字符
	 * @return 包含数量
	 */
	public static int count(CharSequence content, char charForSearch) {
		int count = 0;
		if (isEmpty(content)) {
			return 0;
		}
		int contentLength = content.length();
		for (int i = 0; i < contentLength; i++) {
			if (charForSearch == content.charAt(i)) {
				count++;
			}
		}
		return count;
	}

	// ------------------------------------------------------------------------ compare

	/**
	 * 比较两个字符串，用于排序
	 *
	 * <pre>
	 * StrUtil.compare(null, null, *)     = 0
	 * StrUtil.compare(null , "a", true)  &lt; 0
	 * StrUtil.compare(null , "a", false) &gt; 0
	 * StrUtil.compare("a", null, true)   &gt; 0
	 * StrUtil.compare("a", null, false)  &lt; 0
	 * StrUtil.compare("abc", "abc", *)   = 0
	 * StrUtil.compare("a", "b", *)       &lt; 0
	 * StrUtil.compare("b", "a", *)       &gt; 0
	 * StrUtil.compare("a", "B", *)       &gt; 0
	 * StrUtil.compare("ab", "abc", *)    &lt; 0
	 * </pre>
	 *
	 * @param str1       字符串1
	 * @param str2       字符串2
	 * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
	 * @return 排序值。负数：str1 &lt; str2，正数：str1 &gt; str2, 0：str1 == str2
	 */
	public static int compare(final CharSequence str1, final CharSequence str2, final boolean nullIsLess) {
		if (str1 == str2) {
			return 0;
		}
		if (str1 == null) {
			return nullIsLess ? -1 : 1;
		}
		if (str2 == null) {
			return nullIsLess ? 1 : -1;
		}
		return str1.toString().compareTo(str2.toString());
	}

	/**
	 * 比较两个字符串，用于排序，大小写不敏感
	 *
	 * <pre>
	 * StrUtil.compareIgnoreCase(null, null, *)     = 0
	 * StrUtil.compareIgnoreCase(null , "a", true)  &lt; 0
	 * StrUtil.compareIgnoreCase(null , "a", false) &gt; 0
	 * StrUtil.compareIgnoreCase("a", null, true)   &gt; 0
	 * StrUtil.compareIgnoreCase("a", null, false)  &lt; 0
	 * StrUtil.compareIgnoreCase("abc", "abc", *)   = 0
	 * StrUtil.compareIgnoreCase("abc", "ABC", *)   = 0
	 * StrUtil.compareIgnoreCase("a", "b", *)       &lt; 0
	 * StrUtil.compareIgnoreCase("b", "a", *)       &gt; 0
	 * StrUtil.compareIgnoreCase("a", "B", *)       &lt; 0
	 * StrUtil.compareIgnoreCase("A", "b", *)       &lt; 0
	 * StrUtil.compareIgnoreCase("ab", "abc", *)    &lt; 0
	 * </pre>
	 *
	 * @param str1       字符串1
	 * @param str2       字符串2
	 * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
	 * @return 排序值。负数：str1 &lt; str2，正数：str1 &gt; str2, 0：str1 == str2
	 */
	public static int compareIgnoreCase(CharSequence str1, CharSequence str2, boolean nullIsLess) {
		if (str1 == str2) {
			return 0;
		}
		if (str1 == null) {
			return nullIsLess ? -1 : 1;
		}
		if (str2 == null) {
			return nullIsLess ? 1 : -1;
		}
		return str1.toString().compareToIgnoreCase(str2.toString());
	}

}
