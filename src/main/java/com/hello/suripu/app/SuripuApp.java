package com.hello.suripu.app;

import com.hello.suripu.app.configuration.SuripuAppConfiguration;
import com.hello.suripu.app.resources.AccountResource;
import com.hello.suripu.app.resources.HistoryResource;
import com.hello.suripu.app.resources.OAuthResource;
import com.hello.suripu.core.Account;
import com.hello.suripu.core.db.AccountDAOImpl;
import com.hello.suripu.core.db.TimeSerieDAO;
import com.hello.suripu.core.oauth.*;
import com.hello.suripu.service.db.JodaArgumentFactory;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;
import org.skife.jdbi.v2.DBI;

public class SuripuApp extends Service<SuripuAppConfiguration> {
    public static void main(String[] args) throws Exception {
        new SuripuApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<SuripuAppConfiguration> bootstrap) {

    }

    @Override
    public void run(SuripuAppConfiguration config, Environment environment) throws Exception {


        final OAuthTokenStore<AccessToken,ClientDetails, ClientCredentials> tokenStore = new InMemoryOAuthTokenStore();

        // Temporary, don't have a register page
        final Account account = new Account("tim@sayhello.com");
        OAuthScope[] scopes = new OAuthScope[1];
        scopes[0] = OAuthScope.USER_BASIC;
        final ClientDetails clientDetails = new ClientDetails(
                "responseType",
                "clientId",
                "redirectUri",
                scopes,
                "state",
                "code",
                1L,
                "secret"
        );

        tokenStore.storeAccessToken(clientDetails);
        tokenStore.storeAuthorizationCode(clientDetails);
        final AccountDAOImpl accountDAO = new AccountDAOImpl();

        final DBIFactory factory = new DBIFactory();

        final DBI jdbi = factory.build(environment, config.getDatabaseConfiguration(), "postgresql");
        jdbi.registerArgumentFactory(new JodaArgumentFactory());
        final TimeSerieDAO timeSerieDAO = jdbi.onDemand(TimeSerieDAO.class);


//        environment.addProvider(new OAuthProvider<ClientDetails>(new OAuthAuthenticator(tokenStore), "protected-resources"));

        environment.addProvider(new OAuthProvider<ClientDetails>(new OAuthAuthenticator(tokenStore), "blah"));

        environment.addResource(new OAuthResource(tokenStore));
        environment.addResource(new AccountResource(accountDAO));
        environment.addResource(new HistoryResource(timeSerieDAO));
    }
}
