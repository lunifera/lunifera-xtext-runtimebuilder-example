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
import org.lunifera.dsl.semantic.dto.LDto;
import org.lunifera.dsl.semantic.dto.LunDtoPackage;
import org.lunifera.dsl.xtext.builder.participant.IDtoMetadataService;
import org.lunifera.xtext.builder.metadata.services.IMetadataBuilderService;
import org.lunifera.xtext.runtimebuilder.example.Activator;
import org.osgi.framework.BundleException;

public class DtoDSLBuilderParticipantTests {

	private static final String DTO_FQN = "org.lunifera.dsl.xtext.builder.participant.tests.dtos.MyEntityDto";
	private static final String DTO2_FQN = "org.lunifera.dsl.xtext.builder.participant.tests.other.dtos.OtherDto";
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
	public void testAccessDto() throws Exception {

		IDtoMetadataService service = getService(Activator.context,
				IDtoMetadataService.class, TIME_1000);
		assertNotNull(service);

		LDto dto = service.getMetadata(DTO_FQN);
		assertEquals("MyEntityDto", dto.getName());

		LDto dto2 = service.getMetadata(DTO2_FQN);
		assertEquals("OtherDto", dto2.getName());

		// we can also use the builderService
		LDto dto_directly = (LDto) builderService.getMetadata(DTO_FQN,
				LunDtoPackage.Literals.LDTO);

		// And the cool thing.
		assertSame(dto, dto_directly);

	}

}
