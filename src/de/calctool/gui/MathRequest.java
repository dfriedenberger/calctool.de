package de.calctool.gui;

public class MathRequest {

	private String command;
	private String session;
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	
	@Override
	public String toString() {
		return "MathRequest [command=" + command + ", session=" + session + "]";
	}
	
	
	
	

}
