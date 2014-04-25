package com.sw.cassandra.demo.dao;

import java.util.Iterator;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.CqlResult;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.sw.cassandra.demo.compositekeys.TimeSeriesAggClusteredKey;
import com.sw.cassandra.demo.compositekeys.TimeSeriesAggRowKey;
import com.sw.cassandra.demo.init.ContextBuilder;

public class TimeSeriesAggDAO {
	private static Keyspace keyspace;
	private static ColumnFamily<TimeSeriesAggRowKey,TimeSeriesAggClusteredKey> STEEL_CF;
	private static final String STEEL_CF_NAME = "sw_worksheet_timeseries_agg";
	
	static AnnotatedCompositeSerializer<TimeSeriesAggRowKey> keySerializer = new AnnotatedCompositeSerializer<TimeSeriesAggRowKey>(TimeSeriesAggRowKey.class);
	static AnnotatedCompositeSerializer<TimeSeriesAggClusteredKey> columnSerializer = new AnnotatedCompositeSerializer<TimeSeriesAggClusteredKey>(TimeSeriesAggClusteredKey.class);


	static {
		keyspace = ContextBuilder.getKeySpace();

		
		STEEL_CF = ColumnFamily.newColumnFamily(STEEL_CF_NAME, keySerializer,columnSerializer);
	}

	public static void readAll() {
		Rows<TimeSeriesAggRowKey,TimeSeriesAggClusteredKey> result;
		try {
			result = keyspace.prepareQuery(STEEL_CF).getAllRows().execute().getResult();

			Iterator<Row<TimeSeriesAggRowKey,TimeSeriesAggClusteredKey>> rowIterator = result.iterator();

			while (rowIterator.hasNext()) {
				Row<TimeSeriesAggRowKey,TimeSeriesAggClusteredKey> row = rowIterator.next();
				System.out.println("Key            "+row.getKey());
				
				ColumnList<TimeSeriesAggClusteredKey> columnList = row.getColumns();
				Iterator<Column<TimeSeriesAggClusteredKey>> columnIterator = columnList.iterator();
				while (columnIterator.hasNext()) {
					try {
						Column<TimeSeriesAggClusteredKey> column = columnIterator.next();
						System.out.println(column.getName()+"--------"+column.getDoubleValue());
					} catch (Exception e) {
						//System.out.println("EMPTY VALUE RECORD ENCOUNTERED...");
					}
				}
			}
		} catch (ConnectionException e) {
			e.printStackTrace();
		}

	}
	
	public static void readSingleRowKey(TimeSeriesAggRowKey rowKey) throws ConnectionException {
		try {
			ColumnList<TimeSeriesAggClusteredKey> columns = keyspace.prepareQuery(STEEL_CF).getKey(rowKey).execute().getResult();
			
			Iterator<Column<TimeSeriesAggClusteredKey>> columnIterator = columns.iterator();
			while (columnIterator.hasNext()) {
				try {
					Column<TimeSeriesAggClusteredKey> column = columnIterator.next();
					System.out.println(column.getName()+"--------"+column.getDoubleValue());
				} catch (Exception e) {
					//System.out.println("EMPTY VALUE RECORD ENCOUNTERED...");
				}
			}

		} catch (ConnectionException e) {
			e.printStackTrace();
		}

	}
	
	public static void readSingleRowKeyWithCQL(TimeSeriesAggRowKey rowKey)
	{
		try {
			
			String cqlString = "SELECT period_id,ts_metric FROM "+STEEL_CF_NAME+" WHERE worksheet_pk='"+rowKey.worksheet_pk+"' and parent_name='"+rowKey.parent_name+"' and tsname='"+rowKey.tsname+"'";
			System.out.println(cqlString);
			
			CqlResult<TimeSeriesAggRowKey,TimeSeriesAggClusteredKey> result = keyspace.prepareQuery(STEEL_CF)
																						.withCql(cqlString)
																						//.withColumnRange(new RangeBuilder().setStart("firstColumn").setEnd("lastColumn").setMaxSize(100).build())
																						.execute().getResult();
			
			System.out.println("Total no of records found : "+result.getRows().size());

			for(Row<TimeSeriesAggRowKey,TimeSeriesAggClusteredKey> row: result.getRows())
			{
				System.out.println(row);
				
			}
			
			
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) throws ConnectionException {
		//readAll();
		
		TimeSeriesAggRowKey rowKey = new TimeSeriesAggRowKey();
		rowKey.worksheet_pk = "9d9062670a0400e211b41e95afa24b2b";
		rowKey.parent_name = "PARENT00020";
		rowKey.tsname = "Commercial Demand Plan (Rev)";
		
		//readSingleRowKey(rowKey);
		
		readSingleRowKeyWithCQL(rowKey);
	}

}
