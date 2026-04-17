package com.tcc.analisador_opc;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.Arrays;
import java.util.List;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

public class OpcService {
	private OpcUaClient client;

	public void connectWithFailover() {
		List<String> endpoints = Arrays.asList("opc.tcp://192.168.0.180:49320", "opc.tcp://192.168.0.237:49370");

		for (String endpoint : endpoints) {
			try {
				System.out.println("Tentando: " + endpoint);
				this.client = OpcUaClient.create(endpoint,
						endpointsDescription -> endpointsDescription.stream().findFirst(),
						configBuilder -> configBuilder.setApplicationName(LocalizedText.english("TCC Analisador"))
								.setRequestTimeout(Unsigned.uint(5000)).build());
				client.connect().get();
				System.out.println("CONECTADO: " + endpoint);
				return;
			} catch (Exception e) {
				System.err.println("Falha em " + endpoint);
			}
		}
	}

	public void subscribe(TagConfig tag) {
		try {
			UaSubscription subscription = client.getSubscriptionManager().createSubscription(1000.0).get();
			NodeId nodeId = new NodeId(tag.getNs(), tag.getAddress());
			ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);

			MonitoringParameters parameters = new MonitoringParameters(uint(1), 1000.0, null, uint(10), true);
			MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(readValueId, MonitoringMode.Reporting,
					parameters);

			subscription.createMonitoredItems(TimestampsToReturn.Both, Arrays.asList(request), (item, id) -> {
				item.setValueConsumer(value -> {
					System.out.printf("[%s] STATUS: %s%n", tag.getId(), value.getValue().getValue());
				});
			}).get();
		} catch (Exception e) {
			System.err.println("Erro na tag " + tag.getId());
		}
	}

	public OpcUaClient getClient() {
		return client;
	}
}
