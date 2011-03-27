package dk.osaa.gb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class GearBook {
	static Logger log = Logger.getLogger(GearBook.class.getName());

	public static void main(String[] args) {
		Server server = new Server(Integer.getInteger("dk.osaa.gb.port", 8000));

		try {
			ResourceHandler resource_handler = new ResourceHandler();
			resource_handler.setDirectoriesListed(true);
			resource_handler.setWelcomeFiles(new String[]{ "index.html" });
			File root = new File(System.getProperty("dk.osaa.gb.root", "root")).getAbsoluteFile();
			System.err.println("Serving files out of "+root);
			resource_handler.setResourceBase(root.getAbsolutePath());

			HandlerList handlers = new HandlerList();
			handlers.setHandlers(new Handler[] {
					resource_handler,
					new NewGearHandler(),
					new DefaultHandler()
			});
			server.setHandler(handlers);

			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
