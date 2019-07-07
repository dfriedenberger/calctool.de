package de.calctool.gui;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;


public class WebServer {
	public static void main(String args[]) throws Exception {


		Server server = new Server(new InetSocketAddress("localhost", 8080));

		// htdocs - fuer den HTML-View
		ResourceHandler resource_handler = new ResourceHandler();
		ContextHandler context0 = new ContextHandler();
		context0.setContextPath("/");
		File dir0 = new File("htdocs");
		context0.setBaseResource(Resource.newResource(dir0));
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		context0.setHandler(resource_handler);

		ServletContextHandler context1 = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context1.setContextPath("/");
		context1.setResourceBase("data");

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(new Handler[] { context0, context1 });
		server.setHandler(contexts);

		context1.addServlet(ExamplesServlet.class, "/Examples");
		context1.addServlet(MathServlet.class, "/Math");

		// Start things up!
		server.start();

		// The use of server.join() the will make the current thread join and
		// wait until the server is done executing.
		// See
		// http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
		server.join();
	}
}
