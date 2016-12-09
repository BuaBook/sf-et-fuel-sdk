package com.buabook.exacttarget.fuelsdk;

import java.util.logging.Logger;

import com.exacttarget.fuelsdk.ETClient;
import com.exacttarget.fuelsdk.ETDataExtension;
import com.exacttarget.fuelsdk.ETFilter;
import com.exacttarget.fuelsdk.ETSdkException;

/**
 * <h3>ExactTarget API Interface</h3>
 * <p>Simplified interface to querying ExactTarget via FueldSDK</p>
 * <p>(c) 2016 Sport Trades Ltd</p>
 * <p>License GPLv3</p>
 * 
 * @author Jas Rajasansir
 * @version 1.0.0
 * @since 8 Dec 2016
 */
public class ExactTargetInterface {
	private static final Logger log = Logger.getLogger(ExactTargetInterface.class.getName());

	
	protected final ETClient client;
	
	
	public ExactTargetInterface(ETClient client) throws IllegalArgumentException {
		if(client == null)
			throw new IllegalArgumentException("ETClient must be provided");
		
		this.client = client;
		
		log.info("ExactTarget interface initialised [ Access Token: " + client.getAccessToken() + " ]");
	}
	
	/**
	 * Provides access to a data extension, querying by the data extension's name (as shown on the ExactTarget web interface)
	 * @param extensionName The name of the data extension to query
	 * @return The data extension
	 * @throws IllegalArgumentException If the name supplied is <code>null</code> or empty string
	 */
	public ETDataExtension getDataExtension(String extensionName) throws IllegalArgumentException, ETSdkException {
		if(extensionName == null || extensionName.isEmpty())
			throw new IllegalArgumentException("Empty extension name is not permitted");
		
		String parseString = "Name=" + extensionName;

		log.fine("Attempting to retrieve Data Extension from ExactTarget [ Name: " + parseString + " ]");
		
		ETFilter filter = ETFilter.parse(parseString);
		
		return client.retrieveObject(ETDataExtension.class, filter);
	}

}
