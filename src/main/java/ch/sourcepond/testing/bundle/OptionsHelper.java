package ch.sourcepond.testing.bundle;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

import java.net.URL;

import org.ops4j.pax.exam.Option;

/**
 * @author rolandhauser
 *
 */
public class OptionsHelper {
	public static final String INTEGRATION_TEST_JAR = "/integrationtest.jar";

	/**
	 * @return
	 */
	public static Option defaultOptions() {
		final URL integrationTestJar = OptionsHelper.class.getResource(INTEGRATION_TEST_JAR);
		assertNotNull(INTEGRATION_TEST_JAR + " could not be found in classpath!", integrationTestJar);
		return composite(mavenBundle("com.google.guava", "guava").versionAsInProject(),
				mavenBundle("org.apache.commons", "commons-lang3").versionAsInProject(),
				mavenBundle("org.mockito", "mockito-all").versionAsInProject(), bundle(integrationTestJar.toString()),
				frameworkProperty("felix.bootdelegation.implicit").value("false"), junitBundles());
	}
}
