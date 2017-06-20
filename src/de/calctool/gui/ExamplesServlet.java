package de.calctool.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.calctool.help.ExampleManager;

public class ExamplesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ExampleManager exampleManager = new ExampleManager("help");
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		ObjectMapper mapper = new ObjectMapper();

		ExampleRequest req = mapper.readValue(request.getInputStream(), ExampleRequest.class);

		
		ExampleResponse resp = new ExampleResponse();
		List<Example> examples = new ArrayList<Example>();
		
		
		for(String eKey : exampleManager.listExampleKeys())
		{
		   
			Example ex = new Example();
			ex.setTitle(exampleManager.getTitle(eKey));
			List<ExampleCommand> commands = new ArrayList<ExampleCommand>();

			for(String cmd : exampleManager.getCommands(eKey))
			{
				ExampleCommand c = new ExampleCommand();
				c.setDescription(exampleManager.getCommandDescription(cmd));
				c.setCommand(exampleManager.getCommand(cmd));
				commands.add(c);
			}
			ex.setCommands(commands.toArray(new ExampleCommand[0]));
			examples.add(ex);
		
		}
		resp.setExamples(examples.toArray(new Example[0]));
		response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);

		
		mapper.writeValue(response.getWriter(), resp);
	}

}
