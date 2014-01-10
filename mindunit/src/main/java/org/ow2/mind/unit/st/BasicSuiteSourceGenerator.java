/**
 * Copyright (C) 2013 Schneider-Electric
 *
 * This file is part of "Mind Compiler" is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact: mind@ow2.org
 *
 * Authors: Stephane Seyvoz
 * Contributors: 
 */

package org.ow2.mind.unit.st;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.objectweb.fractal.adl.CompilerError;
import org.objectweb.fractal.adl.Definition;
import org.ow2.mind.SourceFileWriter;
import org.ow2.mind.adl.AbstractSourceGenerator;
import org.ow2.mind.io.IOErrors;
import org.ow2.mind.io.OutputFileLocator;
import org.ow2.mind.unit.model.Suite;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class BasicSuiteSourceGenerator extends AbstractSourceGenerator implements SuiteSourceGenerator {

	@Inject
	protected OutputFileLocator 	outputFileLocatorItf;

	/** The name to be used to inject the templateGroupName used by this class. */
	public static final String    	TEMPLATE_NAME 	= "st.suite";

	/** The default templateGroupName used by this class. */
	public static final String		DEFAULT_TEMPLATE 	= "st.suite.Suite";

	protected final static String 	FILE_EXT 		= ".c";

	@Inject
	public BasicSuiteSourceGenerator(@Named(TEMPLATE_NAME) final String templateGroupName) {
		super(templateGroupName);
	}

	// ---------------------------------------------------------------------------
	// public static methods
	// ---------------------------------------------------------------------------

	/**
	 * A static method that returns the name of the file that is generated by this
	 * component for the given {@link Definition};
	 * 
	 * Base folder is the output folder.
	 * 
	 * @param definition a {@link Definition} node.
	 * @return the name of the file that is generated by this component for the
	 *         given {@link Definition};
	 */
	public static String getSuiteFileName() {
		//return "/org/ow2/mind/unit/mindUnitSuite.c";
		return "/mindUnitSuite.c";
	}

	public void visit(List<Suite> suites, Map<Object, Object> context) {

		final File outputFile = outputFileLocatorItf.getCSourceOutputFile(
				"/unit-gen" + getSuiteFileName(), context);

		final StringTemplate st = getInstanceOf("Suite");
		
		st.setAttribute("suiteDefinitions", suites);

		try {
			SourceFileWriter.writeToFile(outputFile, st.toString());
		} catch (final IOException e) {
			throw new CompilerError(IOErrors.WRITE_ERROR, e,
					outputFile.getAbsolutePath());
		}
	}
}