package is.hello.supichi.models.responsebuilder;

import is.hello.supichi.models.HandlerResult;

/**
 * Created by ksg on 8/9/16
 */
public interface ResponseBuilderInterface {

    // MUST-HAVES
    String BUCKET_NAME = "";
    String FILENAME_PREFIX = "";

    static BuilderResponse response(HandlerResult handlerResult, String voiceService, String voiceName) {
        return BuilderResponse.empty();
    }

}
