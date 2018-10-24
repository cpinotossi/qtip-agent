package com.akamai.qtip.cli.commands.services.iec;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;

public class NamespaceService extends EdgercService {
	public NamespaceService(String apiClientName, String edgercFilePath) {
		super(apiClientName, edgercFilePath);
	}

	public String getListReservedNamespaces(String size, String page)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces?&{size}&{page}";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "size", size);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "page", page);
		return this.doAPIRequestGET(apiEndpoint);
	}

	public String postReserveNamespace(String requestBody)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces";
		return this.doAPIRequestPOST(apiEndpoint, requestBody);
	}

	public String getListAllReservedNamespaces(String global, String match, String detail)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/search?&{global}&{match}&{detail}";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "global", global);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "match", match);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "detail", detail);
		return this.doAPIRequestGET(apiEndpoint);
	}

	public String deleteRemoveNamespace(String namespace)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/{namespace}";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "namespace", namespace);
		return this.doAPIRequestDELETE(apiEndpoint);
	}

	public String getListAllNamespaceConfigurations(String namespace)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/{namespace}/configurations";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "namespace", namespace);
		return this.doAPIRequestGET(apiEndpoint);
	}

	public String postCreateNamespaceConfiguration(String namespace, String requestBody)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/{namespace}/configurations";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "namespace", namespace);
		return this.doAPIRequestPOST(apiEndpoint, requestBody);
	}

	public String getNamespaceConfiguration(String namespace, String jurisdiction)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/{namespace}/configurations/{jurisdiction}";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "namespace", namespace);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "jurisdiction", jurisdiction);
		return this.doAPIRequestGET(apiEndpoint);
	}

	public String putUpdateNamespaceConfiguration(String namespace, String jurisdiction, String requestBody)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/{namespace}/configurations/{jurisdiction}";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "namespace", namespace);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "jurisdiction", jurisdiction);
		return this.doAPIRequestPUT(apiEndpoint, requestBody);
	}

	public String deleteNamespaceConfiguration(String namespace, String jurisdiction)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/{namespace}/configurations/{jurisdiction}";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "namespace", namespace);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "jurisdiction", jurisdiction);
		return this.doAPIRequestDELETE(apiEndpoint);
	}

	public String getVersionsNamespaceConfiguration(String namespace, String jurisdiction)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/{namespace}/configurations/{jurisdiction}/versions";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "namespace", namespace);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "jurisdiction", jurisdiction);
		return this.doAPIRequestGET(apiEndpoint);
	}

	public String postCreateVersionOfNamespaceConfiguration(String namespace, String jurisdiction, String requestBody)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/{namespace}/configurations/{jurisdiction}/versions";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "namespace", namespace);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "jurisdiction", jurisdiction);
		return this.doAPIRequestPOST(apiEndpoint, requestBody);
	}

	public String putDeactivateVersionOfNamespaceConfiguration(String namespace, String jurisdiction,
			String activationState) throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/{namespace}/configurations/{jurisdiction}/versions/active?&{activation-state}";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "namespace", namespace);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "jurisdiction", jurisdiction);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "activation-state", activationState);
		return this.doAPIRequestPUT(apiEndpoint, (String) null);
	}

	public String putActivateVersionOfNamespaceConfiguration(String namespace, String jurisdiction, String version,
			String activationState) throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/{namespace}/configurations/{jurisdiction}/versions/{version}?&{activation-state}";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "namespace", namespace);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "jurisdiction", jurisdiction);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "version", version);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "activation-state", activationState);
		return this.doAPIRequestPUT(apiEndpoint, (String) null);
	}

	public String getListAllOperationsForConfigurationVersions(String namespace, String jurisdiction)
			throws ClientProtocolException, UnsupportedOperationException, IOException {
		String apiEndpoint = "/dcp-api/v1/namespaces/{namespace}/configurations/{jurisdiction}/versions/activations";
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "namespace", namespace);
		apiEndpoint = this.addValueToAPIEndPointURL(apiEndpoint, "jurisdiction", jurisdiction);
		return this.doAPIRequestGET(apiEndpoint);
	}
}