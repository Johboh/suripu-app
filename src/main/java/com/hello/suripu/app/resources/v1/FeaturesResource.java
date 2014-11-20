package com.hello.suripu.app.resources.v1;

import com.hello.suripu.core.db.FeatureStore;
import com.hello.suripu.core.models.Feature;
import com.hello.suripu.core.oauth.AccessToken;
import com.hello.suripu.core.oauth.OAuthScope;
import com.hello.suripu.core.oauth.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/v1/features")
public class FeaturesResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeaturesResource.class);
    private final FeatureStore featureStore;

    public FeaturesResource(final FeatureStore featureStore) {
        this.featureStore = featureStore;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setFeature(@Scope(OAuthScope.ADMINISTRATION_WRITE) final AccessToken accessToken, @Valid Feature feature) {
        LOGGER.info("Saving feature: {}", feature);
        featureStore.put(feature);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Feature> listFeatures(@Scope(OAuthScope.ADMINISTRATION_READ) final AccessToken accessToken) {
        return featureStore.getAllFeatures();
    }
}
