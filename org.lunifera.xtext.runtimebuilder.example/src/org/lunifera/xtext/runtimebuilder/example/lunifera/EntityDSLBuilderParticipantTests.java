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
import org.lunifera.dsl.semantic.entity.LEntity;
import org.lunifera.dsl.semantic.entity.LunEntityPackage;
import org.lunifera.dsl.xtext.builder.participant.IEntityMetadataService;
import org.lunifera.xtext.builder.metadata.services.IMetadataBuilderService;
import org.lunifera.xtext.runtimebuilder.example.Activator;
import org.osgi.framework.BundleException;

public class EntityDSLBuilderParticipantTests {

	private static final String ENTITY_FQN = "org.lunifera.dsl.xtext.builder.participant.tests.other.OtherEntity";
	private static final String ENTITY2_FQN = "org.lunifera.dsl.xtext.builder.participant.tests.MyEntity";
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
	public void testAccessEntity() throws Exception {

		IEntityMetadataService service = getService(Activator.context,
				IEntityMetadataService.class, TIME_1000);
		assertNotNull(service);

		LEntity entity = service.getMetadata(ENTITY_FQN);
		assertEquals("OtherEntity", entity.getName());

		LEntity entity2 = service.getMetadata(ENTITY2_FQN);
		assertEquals("MyEntity", entity2.getName());

		// we can also use the builderService
		LEntity entity_directly = (LEntity) builderService.getMetadata(
				ENTITY_FQN, LunEntityPackage.Literals.LENTITY);

		// And the cool thing.
		assertSame(entity, entity_directly);

	}

}
