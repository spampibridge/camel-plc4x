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
		
		from(timer("opcua").period(10000))
	        .pollEnrich(plc4x("opcua:tcp://KG2513.kg.kireygroup.com:53530/OPCUA/SimulationServer")
	        		.autoReconnect(true)
	        		.tags(Map.of("Data", "ns=3;i=1002")), 5000)
	        .autoStartup(false)
	        .log(LoggingLevel.INFO, LOG, ">> Message from opcua ${body}");
		
		from(timer("modbus").period(10000))
	        .pollEnrich(plc4x("modbus-tcp://localhost:502")
	        		.autoReconnect(true)
	        		.tags(Map.of(
	    					"coil-1", "coil:1", 
	    					"coil-2", "coil:2",
	    					"coil-3", "coil:3",
	    					"coil-4", "coil:4",
	    					"coil-5", "coil:5",
	    					"holding-register-1", "holding-register:1")), 5000)
	        .autoStartup(false)
	        .log(LoggingLevel.INFO, LOG, ">> Message from modbus ${body}");
		
		from(timer("s7").period(10000))
	        .pollEnrich(plc4x("s7://localhost:102")
	        		.autoReconnect(true)
	        		.tags(Map.of(
	        				"var1", "RAW(%DB1.DBX0.0:BOOL[1])",
	        				"var2", "RAW(%DB1.DBX0.1:BOOL[1])",
	        				"var3", "RAW(%DB1.DBX0.2:BOOL[1])",
	        				"var4", "RAW(%DB1.DBX0.3:BOOL[1])")), 5000)
	        .autoStartup(true)
	        .log(LoggingLevel.INFO, LOG, ">> Message from s7 ${body}");
	}
}
