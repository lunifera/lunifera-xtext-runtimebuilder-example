package org.lunifera.xtext.runtimebuilder.example.jvmtypes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.knowhowlab.osgi.testing.assertions.OSGiAssert.setDefaultBundleContext;
import static org.knowhowlab.osgi.testing.utils.BundleUtils.findBundle;
import static org.knowhowlab.osgi.testing.utils.ServiceUtils.getService;

import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.TypesPackage;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.dsl.xtext.builder.participant.jvmtypes.IJvmTypeMetadataService;
import org.lunifera.xtext.builder.metadata.services.IMetadataBuilderService;
import org.lunifera.xtext.runtimebuilder.example.Activator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class JvmTypesBuilderParticipantTests {

	private static final String STRING_CLASS = "java.lang.String";
	private static final String BUNDLE_METADATA_SERVICE = "org.lunifera.xtext.builder.metadata.services";
	private static final int TIME_500 = 500;
	private static final int TIME_1000 = 1000;
	private static final int TIME_2000 = 2000;

	private IJvmTypeMetadataService service;
	private IMetadataBuilderService builderService;

	@Before
	public void setup() throws BundleException {

		setDefaultBundleContext(Activator.context);

		// restart the metadata service
		Bundle builderBundle = findBundle(Activator.context, BUNDLE_METADATA_SERVICE);
		builderBundle.start();
		getService(Activator.context, IMetadataBuilderService.class, TIME_2000);

		service = getService(Activator.context, IJvmTypeMetadataService.class,
				TIME_1000);
		builderService = getService(Activator.context,
				IMetadataBuilderService.class, TIME_1000);
		// add this test bundle to bundle space
		builderService.addToBundleSpace(Activator.context.getBundle());
		assertNotNull(builderService);
		assertNotNull(service);
	}

	@Test
	public void testLoadClassThenRemoveParticipant() {

		// loads the Class1ToLoad as a JvmType
		//
		JvmType testClass = service.getJvmType(Class1ToLoad.class);
		
		// then load class2
		JvmType testClass2 = service.getJvmType(Class2ToLoad.class);

		// load String
		JvmType testClass3 = service.getJvmType(String.class);

		
		// But it also works using the BuilderService directly
		JvmType testClass_directly = (JvmType) builderService.getMetadata(Class1ToLoad.class.getCanonicalName(), TypesPackage.Literals.JVM_TYPE);
	}
}
