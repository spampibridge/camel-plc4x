package com.kireygroup.camel.plc4x.route;

import java.util.Map;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class IntegrationRoute extends EndpointRouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from(plc4x("opcua:tcp://KG2513.kg.kireygroup.com:53530/OPCUA/SimulationServer")
				.tags(Map.of("Data", "ns=3;i=1002")))
			.log(">> Message from PLC4X ${body}");
	}
}
