public class Token {
	private String name;
	private String kind;

	public Token(String name, String kind) {
		this.setName(name);
		this.setKind(kind);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getKind() {
		return kind;
	}

	@Override
	public String toString() {
		return getName() + "-" + getKind();
	}
}
