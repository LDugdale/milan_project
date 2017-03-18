package applications.calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Calendar {
	
	Map<Integer, Map<Integer, Map<Integer, List<Event>>>> calendar; // Year, Month, Day
	
	public Calendar() {
		calendar = new TreeMap<Integer, Map<Integer, Map<Integer, List<Event>>>>();
	}
	
	public void addEvent(int day, int month, int year, String event) {
		if(calendar.get(year) == null) {
			calendar.put(year, new TreeMap<Integer, Map<Integer, List<Event>>>());
		}
		if(calendar.get(year).get(month) == null) {
			calendar.get(year).put(month, new TreeMap<Integer, List<Event>>());
		}
		if(calendar.get(year).get(month).get(day) == null) {
			calendar.get(year).get(month).put(day, new ArrayList<Event>());
		}
		
		calendar.get(year).get(month).get(day).add(new Event(day, month, year, event));
	}
	
	public boolean removeEvent(Event e) {
		try {
			calendar.get(e.getYear()).get(e.getMonth()).get(e.getDay()).remove(e);
			return true;
		}
		catch (NullPointerException ex) {
			return false;
		}
	}
	
	public List<Event> getYear(int year) {
		List<Event> events = new ArrayList<Event>();
		
		Map<Integer, Map<Integer, List<Event>>> yearMap = calendar.get(year);
		
		if(yearMap != null) {
			for(Map<Integer, List<Event>> monthMap : yearMap.values()) {
				for(List<Event> eventList : monthMap.values()) {
					events.addAll(eventList);
				}
			}
		}
		
		return events;
	}
	
	public List<Event> getMonth(int year, int month) {
		List<Event> events = new ArrayList<Event>();
		
		Map<Integer, List<Event>> monthMap = null;
		
		try {
			monthMap = calendar.get(year).get(month);
		}
		catch (NullPointerException ex) { }
		
		if(monthMap != null) {
			for(List<Event> eventList : monthMap.values()) {
				events.addAll(eventList);
			}
		}
		
		return events;
	}
	
	public List<Event> getDay(int year, int month, int day) {
		List<Event> events = new ArrayList<Event>();
		try {
			events.addAll(calendar.get(year).get(month).get(day));
		}
		catch (NullPointerException ex) { }
		return events;
	}
	
	public List<Event> get(int... date) {
		if(date.length==1) return getYear(date[0]);
		else if(date.length==2) return getMonth(date[0], date[1]);
		else if(date.length==3) return getDay(date[0], date[1], date[2]);
		else return null;
	}
}
