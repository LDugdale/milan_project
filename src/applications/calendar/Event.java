package applications.calendar;

class Event {
	
	private int day;
	private int month;
	private int year;
	
	private String event;
	
	public Event(int day, int month, int year, String event) {
		this.day = day;
		this.month = month;
		this.year = year;
		
		this.event = event;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getYear() {
		return year;
	}
	
	public String getEvent() {
		return event;
	}
	
	@Override
	public boolean equals(Object o) {
		Event e = null;
		try {
			e = (Event)o;
		}
		catch (ClassCastException ex) {
			return false;
		}
		return this.event.equals(e.event);
	}
}