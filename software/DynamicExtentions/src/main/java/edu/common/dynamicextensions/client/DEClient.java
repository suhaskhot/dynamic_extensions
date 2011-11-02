package edu.common.dynamicextensions.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URLConnection;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.utility.HTTPSConnection;

public class DEClient extends AbstractClient {

	@Override
	protected void initializeResources(String[] args)
			throws DynamicExtensionsSystemException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validate(String[] args)
			throws DynamicExtensionsSystemException {
		// TODO Auto-generated method stub

	}
	@Override
	protected void performAction(HTTPSConnection httpsConnection,
			URLConnection servletConnection)
			throws DynamicExtensionsSystemException, IOException {
		ObjectOutputStream outputToServlet = new ObjectOutputStream(servletConnection
				.getOutputStream());
		outputToServlet.writeObject(paramaterObjectMap);
		outputToServlet.flush();
		outputToServlet.close();
	}

}
