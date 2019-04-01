package eu.esens.espdvcd.designer.endpoint;

import eu.esens.espdvcd.designer.util.AppInfo;
import eu.esens.espdvcd.designer.util.JsonUtil;
import spark.Service;

public class PlatformInfoEndpoint extends Endpoint {
    @Override
    public void configure(Service spark, String basePath) {
        spark.get(basePath, (req,resp)-> AppInfo.getInstance().getInfo(), JsonUtil.json());
        spark.after((req, res) -> res.type("application/json"));
    }
}
