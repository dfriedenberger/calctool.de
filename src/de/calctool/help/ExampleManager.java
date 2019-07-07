package de.calctool.help;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExampleManager {

	private String path;

	public ExampleManager(String path) {
		this.path = path;
	}

	public String[] listExampleKeys() {
	
		String n[] = new File(path).list(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("0") && name.endsWith(".txt");
			}});
		
		Arrays.sort(n);
		
		return n;
	}

	public String getTitle(String eKey) {
		
		List<String> files = readFile(path + "/" + eKey, new LineFilter() {

			@Override
			boolean match(String line) {
				return line.startsWith("#");
			}});
		
		return files.get(0).substring(1);
	}

	

	public String[] getCommands(String eKey) {
		List<String> files = readFile(path + "/" + eKey, new LineFilter() {

			@Override
			boolean match(String line) {
				return !line.startsWith("#") && !line.trim().isEmpty();
			}});
		
		return files.toArray(new String[0]);
	}

	public String getCommandDescription(String command) {
		int ix = command.indexOf(":");
		return command.substring(0,ix).trim();
	}

	public String getCommand(String command) {
		int ix = command.indexOf(":");
		return command.substring(ix + 1).trim();
	}

	
	
	private List<String> readFile(String filename, LineFilter lineFilter) {

		List<String> lines = new ArrayList<String>();
		BufferedReader in = null;
		try {
			 in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			 
			 while(true)
			 {
				 String line = in.readLine();
				 if(line == null) break;
				 if(lineFilter.match(line))
					 lines.add(line);
			 }
			 
			 
		} catch (IOException e) {
			e.printStackTrace();
			if(in != null)
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
		
		
		
		return lines;
	}
	
}
