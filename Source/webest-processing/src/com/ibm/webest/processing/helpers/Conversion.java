package com.ibm.webest.processing.helpers;

import java.util.Calendar;

/**
 * Conversion Helpercals for conversiontasks.
 * 
 * A report can use this class to create date with the duration from a phase.
 * 
 * @author Andre Munzinger, Wail Shakir
 */
public class Conversion {

	/**
	 * convertToMonth Converts months into years
	 * 
	 * @param years
	 * @return months
	 */
	public static Float convertToMonth(Float years) {
		return years * 12;
	}

	/**
	 * convertToYear Converts months into years
	 * 
	 * @param months
	 * @return years
	 */
	public static Float convertToYear(Float months) {
		return months / 12;
	}

	/**
	 * add the duration for the Phase to the calendar
	 * 
	 * @param calendar
	 *            the calendar that must changed.
	 * @param duration
	 *            the duration for the Phase
	 * @param weeksPerMonth
	 *            the weeks per month
	 * @param daysPerWeek
	 *            the work days per week
	 * @param hoursPerDay
	 *            the work hours per day
	 * @throws ConversionException
	 *             The calendar can't be empty. The work days per week must
	 *             between 1 and 7. The days must be bigger then -1 and smaller
	 *             (work days per week + 1).
	 */
	public static void date(Calendar calendar, float duration,
			double weeksPerMonth, int daysPerWeek, int hoursPerDay)
			throws ConversionException {
		if (duration >= 0) {
			double projectWeeks = duration * weeksPerMonth;
			calendar.add(Calendar.WEEK_OF_YEAR, (int) projectWeeks);
			double lastDays = ((((int) projectWeeks) != 0) ? (projectWeeks % ((int) projectWeeks))
					: projectWeeks)
					* daysPerWeek;

			date(calendar, (int) lastDays, daysPerWeek);

			double hours = (((int) lastDays != 0) ? (lastDays % ((int) lastDays))
					: lastDays)
					* hoursPerDay;

			if (hours > 0)
				date(calendar, 1, daysPerWeek);

			int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			if (day == 0 && daysPerWeek != 7)
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			else if (daysPerWeek - day < 0)
				calendar.add(Calendar.DAY_OF_MONTH,
						(getRestDaysOfWeek(calendar.get(day)) + 1));
		} else
			throw new ConversionException("The duration is smaller than zero!");

	}

	/**
	 * add days to Date
	 * 
	 * @param calendar
	 *            the calendar that must changed.
	 * @param days
	 *            the days that added to calendar
	 * @param daysPerWeek
	 *            the work days per week
	 * @throws ConversionException
	 *             The calendar can't be empty. The work days per week must
	 *             between 1 and 7. The days must be bigger then -1 and smaller
	 *             (work days per week + 1).
	 */
	public static void date(Calendar calendar, int days, int daysPerWeek)
			throws ConversionException {
		if (calendar == null)
			throw new ConversionException("The calendar is empty!");
		else if (daysPerWeek < 1 || daysPerWeek > 7)
			throw new ConversionException(
					"The work days per week must between 1 and 7 but it was "
							+ daysPerWeek);
		else if (days > daysPerWeek || days < 0)
			throw new ConversionException(
					"The days must be bigger then -1 and smaller (work days per week + 1) but it was "
							+ days);

		checkDay(calendar, days, daysPerWeek);

	}

	/**
	 * check the calendar with the days per week. Monday is the week start day.
	 * 
	 * @param calendar
	 *            the calendar that must changed.
	 * @param days
	 *            the days that added to calendar
	 * @param daysPerWeek
	 *            the work days per week
	 * @throws ConversionException
	 */
	private static void checkDay(Calendar calendar, int days, int daysPerWeek) {

		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		switch (daysPerWeek) {

		case 1:
			if (day == 1)
				calendar.add(Calendar.DAY_OF_WEEK, 7);
			else
				calendar.add(Calendar.DAY_OF_WEEK, (getRestDaysOfWeek(day) + 1));

			break;
		case 7:
			calendar.add(Calendar.DAY_OF_MONTH, days);
			break;
		default:
			checkDayHelper(calendar, days, daysPerWeek, day);
			break;
		}

	}

	private static void checkDayHelper(Calendar calendar, int days,
			int daysPerWeek, int day) {

		int x;
		if (day == 0)
			calendar.add(Calendar.DAY_OF_MONTH, days);
		else if (day >= daysPerWeek) {
			calendar.add(Calendar.DAY_OF_MONTH, getRestDaysOfWeek(day));
			calendar.add(Calendar.DAY_OF_MONTH, days);
		} else {
			x = daysPerWeek - day;
			int y = days - x;

			if (y > 0) {
				calendar.add(Calendar.DAY_OF_MONTH, getRestDaysOfWeek(day));
				calendar.add(Calendar.DAY_OF_MONTH, y);
			} else {
				calendar.add(Calendar.DAY_OF_MONTH, days);
			}
		}
	}

	private static int getRestDaysOfWeek(int day) {
		switch (day) {
		case 0:
			return 0;
		case 1:
			return 6;
		case 2:
			return 5;
		case 3:
			return 4;
		case 4:
			return 3;
		case 5:
			return 2;
		case 6:
			return 1;

		default:
			return 0;
		}
	}

}
