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
package org.lunifera.xtext.runtimebuilder.example.xbase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.knowhowlab.osgi.testing.assertions.OSGiAssert.setDefaultBundleContext;
import static org.knowhowlab.osgi.testing.utils.BundleUtils.findBundle;
import static org.knowhowlab.osgi.testing.utils.ServiceUtils.getService;

import org.eclipse.xtext.Grammar;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.dsl.xtext.builder.participant.xbase.IXbaseMetadataService;
import org.lunifera.xtext.builder.metadata.services.IMetadataBuilderService;
import org.lunifera.xtext.runtimebuilder.example.Activator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class XbaseBuilderParticipantTests {

	private static final String XTEXT_GRAMMAR = "org.eclipse.xtext.Xtext";
	private static final String OTHER_GRAMMAR = "xbase.tests.Grammar";

	private static final int TIME_1000 = 1000;
	private static final int TIME_2000 = 2000;
	private Bundle builderBundle;
	private Bundle participantBundle;

	@Before
	public void setup() throws BundleException {

		setDefaultBundleContext(Activator.context);

		participantBundle = findBundle(Activator.context,
				"org.lunifera.xtext.builder.participant.xbase");
		participantBundle.start();

		// restart the metadata service
		builderBundle = findBundle(Activator.context,
				"org.lunifera.xtext.builder.metadata.services");
		builderBundle.stop();
		builderBundle.start();
		getService(Activator.context, IMetadataBuilderService.class, TIME_2000);
	}

	@Test
	public void testCache() throws Exception {
		IXbaseMetadataService service = getService(Activator.context,
				IXbaseMetadataService.class, TIME_1000);
		IMetadataBuilderService builderService = getService(Activator.context,
				IMetadataBuilderService.class, TIME_1000);
		assertNotNull(service);
		assertNotNull(builderService);

		Grammar grammar = service.getGrammar(XTEXT_GRAMMAR);
		assertEquals(XTEXT_GRAMMAR, grammar.getName());

		Grammar grammar2 = service.getGrammar(OTHER_GRAMMAR);
		assertEquals(OTHER_GRAMMAR, grammar2.getName());

	}

}
