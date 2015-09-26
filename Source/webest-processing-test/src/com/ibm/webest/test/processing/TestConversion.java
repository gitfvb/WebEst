package com.ibm.webest.test.processing;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ibm.webest.processing.helpers.Conversion;
import com.ibm.webest.processing.helpers.ConversionException;

/**
 * This class tested the correctly data for a duration
 * 
 * @author Wail Shakir
 */
public class TestConversion {

	int[][] daysPerWeek = { { 1 }, { 1, 2 }, { 1, 2, 3 }, { 1, 2, 3, 4 },
			{ 1, 2, 3, 4, 5 }, { 1, 2, 3, 4, 5, 6 }, { 1, 2, 3, 4, 5, 6, 7 } };
	float[] duration = { 12.8F, 10.5F, 9.2F };
	int[] daysPerWeek2 = { 2, 3, 4, 5, 6, 7 };
	int hoursPerDay = 8;
	int[][] date2Result = { { 2033, 2034, 2022 }, { 2034, 2035, 2023 },
			{ 2034, 2035, 2024 }, { 2035, 2036, 2025 }, { 2035, 2036, 2026 },
			{ 2034, 2036, 2025 } };
	Vector<int[][]> dateResult = new Vector<int[][]>();
	Calendar calendar;

	@Before
	public void test() {
		calendar = GregorianCalendar.getInstance();

		int[][] result1 = { { 31, 31, 7, 7, 7, 7, 7 } };
		int[][] result2 = { { 31, 31, 1, 7, 7, 7, 7 }, { 1, 1, 8, 8, 8, 8, 8 } };
		int[][] result3 = { { 31, 31, 1, 2, 7, 7, 7 }, { 1, 1, 2, 7, 8, 8, 8 },
				{ 2, 2, 7, 8, 9, 14, 15 } };
		int[][] result4 = { { 31, 31, 1, 2, 3, 7, 7 }, { 1, 1, 2, 3, 7, 8, 8 },
				{ 2, 2, 3, 7, 8, 9, 9 }, { 3, 3, 7, 8, 9, 10, 14 } };
		int[][] result5 = { { 31, 31, 1, 2, 3, 4, 7 }, { 1, 1, 2, 3, 4, 7, 8 },
				{ 2, 2, 3, 4, 7, 8, 9 }, { 3, 3, 4, 7, 8, 9, 10 },
				{ 4, 4, 7, 8, 9, 10, 11 } };
		int[][] result6 = { { 31, 31, 1, 2, 3, 4, 5 }, { 1, 1, 2, 3, 4, 5, 7 },
				{ 2, 2, 3, 4, 5, 7, 8 }, { 3, 3, 4, 5, 7, 8, 9 },
				{ 4, 4, 5, 7, 8, 9, 10 }, { 5, 5, 7, 8, 9, 10, 11 } };
		int[][] result7 = { { 30, 31, 1, 2, 3, 4, 5 },
				{ 31, 1, 2, 3, 4, 5, 6 }, { 1, 2, 3, 4, 5, 6, 7 },
				{ 2, 3, 4, 5, 6, 7, 8 }, { 3, 4, 5, 6, 7, 8, 9 },
				{ 4, 5, 6, 7, 8, 9, 10 }, { 5, 6, 7, 8, 9, 10, 11 } };

		dateResult.add(result1);
		dateResult.add(result2);
		dateResult.add(result3);
		dateResult.add(result4);
		dateResult.add(result5);
		dateResult.add(result6);
		dateResult.add(result7);
	}

	@Test
	public void conversionDateTest1() throws ConversionException {

		for (int i = 0; i < daysPerWeek.length; i++) {
			for (int j = 0; j < daysPerWeek[i].length - 1; j++) {
				for (int j2 = 0; j2 < 7; j2++) {

					calendar.set(2011, 0, 29 + (j2));
					Conversion.date(calendar, daysPerWeek[i][j],
							daysPerWeek[i].length);

					if (dateResult.get(i)[j][j2] < 30)
						assertTrue(calendar.get(Calendar.MONTH) + "",
								calendar.get(Calendar.MONTH) == 1);
					else
						assertTrue(calendar.get(Calendar.MONTH) == 0);
					assertTrue(calendar.get(Calendar.DAY_OF_MONTH) == dateResult
							.get(i)[j][j2]);
				}

			}
		}
	}

	@Test
	public void conversionDateTest2() throws ConversionException {
		for (int i = 0; i < daysPerWeek2.length; i++) {
			for (int j = 0; j < duration.length; j++) {
				calendar.set(2011, 0, 29);
				Conversion.date(calendar, duration[j], 4.33, daysPerWeek2[i],
						hoursPerDay);
				int value = calendar.get(Calendar.DAY_OF_MONTH)
						+ calendar.get(Calendar.MONTH)
						+ calendar.get(Calendar.YEAR);
				assertTrue(value == date2Result[i][j]);
			}
		}
	}

	@Test(expected = ConversionException.class)
	public void conversionExceptionTest1() throws ConversionException {

		Conversion.date(null, 2, 3);
	}

	@Test(expected = ConversionException.class)
	public void conversionExceptionTest2() throws ConversionException {
		Calendar calendar = GregorianCalendar.getInstance();
		Conversion.date(calendar, 0, 0);
	}

	@Test(expected = ConversionException.class)
	public void conversionExceptionTest3() throws ConversionException {
		Calendar calendar = GregorianCalendar.getInstance();
		Conversion.date(calendar, 4, 8);
	}

	@Test(expected = ConversionException.class)
	public void conversionExceptionTest4() throws ConversionException {
		Calendar calendar = GregorianCalendar.getInstance();
		Conversion.date(calendar, -1, 3);
	}

	@Test(expected = ConversionException.class)
	public void conversionExceptionTest5() throws ConversionException {
		Calendar calendar = GregorianCalendar.getInstance();
		Conversion.date(calendar, 4, 3);
	}
}
