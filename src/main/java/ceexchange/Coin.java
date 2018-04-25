package ceexchange;

public class Coin {

	private String name;
	private String id;
	private String symbol;
	
	public Coin() {
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	@Override
	public String toString(){
		return "Coin : name = "+ name +" and Symbol="+ symbol;
	}
	
}
