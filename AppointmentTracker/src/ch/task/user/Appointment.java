package ch.task.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Appointment implements Comparable<Appointment>, Cloneable {

	private UUID id;
	private String startDate;
	private String dueDate;
	private String title;
	private String comment;
	private String creator;
	private boolean completed;
	private boolean due;

	@JsonCreator
	public Appointment(@JsonProperty("appID") UUID appID, @JsonProperty("startDate") String startDate,
			@JsonProperty("dueDate") String dueDate, @JsonProperty("userTitle") String title,
			@JsonProperty("comm") String comment, @JsonProperty("creator") String creator,
			@JsonProperty("compeleted") boolean completed, @JsonProperty("due") boolean isDue) {
		setId(appID != null ? appID : UUID.randomUUID());
		setStartDate(startDate);
		setDueDate(dueDate);
		setTitle(title);
		setComment(comment);
		setCreator(creator);
		setCompleted(completed);
		checkIfDue();
	}
	
	public Appointment(String startDate, String dueDate, String title, String comment, String creator) {
		this(null, startDate, dueDate, title, comment, creator, false, false);
	}

	/*
	 * @return appointment ID
	 */
	public UUID getId() {
		return id;
	}

	/*
	 * @param id appointment id
	 */
	public void setId(UUID id) {
		this.id = id;
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

	/*
	 * @return title of appointment
	 */
	public String getTitle() {
		return title;
	}

	/*
	 * @param title title of appointment
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * @return appointment comment
	 */
	public String getComment() {
		return comment;
	}

	/*
	 * @param desc comment details for appointment
	 */
	public void setComment(String desc) {
		this.comment = desc;
	}

	/*
	 * @return appointment creator
	 */
	public String getCreator() {
		return creator;
	}

	/*
	 * @param userCreator username who created appointment
	 */
	public void setCreator(String userCreator) {
		creator = userCreator;
	}

	/*
	 * @return if appointment is completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	/*
	 * @param completed boolean to determine if appointment is completed
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/*
	 * @return if appointment is due today
	 */
	public boolean isDueToday() {
		return due;
	}

	/*
	 * @param startDate starting date of appointment
	 */
	public void setDueToday(boolean dueToday) {
		this.due = dueToday;
	}

	/*
	 * Check to see if appointment due date has arrived/passed
	 * 
	 * @return if due date has arrived/passed
	 */
	public boolean checkIfDue() {
		LocalDate today = LocalDate.now();
		LocalDate dueDate = LocalDate.parse(this.dueDate);
		boolean due = today.plusDays(1).isAfter(dueDate);
		if (due) {
			this.due = true;
		}
		return due;
	}

	/*
	 * Formats date to MM/dd/yyyy
	 * 
	 * @param date date string to be formatted
	 */
	public String reformatDate(String date) {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String formattedDate = LocalDate.parse(date).format(f);
		return formattedDate;
	}
	
	
	/*
	 * @return clone of this object
	 */
	@Override
	protected Appointment clone() {
		return new Appointment(id, startDate, dueDate, title, comment, creator, completed, due);
	}

	/*
	 * return formatted start/due date and title of appointment
	 * 
	 * @return formatted string
	 */
	@Override
	public String toString() {
		String startReformat = reformatDate(startDate);
		String dueReformat = reformatDate(dueDate);
		String format = String.format("%-15s %-15s %s", startReformat, dueReformat, title);
		return format;
	}

	/*
	 * compares two appointment objects by due date
	 * 
	 * @param otherApp appointment object to be compared to
	 */
	@Override
	public int compareTo(Appointment otherApp) {
		LocalDate dueDate = LocalDate.parse(this.dueDate, DateTimeFormatter.ISO_DATE);
		LocalDate otherDueDate = LocalDate.parse(otherApp.getDueDate(), DateTimeFormatter.ISO_DATE);
		return dueDate.compareTo(otherDueDate);
	}

}
