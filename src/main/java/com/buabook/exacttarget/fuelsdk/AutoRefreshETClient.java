package com.buabook.exacttarget.fuelsdk;

import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.exacttarget.fuelsdk.ETClient;
import com.exacttarget.fuelsdk.ETConfiguration;
import com.exacttarget.fuelsdk.ETSdkException;

/**
 * <h3>{@link ETClient} with Auto Refresh of Refresh Token</h3>
 * <p>The default {@link ETClient} does not refresh the refresh token and, as per OAuth standards, expires
 * after 14 days. This class extends {@link ETClient} and force refreshes the token after the configured
 * amount of time.</p>
 * <p><b>NOTE</b>: Due to the methods available to sub-classes, when the refresh token requires refreshing, 2 API 
 * calls will be made to ExactTarget. Normal access token refreshes will behave as before.</p>
 * <p>(c) 2016 Sport Trades Ltd</p>
 * <p>License GPLv3</p>
 * 
 * @author Jaskirat M.S. Rajasansir
 * @version 1.0.0
 * @since 8 Dec 2016
 */
public class AutoRefreshETClient extends ETClient {
	private static final Logger log = Logger.getLogger(AutoRefreshETClient.class.getName());
	
	private static final Duration DEFAULT_REFRESH_TOKEN_LIFE = Duration.standardDays(1);
	
	
	private Duration refreshTokenLife;

	private DateTime lastRefreshTime;
	

	/**
	 * Provides a functionally equivalent version of {@link ETClient} with automatic refresh of the refresh token based on
	 * the supplied duration
	 * @param refreshTokenLife The duration between refresh token refreshes. Pass <code>null</code> to use the default 
	 * ({@link #DEFAULT_REFRESH_TOKEN_LIFE})
	 * @return A new client with auto refresh of the refresh token enabled
	 * @see ETClient#ETClient(String)
	 */
	public static AutoRefreshETClient newRefreshClient(String file, Duration refreshTokenLife) throws ETSdkException {
		if(refreshTokenLife == null)
			refreshTokenLife = DEFAULT_REFRESH_TOKEN_LIFE;
		
		AutoRefreshETClient client = new AutoRefreshETClient(file);
		client.initialiseRefreshTracking(refreshTokenLife);
		
		return client;
	}
	
	/**
	 * Provides a functionally equivalent version of {@link ETClient} with automatic refresh of the refresh token based on
	 * the supplied duration
	 * @param refreshTokenLife The duration between refresh token refreshes. Pass <code>null</code> to use the default
	 * ({@link #DEFAULT_REFRESH_TOKEN_LIFE})
	 * @return A new client with auto refresh of the refresh token enabled
	 * @see ETClient#ETClient(ETConfiguration)
	 */
	public static AutoRefreshETClient newRefreshClient(ETConfiguration configuration, Duration refreshTokenLife) throws ETSdkException {
		if(refreshTokenLife == null)
			refreshTokenLife = DEFAULT_REFRESH_TOKEN_LIFE;
		
		AutoRefreshETClient client = new AutoRefreshETClient(configuration);
		client.initialiseRefreshTracking(refreshTokenLife);
		
		return client;
	}
	
	
	/**
	 * Performs the standard {@link ETClient#refreshToken()} with the added refresh of the refresh token if 
	 * it has been greater than the specified duration ({@link #refreshTokenLife}) since the last refresh.
	 */
	@Override
	public synchronized String refreshToken() throws ETSdkException {
		if(lastRefreshTime != null && lastRefreshTime.plus(refreshTokenLife).isBeforeNow()) {
			log.info("Refreshing refresh token after configured duration");
			
			lastRefreshTime = DateTime.now();
			requestToken(null);
		} 

		return super.refreshToken();
	}
	
	public DateTime getLastRefreshTime() {
		return lastRefreshTime;
	}
	
	
	/** Private constructor as calling this directly, without {@link #initialiseRefreshTracking(Duration)}, just gives you an {@link ETClient} */
	private AutoRefreshETClient(String file) throws ETSdkException {
		super(file);
	}

	/** Private constructor as calling this directly, without {@link #initialiseRefreshTracking(Duration)}, just gives you an {@link ETClient} */
	private AutoRefreshETClient(ETConfiguration configuration) throws ETSdkException {
		super(configuration);
	}
	
	private void initialiseRefreshTracking(Duration refreshTokenLife) throws IllegalArgumentException {
		if(refreshTokenLife == null || refreshTokenLife.isEqual(Duration.ZERO))
			throw new IllegalArgumentException("Invalid refresh token life duration");
		
		this.refreshTokenLife = refreshTokenLife;
		this.lastRefreshTime = DateTime.now();
		
		log.info("ExactTarget client with auto-refresh of refresh token created [ Refresh Duration: " + refreshTokenLife + " ]");
	}

}
