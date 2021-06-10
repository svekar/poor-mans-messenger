package org.example.quarkus;

import java.util.Collections;
import java.util.Map;

import org.testcontainers.containers.PostgreSQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class PostgreSQLResource implements QuarkusTestResourceLifecycleManager {

	 static PostgreSQLContainer<?> db =
		      new PostgreSQLContainer<>("postgres:13.3") 
		        .withDatabaseName("pmmdb")
		        .withUsername("postgres")
		        .withPassword("postgres");

		  @Override
		  public Map<String, String> start() { 
		    db.start();
		    return Collections.singletonMap(
		        "quarkus.datasource.jdbc.url", db.getJdbcUrl()
		    );
		  }

		  @Override
		  public void stop() { 
		    db.stop();
		  }

}
