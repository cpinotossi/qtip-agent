package com.akamai.qtip.cli.commands.services.iec;

import com.akamai.edgegrid.signer.ClientCredential;
import com.akamai.edgegrid.signer.apachehttpclient.ApacheHttpClientEdgeGridInterceptor;
import com.akamai.edgegrid.signer.apachehttpclient.ApacheHttpClientEdgeGridRoutePlanner;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EdgercService {
	private String apiClientSecret;
	private String apiHost;
	private String apiAccessToken;
	private String apiClientToken;
	private String apiClientName;
	private String edgercFileName;
	private String edgercFilePath;
	private String apiPapiCreateCPCodesEndpoint;
	private String apiPapiListProductsEndpoint;
	private String apiIECNamespaceConfigurationsEndpoint;
	private Wini edgerc;
	private Wini appConfigProperties;
	private ClientCredential apiCredential;
	private Logger logger;

	public EdgercService(String apiClientName, String edgercFilePath) {
		this.setLogger(LoggerFactory.getLogger(EdgercService.class));
		this.initEdgercFile(edgercFilePath);
		this.setApiClientSecret(this.getEdgerc().get(apiClientName, "client_secret"));
		this.setApiHost(this.getEdgerc().get(apiClientName, "host"));
		this.setApiAccessToken(this.getEdgerc().get(apiClientName, "access_token"));
		this.setApiClientToken(this.getEdgerc().get(apiClientName, "client_token"));
		this.setApiCredential(
				ClientCredential.builder().accessToken(this.getApiAccessToken()).clientSecret(this.getApiClientSecret())
						.clientToken(this.getApiClientToken()).host(this.getApiHost()).build());
	}

	protected void initEdgercFile(String edgercFilePath) {
		this.setEdgercFilePath(edgercFilePath);

		try {
			this.setEdgerc(new Wini(new File(this.getEdgercFilePath())));
		} catch (InvalidFileFormatException var3) {
			var3.printStackTrace();
		} catch (IOException var4) {
			var4.printStackTrace();
		}

	}

	public static String jsonFileReader(String path) throws IOException {
		String content = null;

		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			content = new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException var3) {
			var3.printStackTrace();
		}

		return content;
	}

	public InputStream fileReader(String path) throws Exception {
		File file = new File(path);
		FileInputStream fis = null;
		fis = new FileInputStream(file);
		return fis;
	}

	public static String inputStreamReader(InputStream is) throws IOException {
		String response = null;
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();

		for (int result = bis.read(); result != -1; result = bis.read()) {
			buf.write((byte) result);
		}

		try {
			response = buf.toString("UTF-8");
		} catch (UnsupportedEncodingException var6) {
			var6.printStackTrace();
		}

		return response;
	}

	public static String xml2json(String xml) {
		String json = null;

		try {
			int PRETTY_PRINT_INDENT_FACTOR = 4;
			JSONObject xmlJSONObj = XML.toJSONObject(xml);
			json = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
		} catch (JSONException var4) {
			System.out.println(var4.toString());
		}

		return json;
	}

	public String doAPIRequestGET(String apiRequestUrl)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String jsonResult = null;
		HttpClient client = null;
		ApacheHttpClientEdgeGridInterceptor interceptor = new ApacheHttpClientEdgeGridInterceptor(
				this.getApiCredential());
		ApacheHttpClientEdgeGridRoutePlanner planner = new ApacheHttpClientEdgeGridRoutePlanner(
				this.getApiCredential());
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.addInterceptorFirst(interceptor);
		clientBuilder.setRoutePlanner(planner);
		client = clientBuilder.build();
		HttpGet request = new HttpGet("https://" + this.getApiHost() + apiRequestUrl);
		jsonResult = this.executeHTTPResponseBody(client, request);
		return jsonResult;
	}

	public String doAPIRequestDELETE(String apiRequestUrl)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String jsonResult = null;
		HttpClient client = null;
		ApacheHttpClientEdgeGridInterceptor interceptor = new ApacheHttpClientEdgeGridInterceptor(
				this.getApiCredential());
		ApacheHttpClientEdgeGridRoutePlanner planner = new ApacheHttpClientEdgeGridRoutePlanner(
				this.getApiCredential());
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.addInterceptorFirst(interceptor);
		clientBuilder.setRoutePlanner(planner);
		client = clientBuilder.build();
		HttpDelete request = new HttpDelete("https://" + this.getApiHost() + apiRequestUrl);
		jsonResult = this.executeHTTPResponseBody(client, request);
		return jsonResult;
	}

	public String doAPIRequestPOST(String apiRequestUrl, String json)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String jsonResult = null;
		HttpClient client = null;
		ApacheHttpClientEdgeGridInterceptor interceptor = new ApacheHttpClientEdgeGridInterceptor(
				this.getApiCredential());
		ApacheHttpClientEdgeGridRoutePlanner planner = new ApacheHttpClientEdgeGridRoutePlanner(
				this.getApiCredential());
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.addInterceptorFirst(interceptor);
		clientBuilder.setRoutePlanner(planner);
		client = clientBuilder.build();
		HttpPost request = new HttpPost("https://" + this.getApiHost() + apiRequestUrl);
		StringEntity entity = new StringEntity(json);
		entity.setContentType(new BasicHeader("Content-Type", "application/json"));
		request.setEntity(entity);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		jsonResult = this.executeHTTPResponseBody(client, request);
		return jsonResult;
	}

	public String doAPIRequestPUT(String apiRequestUrl, String json)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String jsonResult = null;
		HttpClient client = null;
		ApacheHttpClientEdgeGridInterceptor interceptor = new ApacheHttpClientEdgeGridInterceptor(
				this.getApiCredential());
		ApacheHttpClientEdgeGridRoutePlanner planner = new ApacheHttpClientEdgeGridRoutePlanner(
				this.getApiCredential());
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.addInterceptorFirst(interceptor);
		clientBuilder.setRoutePlanner(planner);
		client = clientBuilder.build();
		HttpPut request = new HttpPut("https://" + this.getApiHost() + apiRequestUrl);
		if (!isNullOrBlank(json)) {
			StringEntity entity = new StringEntity(json);
			entity.setContentType(new BasicHeader("Content-Type", "application/json"));
			request.setEntity(entity);
		}

		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		jsonResult = this.executeHTTPResponseBody(client, request);
		return jsonResult;
	}

	protected String executeHTTPResponseBody(HttpClient client, HttpRequestBase request)
			throws IOException, ClientProtocolException, UnsupportedOperationException {
		String jsonResult = null;
		HttpResponse response = client.execute(request);
		if (response != null) {
			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder result = new StringBuilder();

			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}

			jsonResult = result.toString();
		}

		return jsonResult;
	}

	public String addValueToAPIEndPointURL(String apiEndPointURL, String key, String value) {
		if (value != null && !value.isEmpty()) {
			apiEndPointURL = apiEndPointURL.replace("/{" + key + "}", "/" + value);
			apiEndPointURL = apiEndPointURL.replace("?&{" + key + "}", "?" + key + "=" + value);
			apiEndPointURL = apiEndPointURL.replace("&{" + key + "}", "&" + key + "=" + value);
		} else {
			apiEndPointURL = apiEndPointURL.replace("/{" + key + "}", "/");
			apiEndPointURL = apiEndPointURL.replace("&{" + key + "}", "");
		}

		return apiEndPointURL;
	}

	private static boolean isNullOrBlank(String s) {
		return s == null || s.trim().equals("");
	}

	public String getApiClientSecret() {
		return this.apiClientSecret;
	}

	public String getApiHost() {
		return this.apiHost;
	}

	public String getApiAccessToken() {
		return this.apiAccessToken;
	}

	public String getApiClientToken() {
		return this.apiClientToken;
	}

	public String getApiClientName() {
		return this.apiClientName;
	}

	public void setApiClientSecret(String apiClientSecret) {
		this.apiClientSecret = apiClientSecret;
	}

	public void setApiHost(String apiHost) {
		this.apiHost = apiHost;
	}

	public void setApiAccessToken(String apiAccessToken) {
		this.apiAccessToken = apiAccessToken;
	}

	public void setApiClientToken(String apiClientToken) {
		this.apiClientToken = apiClientToken;
	}

	public void setApiClientName(String apiClientName) {
		this.apiClientName = apiClientName;
	}

	public String getEdgercFileName() {
		return this.edgercFileName;
	}

	public void setEdgercFileName(String edgercFileName) {
		this.edgercFileName = edgercFileName;
	}

	public String getEdgercFilePath() {
		return this.edgercFilePath;
	}

	public void setEdgercFilePath(String edgercFilePath) {
		this.edgercFilePath = edgercFilePath;
	}

	public String getApiPapiCreateCPCodesEndpoint() {
		return this.apiPapiCreateCPCodesEndpoint;
	}

	public void setApiPapiCreateCPCodesEndpoint(String apiPapiCreateCPCodesEndpoint) {
		this.apiPapiCreateCPCodesEndpoint = apiPapiCreateCPCodesEndpoint;
	}

	public String getApiPapiListProductsEndpoint() {
		return this.apiPapiListProductsEndpoint;
	}

	public void setApiPapiListProductsEndpoint(String apiPapiListProductsEndpoint) {
		this.apiPapiListProductsEndpoint = apiPapiListProductsEndpoint;
	}

	public String getApiIECNamespaceConfigurationsEndpoint() {
		return this.apiIECNamespaceConfigurationsEndpoint;
	}

	public void setApiIECNamespaceConfigurationsEndpoint(String apiIECNamespaceConfigurationsEndpoint) {
		this.apiIECNamespaceConfigurationsEndpoint = apiIECNamespaceConfigurationsEndpoint;
	}

	public Wini getEdgerc() {
		return this.edgerc;
	}

	public void setEdgerc(Wini edgerc) {
		this.edgerc = edgerc;
	}

	public Wini getAppConfigProperties() {
		return this.appConfigProperties;
	}

	public void setAppConfigProperties(Wini appConfigProperties) {
		this.appConfigProperties = appConfigProperties;
	}

	public ClientCredential getApiCredential() {
		return this.apiCredential;
	}

	public void setApiCredential(ClientCredential apiCredential) {
		this.apiCredential = apiCredential;
	}

	public Logger getLogger() {
		return this.logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}