/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.xtext.runtimebuilder.example.lunifera;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.knowhowlab.osgi.testing.assertions.OSGiAssert.setDefaultBundleContext;
import static org.knowhowlab.osgi.testing.utils.ServiceUtils.getService;

import org.junit.Before;
import org.junit.Test;
import org.knowhowlab.osgi.testing.utils.BundleUtils;
import org.lunifera.dsl.semantic.common.types.LDataType;
import org.lunifera.dsl.semantic.common.types.LunTypesPackage;
import org.lunifera.dsl.xtext.builder.participant.IDatatypesMetadataService;
import org.lunifera.xtext.builder.metadata.services.IMetadataBuilderService;
import org.lunifera.xtext.runtimebuilder.example.Activator;
import org.osgi.framework.BundleException;

public class DatatypesDSLBuilderParticipantTests {

	private static final String DATATYPE_FQN = "org.lunifera.dsl.xtext.builder.participant.tests.datatypes.forTest";
	private static final String DATATYPE2_FQN = "org.lunifera.dsl.xtext.builder.participant.tests.datatypes.dateTest";
	private static final String LUNIFERA_LONG_FQN = "org.lunifera.dsl.common.datatypes.Long";
	private static final int TIME_15000 = 15000;
	private static final int TIME_1000 = 1000;

	private IMetadataBuilderService builderService;

	@Before
	public void setup() throws BundleException {
		setDefaultBundleContext(Activator.context);

		BundleUtils.startBundleAsync(Activator.context,
				"org.lunifera.xtext.builder.metadata.services");
		builderService = getService(Activator.context,
				IMetadataBuilderService.class, TIME_15000);
		assertNotNull(builderService);
	}

	@Test
	public void testAccessDatatype() throws Exception {

		// use the IDatatypesMetadataService
		IDatatypesMetadataService service = getService(Activator.context,
				IDatatypesMetadataService.class, TIME_1000);
		assertNotNull(service);

		LDataType datatype = service.getMetadata(DATATYPE_FQN);
		assertEquals("forTest", datatype.getName());

		LDataType datatype2 = service.getMetadata(DATATYPE2_FQN);
		assertEquals("dateTest", datatype2.getName());
		
		LDataType longDatatype = service.getMetadata(LUNIFERA_LONG_FQN);
		assertEquals("Long", longDatatype.getName());
		
		// use the IMetadataBuilderService directly
		LDataType datatype_directly = (LDataType) builderService.getMetadata(
				DATATYPE_FQN, LunTypesPackage.Literals.LDATA_TYPE);

		// And the cool thing.
		assertSame(datatype, datatype_directly);

	}

}
