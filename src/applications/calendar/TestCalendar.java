package applications.calendar;

import java.time.*;

public class TestCalendar {

	public static void main(String[] args) {
		
		//LocalDate date = LocalDate.now();
		
		LocalDate date = LocalDate.of(2020, Month.FEBRUARY, 2);
		
		System.out.println("Date: " + date);
		System.out.println("getDayOfMonth: " + date.getDayOfMonth());
		System.out.println("getDayOfYear: " + date.getDayOfYear());
		System.out.println("getMonthValue: " + date.getMonthValue());
		System.out.println("getYear: " + date.getYear());
		System.out.println("lengthOfMonth: " + date.lengthOfMonth());
		System.out.println("lengthOfYear: " + date.lengthOfYear());
		System.out.println("getDayOfWeek: " + date.getDayOfWeek());
		System.out.println("getEra: " + date.getEra());
		System.out.println("isLeapYear: " + date.isLeapYear());
		System.out.println("getMonth: " + date.getMonth());
		
		
		
		Calendar calendar = new Calendar();
		
		calendar.addEvent(10, 3, 2017, "Today is the 10th!");
		calendar.addEvent(11, 3, 2017, "Today is the 11th!");
		calendar.addEvent(12, 3, 2017, "Today is the 12th!");
		calendar.addEvent(13, 3, 2017, "Today is the 13th!");
		calendar.addEvent(14, 3, 2017, "Today is the 14th!");
		calendar.addEvent(15, 3, 2017, "Today is the 15th!");
		
		calendar.addEvent(1, 4, 2017, "Now it is April.");
		calendar.addEvent(2, 4, 2017, "Now it is April 2nd.");
		
		calendar.addEvent(1, 1, 2018, "Now it is 2018.");
		calendar.addEvent(5, 1, 2018, "Now it is Jan 5th 2018.");
		
		for(Event e : calendar.get(2018)) {
			System.out.println(e.getEvent());
		}
	}

}
