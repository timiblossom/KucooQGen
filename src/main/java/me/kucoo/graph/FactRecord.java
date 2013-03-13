package me.kucoo.graph;

public class FactRecord {
	public String mid;
	public String name;
	public String property;
	
	public FactRecord() {}
	
	public FactRecord(String mid, String name) {
		this.mid = mid;
		this.name = name;
	}
	
	@Override
    public int hashCode() {
		if (mid == null || name == null) 
			return 0;
		return (mid.hashCode() + name.hashCode())/3;
	}
	
	@Override
    public boolean equals(Object arg0) {
		FactRecord that = (FactRecord) arg0;
		
		if (that == null) 
			return false;
		
		if (this == that)
			return true;
		
		if (mid == that.mid && name == that.name)
			return true;
		
		if ((mid != null && that.mid == null) || (mid == null && that.mid != null))
			return false;
		
		if ((name != null && that.name == null) || (name == null && that.name != null))
			return false;
		
		if (mid.equals(that.name) && name.equals(that.name)) 
			return true;
		
		return false;
		
	}
	
	public static void main(String[] args) {
		FactRecord r1 = new FactRecord("111", "abc");
		FactRecord r2 = new FactRecord("111", "abc");
		
		System.out.println(r1.equals(r2));
	}
}