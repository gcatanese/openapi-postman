package com.github.openapilab.openapipostman.views;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.shared.ui.LoadMode;
import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Tag("my-recaptcha")
public class ReCaptcha extends Component {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReCaptcha .class);

    private final String secretKey;

    private boolean valid;

    public ReCaptcha(String websiteKey, String secretKey) {
        this.secretKey = secretKey;


        Div div = new Div();
        div.setId("a");
        div.addClassName("g-recaptcha");
        div.getElement().setAttribute("data-sitekey", websiteKey);
        div.getElement().setAttribute("data-callback", "onSubmitToken");
        div.getElement().setAttribute("data-theme", "light");
        //div.getElement().setAttribute("data-size", "invisible");
        getElement().appendChild(div.getElement());

        UI.getCurrent().getPage().executeJs("$0.init = function () {\n" +
                "    function onSubmitToken(token) {\n" +
                "        $0.$server.callback(token);\n" +
                "    }\n" +
                "    window.onSubmitToken = onSubmitToken;" + // See myCallback comment above.
                "};\n" +
                "$0.init();\n", this);

        UI.getCurrent().getPage().addJavaScript("https://www.google.com/recaptcha/api.js", LoadMode.LAZY);
    }


    public void execute() {
        UI.getCurrent().getPage().executeJs("grecaptcha.execute();");
    }

    public boolean isValid() {
        return valid;
    }

    @ClientCallable
    public void callback(String response) {
        try {
            valid = checkResponse(response);
            LOGGER.info("reCaptcha challenge? " + valid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkResponse(String response) throws IOException {
        String remoteAddr = getRemoteAddr(VaadinService.getCurrentRequest());

        String url = "https://www.google.com/recaptcha/api/siteverify";

        String postData = "secret=" + URLEncoder.encode(secretKey, "UTF-8") +
                "&remoteip=" + URLEncoder.encode(remoteAddr, "UTF-8") +
                "&response=" + URLEncoder.encode(response, "UTF-8");

        String result = doHttpPost(url, postData);

        JsonObject parse = Json.parse(result);
        JsonValue jsonValue = parse.get("success");
        return jsonValue != null && jsonValue.asBoolean();
    }

    private static String getRemoteAddr(VaadinRequest request) {
        String ret = request.getHeader("x-forwarded-for");
        if (ret == null || ret.isEmpty()) {
            ret = request.getRemoteAddr();
        }
        return ret;
    }

    private static String doHttpPost(String urlStr, String postData) throws IOException {
        URL url = new URL(urlStr);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        try {

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setReadTimeout(10_000);
            con.setConnectTimeout(10_000);
            con.setUseCaches(false);

            try (OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8)) {
                writer.write(postData);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
            }

            return response.toString();
        } finally {
            con.disconnect();
        }
    }
}
