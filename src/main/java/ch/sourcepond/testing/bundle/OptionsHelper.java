package ch.sourcepond.testing.bundle;

import static org.ops4j.pax.cdi.test.support.TestConfiguration.cdiProviderBundles;
import static org.ops4j.pax.cdi.test.support.TestConfiguration.paxCdiProviderAdapter;
import static org.ops4j.pax.cdi.test.support.TestConfiguration.regressionDefaults;
import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import org.ops4j.pax.exam.Option;

/**
 * @author rolandhauser
 *
 */
public class OptionsHelper {

	/**
	 * @return
	 */
	public static Option defaultOptions() {
		return composite(regressionDefaults(), paxCdiProviderAdapter(), cdiProviderBundles(),
				mavenBundle("com.google.guava", "guava").versionAsInProject(),
				mavenBundle("org.apache.commons", "commons-lang3").versionAsInProject(),
				mavenBundle("org.mockito", "mockito-core").versionAsInProject(),
				frameworkProperty("felix.bootdelegation.implicit").value("false"), junitBundles(),

				// Override regression default; we use the logback.xml located
				// in this jar
				systemProperty("logback.configurationFile").value(""));
	}
}
