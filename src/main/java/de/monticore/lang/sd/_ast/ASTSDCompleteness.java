package de.monticore.lang.sd._ast;

public class ASTSDCompleteness extends ASTSDCompletenessTOP {

	public ASTSDCompleteness() {
		super();
	}

	public ASTSDCompleteness(String completeness) {
		super(completeness);
	}

	public boolean isFree() {
		if (!completeness.isPresent()) {
			return true;
		}
		String comp = completeness.get();
		return comp.equals("<<match:free>>") | comp.equals("...");
	}

	public boolean isComplete() {
		if (!completeness.isPresent()) {
			return false;
		}
		String comp = completeness.get();
		return comp.equals("<<match:complete>>") | comp.equals("(C)") | comp.equals("(c)");
	}

	public boolean isInitial() {
		if (!completeness.isPresent()) {
			return false;
		}
		String comp = completeness.get();
		return comp.equals("<<match:initial>>");
	}

	public boolean isVisible() {
		if (!completeness.isPresent()) {
			return false;
		}
		String comp = completeness.get();
		return comp.equals("<<match:visible>>");
	}

}