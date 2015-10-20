package ch.sourcepond.testing.bundle;

import static org.ops4j.pax.cdi.test.support.TestConfiguration.cdiProviderBundles;
import static org.ops4j.pax.cdi.test.support.TestConfiguration.paxCdiProviderAdapter;
import static org.ops4j.pax.cdi.test.support.TestConfiguration.regressionDefaults;
import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;

import org.ops4j.pax.exam.Option;

/**
 * @author rolandhauser
 *
 */
public class OptionsHelper {

	/**
	 * Creates the default options necessary to test a SourcePond component in
	 * an OSGi environment. All SourcePond components are split into an API
	 * (example artifactId ch.sourcepond.componentname.api) and implementations
	 * (example artifactId ch.sourcepond.componentname.impl). The arguments
	 * passed to this method are always component names without *.api or *.impl
	 * at their end, for instance for the component
	 * ch.sourcepond.mdcwrapper.impl you would pass ch.sourcepond.mdcwrapper.
	 * 
	 * @param pExaminee
	 *            The name of the component to be tested
	 * @param pDependencyComponent
	 *            The names of the components which shall additionally be
	 *            installed into the test-container; can be empty.
	 * @return Option to be passed to the Pax-Exam test-container.
	 */
	public static Option defaultOptions(final String pExaminee, final String... pDependencyComponent) {
		return composite(regressionDefaults(), paxCdiProviderAdapter(), cdiProviderBundles(), examineeOption(pExaminee),
				dependenciesOption(pDependencyComponent), mavenBundle("com.google.guava", "guava").versionAsInProject(),
				mavenBundle("org.apache.commons", "commons-lang3").versionAsInProject(),
				mavenBundle("org.objenesis", "objenesis").versionAsInProject(),
				mavenBundle("org.mockito", "mockito-core").versionAsInProject(),
				frameworkProperty("felix.bootdelegation.implicit").value("false"), junitBundles(),

				// Override regression default; we use the logback.xml located
				// in this jar
				systemProperty("logback.configurationFile").value(""));
	}

	/**
	 * @param pDependencyComponent
	 * @return
	 */
	private static Option dependenciesOption(final String[] pDependencyComponent) {
		final Option[] option = new Option[pDependencyComponent.length];
		for (int i = 0; i < option.length; i++) {
			option[i] = componentOption(pDependencyComponent[i]);
		}
		return composite(option);
	}

	/**
	 * @param pComponentName
	 * @return
	 */
	private static Option examineeOption(final String pComponentName) {
		final String[] coordinates = coordinates(pComponentName);
		return composite(componentOption(pComponentName),
				wrappedBundle(maven(coordinates[0], coordinates[1] + "-impl").classifier("tests"))
						.imports("org.hamcrest.*;resolution:=optional,org.ops4j.pax.exam.*;resolution:=optional,*"));
	}

	/**
	 * @param pComponentName
	 * @return
	 */
	private static Option componentOption(final String pComponentName) {
		final String[] coordinates = coordinates(pComponentName);
		return composite(mavenBundle(coordinates[0], coordinates[1] + "-api").versionAsInProject(),
				mavenBundle(coordinates[0], coordinates[1] + "-impl").versionAsInProject());
	}

	/**
	 * @param pComponentName
	 * @return
	 */
	private static String[] coordinates(final String pComponentName) {
		final String[] coordinates = new String[2];
		coordinates[0] = pComponentName.substring(0, pComponentName.lastIndexOf('.'));
		coordinates[1] = pComponentName.substring(coordinates[0].length() + 1);
		return coordinates;
	}
}
