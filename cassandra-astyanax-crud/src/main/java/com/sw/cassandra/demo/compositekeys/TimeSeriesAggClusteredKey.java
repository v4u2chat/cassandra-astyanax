package com.sw.cassandra.demo.compositekeys;

import com.netflix.astyanax.annotations.Component;

public class TimeSeriesAggClusteredKey {

	@Component(ordinal = 0)
	public Integer period_id;

	public TimeSeriesAggClusteredKey() {

	}

	@Override
	public String toString() {
		return period_id + "";
	}

}
