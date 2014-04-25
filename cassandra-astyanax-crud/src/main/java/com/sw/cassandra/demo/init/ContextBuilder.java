package com.sw.cassandra.demo.init;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

public class ContextBuilder {

	private static String keyspaceName = "rakesh";
	private static AstyanaxContext<Keyspace> context;

	public static AstyanaxContext<Keyspace> getContext() {
		if (context == null) {
			initializeContext();
		}
		return context;
	}

	public static void initializeContext() {
		context = new AstyanaxContext.Builder()
				.forCluster("Test Cluster")
				.forKeyspace(keyspaceName)
				.withAstyanaxConfiguration(new AstyanaxConfigurationImpl().setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE))
				.withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool").setPort(9160).setMaxConnsPerHost(1).setSeeds("localhost:9160"))
				.withAstyanaxConfiguration(new AstyanaxConfigurationImpl().setCqlVersion("3.0.0").setTargetCassandraVersion("1.2"))
				.withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
				.buildKeyspace(ThriftFamilyFactory.getInstance());
		context.start();
	}

	public static Keyspace getKeySpace() {
		if (context == null) {
			initializeContext();
		}
		return context.getClient();
	}
}
