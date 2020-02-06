
public class False extends Formula {
	public False() {
		super();
	}
	
	@Override
	public String toLaTeX() {
		return "\\mathrm{false}";
	}

	@Override
	public String toString() {
		return "false";
	}
}
