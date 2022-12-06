package com.example.gbdpuserac.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * The class Rest client details service.
 *
 */
@Component("restClientDetailsService")
public class RestClientDetailsServiceImpl extends JdbcClientDetailsService {
	private final Logger logger = LoggerFactory.getLogger(RestClientDetailsServiceImpl.class);
	private static final String RESOURCE_ID = "";
	private ClientDetailsService clientDetailsService;

	public RestClientDetailsServiceImpl(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * Load client by client id client details.
	 *
	 * @param clientId the client id
	 *
	 * @return the client details
	 *
	 * @throws ClientRegistrationException the client registration exception
	 */
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		return super.loadClientByClientId(clientId);
	}
}
