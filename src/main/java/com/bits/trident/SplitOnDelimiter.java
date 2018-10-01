package com.bits.trident;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

public class SplitOnDelimiter extends BaseFunction {
	
	private final String delimiter;
	
	public SplitOnDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public void execute(TridentTuple tuple, TridentCollector collector) {
		for(String part: tuple.getString(0).split(delimiter))
			if(part.length()>0)
				collector.emit(new Values(part));
	}

}
