/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.ast;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Lists;

import de.monticore.lang.sd._ast.ASTSDImportStatement;
import de.monticore.lang.sd._ast.SDNodeFactory;
import de.monticore.types.types._ast.ASTQualifiedName;

public class ImportStatementFilenameTest {

	@Test
	public void test() {
		// Build import statement
		ASTQualifiedName qn = SDNodeFactory.createASTQualifiedName();
		qn.setParts(Lists.newArrayList("a", "b", "c", "test", "exe"));
		ASTSDImportStatement ast = SDNodeFactory.createASTSDImportStatement();
		ast.setQualifiedName(qn);

		// Check for correct name resolution
		assertEquals("test.exe", ast.getFileName());
		assertEquals("a/b/c/", ast.getPath());
	}

}
