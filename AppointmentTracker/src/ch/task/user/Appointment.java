package ch.task.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Appointment implements Comparable<Appointment> {

	private String startDate;
	private String dueDate;
	private String title;
	private String comment;
	private boolean completed;
	private boolean due;

	@JsonCreator
	public Appointment(@JsonProperty("startDate") String start, @JsonProperty("dueDate") String dueD,
			@JsonProperty("userTitle") String userTitle, @JsonProperty("comm") String comm,
			@JsonProperty("compeleted") boolean complete, @JsonProperty("due") boolean isDue) {
		startDate = start;
		dueDate = dueD;
		title = userTitle;
		comment = comm;
		completed = complete;
		due = checkIfDue();
	}

	/*
	 * @return start date of appointment
	 */
	public String getStartDate() {
		return startDate;
	}
	
	/*
	 * @param startDate starting date of appointment
	 */

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/*
	 * @return due date of appointment
	 */
	public String getDueDate() {
		return dueDate;
	}

	/*
	 * @param dueDate due date of appointment
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	
	public String getTitle() {
		return title;
	}

	/*
	 * @param title title of appointment
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	/*
	 * @param desc comment details for appointment
	 */
	public void setComment(String desc) {
		this.comment = desc;
	}

	public boolean isCompleted() {
		return completed;
	}

	/*
	 * @param completed boolean to determine if appointment is completed
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isDueToday() {
		return due;
	}
	
	/*
	 * @param startDate starting date of appointment
	 */
	public void setDueToday(boolean dueToday) {
		this.due = dueToday;
	}
	
	
	public boolean checkIfDue() {
		LocalDate today = LocalDate.now();
		LocalDate dueDate = LocalDate.parse(this.dueDate);
		boolean due = today.plusDays(1).isAfter(dueDate) ? true : false;
		if(due) {
			this.due = true;
		}
		return due;
	}
	
	public String detailedView() {
		String format = "Start Date: " + startDate + "\nDue Date: " + dueDate + "\nTitle: " + title + "\nCompleted: " + completed + 
				"\nDue Today: " + due + "\nComment: " + comment;
		return format;
	}
	
	public String reformatDate(String date) {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String formattedDate = LocalDate.parse(date).format(f);
		return formattedDate;
	}

	@Override
	public String toString() {
		String startReformat = reformatDate(startDate);
		String dueReformat = reformatDate(dueDate);
		String format = String.format("%-15s %-15s %s", startReformat, dueReformat, title);
		return format;
	}
	
	@Override
	public int compareTo(Appointment o) {
		LocalDate dueDate = LocalDate.parse(this.dueDate, DateTimeFormatter.ISO_DATE);
		LocalDate otherDueDate = LocalDate.parse(o.getDueDate(), DateTimeFormatter.ISO_DATE);
		return dueDate.compareTo(otherDueDate);
	}

}
