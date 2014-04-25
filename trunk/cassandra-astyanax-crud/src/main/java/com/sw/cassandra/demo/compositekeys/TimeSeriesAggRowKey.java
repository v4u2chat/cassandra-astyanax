package com.sw.cassandra.demo.compositekeys;

import com.netflix.astyanax.annotations.Component;

public class TimeSeriesAggRowKey {
	@Component(ordinal = 0)
    public String worksheet_pk;
    @Component(ordinal = 1)
    public String parent_name;
    @Component(ordinal = 2)
    public String tsname;
    
    public TimeSeriesAggRowKey(){
    	
    }

	@Override
	public String toString() {
		
		return worksheet_pk+":"+ parent_name+":"+ tsname;
	}

	public String getWorksheet_pk() {
		return worksheet_pk;
	}

	public void setWorksheet_pk(String worksheet_pk) {
		this.worksheet_pk = worksheet_pk;
	}

	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

	public String getTsname() {
		return tsname;
	}

	public void setTsname(String tsname) {
		this.tsname = tsname;
	}
    
    
}
