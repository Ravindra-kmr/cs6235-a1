package cs6235.a1;

public class Query {
	private String lhs;
	private String rhs;
	public String getLhs() {
		return lhs;
	}
	public String getRhs() {
		return rhs;
	}
	
	public Query(String lhs, String rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(lhs).append(" alias ").append(rhs).append(" ?");
		return sb.toString();
	}
}
