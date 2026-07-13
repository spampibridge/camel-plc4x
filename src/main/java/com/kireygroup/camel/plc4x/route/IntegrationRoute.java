package com.kireygroup.camel.plc4x.route;

import java.util.Map;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IntegrationRoute extends EndpointRouteBuilder {
	
	private static final Logger LOG = LoggerFactory.getLogger(IntegrationRoute.class);

	@Override
	public void configure() throws Exception {
		
		from(plc4x("opcua:tcp://KG2513.kg.kireygroup.com:53530/OPCUA/SimulationServer")
				.tags(Map.of("Data", "ns=3;i=1002")))
			.autoStartup(false)
			.log(">> Message from PLC4X ${body}");
		
		from(timer("modbus").period(10000))
	        .pollEnrich(plc4x("modbus-tcp://localhost:502")
	        		.tags(Map.of(
	    					"coil-1", "coil:1", 
	    					"coil-2", "coil:2",
	    					"coil-3", "coil:3",
	    					"coil-4", "coil:4",
	    					"coil-5", "coil:5",
	    					"holding-register-1", "holding-register:1")), 5000)
	        .log(LoggingLevel.INFO, LOG, ">> Message from modbus ${body}");
	}
}
