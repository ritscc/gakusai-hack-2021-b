package dev.abelab.gifttree.helper.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.jeasy.random.EasyRandom;

public class RandomUtil {

	private static final EasyRandom easyRandom = new EasyRandom();

	/**
	 * ランダムなメールアドレスを生成
	 *
	 * @return email
	 */
	public static String generateEmail() {
		return RandomStringUtils.randomAlphanumeric(10) + "@example.com";
	}

	/**
	 * ランダムなアルファベット+数字の文字列を生成
	 *
	 * @param length length
	 *
	 * @return random string
	 */
	public static String generateAlphanumeric(final int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}

	/**
	 * ランダムな値を格納したオブジェクトを生成
	 *
	 * @param <T>   type
	 * @param clazz clazz
	 *
	 * @return object
	 */
	public static <T> T generateRandomObject(final Class<T> clazz) {
		return easyRandom.nextObject(clazz);
	}

}
