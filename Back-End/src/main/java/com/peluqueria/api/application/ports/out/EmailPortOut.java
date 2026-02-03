package com.peluqueria.api.application.ports.out;

/**
 * The Interface EmailPortOut.
 */
public interface EmailPortOut {
	
	/**
	 * Send appointment notification.
	 *
	 * @param email the email
	 * @param clientName the client name
	 * @param serviceName the service name
	 * @param date the date
	 * @param status the status
	 */
	void sendAppointmentNotification(String email, String clientName, String serviceName, String date, String status);
	
	/**
	 * Send admin notification.
	 *
	 * @param adminEmail the admin email
	 * @param clientName the client name
	 * @param service the service
	 * @param date the date
	 */
	public void sendAdminNotification(String adminEmail, String clientName, String service, String date);
	
	/**
	 * Send appointment reminder.
	 *
	 * @param to the to
	 * @param clientName the client name
	 * @param date the date
	 * @param time the time
	 * @param service the service
	 */
	public void sendAppointmentReminder(String to, String clientName, String date, String time, String service);
	
	/**
	 * Send urgent reminder.
	 *
	 * @param to the to
	 * @param clientName the client name
	 * @param time the time
	 * @param service the service
	 */
	public void sendUrgentReminder(String to, String clientName, String time, String service);
}
