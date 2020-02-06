
public class True extends Formula {
	public True() {
		super();
	}

	@Override
	public String toLaTeX() {
		return "\\mathrm{true}";
	}

	@Override
	public String toString() {
		return "true";
	}
}
