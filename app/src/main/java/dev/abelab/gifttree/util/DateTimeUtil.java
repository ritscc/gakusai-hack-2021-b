package dev.abelab.gifttree.util;

import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DateTimeUtil {

    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.JAPAN);

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM月dd日(E)", Locale.JAPAN);

    /**
     * 今日の日時を取得
     *
     * return 今日の日時
     */
    public static Date getToday() {
        final var calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 翌日の日時を取得
     *
     * return 翌日の日時
     */
    public static Date getTomorrow() {
        final var calendar = Calendar.getInstance();
        calendar.setTime(getToday());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 翌週の日時を取得
     *
     * return 翌週の日時
     */
    public static Date getNextWeek() {
        final var calendar = Calendar.getInstance();
        calendar.setTime(getToday());
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * 昨日の日時を取得
     *
     * return 翌日の日時
     */
    public static Date getYesterday() {
        final var calendar = Calendar.getInstance();
        calendar.setTime(getToday());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 指定した日時を取得
     *
     * @param year   年
     *
     * @param month  月
     *
     * @param day    日
     *
     * @param hour   時
     *
     * @param minute 分
     *
     * @return 日時
     */
    public static Date getDateTime(final int year, final int month, final int day, final int hour, final int minute) {
        final var calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTime();
    }

    /**
     * 時間を文字列に変換
     *
     * @param date 時間
     *
     * @return 時間（文字列）
     */
    public static String convertTimeToString(final Date date) {
        return timeFormatter.format(date);
    }

    /**
     * 日時を文字列に変換
     *
     * @param date 日時
     *
     * @return 日時（文字列）
     */
    public static String convertDateToString(final Date date) {
        return dateFormatter.format(date);
    }

    /**
     * 日時を加算する
     *
     * @param date   日時
     *
     * @param field  calendar field
     *
     * @param amount 加算量
     *
     * @return 加算後の日時
     */
    public static Date addDateTime(final Date date, final int field, final int amount) {
        final var calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 日時の属性を編集する
     *
     * @param date  日時
     *
     * @param field calendar field
     *
     * @param value 変更後の値
     *
     * @return 編集後の日時
     */
    public static Date editDateTime(final Date date, final int field, final int value) {
        final var calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(field, value);
        return calendar.getTime();
    }

    /**
     * 日時から時間を取得する
     *
     * @param date 日時
     *
     * @return 時間
     */
    public static int getHour(final Date date) {
        final var calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 時間差分を計算
     *
     * @param date1 日時1
     *
     * @param date2 日時2
     *
     * @return 時間差分
     */
    public static int diffHours(final Date date1, final Date date2) {
        return (int) Math.abs((date1.getTime() - date2.getTime()) / (1000.0 * 60.0 * 60.0));
    }

}
