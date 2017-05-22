package de.monticore.lang.sd.cocos;

import de.monticore.lang.sd._ast.ASTOCLBlock;
import de.monticore.lang.sd._cocos.SDASTOCLBlockCoCo;

public class OCLContextDeclaredCoco implements SDASTOCLBlockCoCo {

	@Override
	public void check(ASTOCLBlock node) {
		if (node.contextIsPresent()) {
			String context = node.getContext().get();
			// TODO use symbol table
		}
	}

}
