package com.bits.checkin;

import java.io.IOException;
import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;

public class GeoCodeLookup extends BaseBasicBolt {

	private Geocoder geocoder;

	public void declareOutputFields(OutputFieldsDeclarer fieldsDeclarer) {
		fieldsDeclarer.declare(new Fields("time", "geocode"));
	}

	public void prepare(Map config, TopologyContext context) {
		geocoder = new Geocoder();
	}

	public void execute(Tuple tuple, BasicOutputCollector outputCollector) {
		String address = tuple.getStringByField("address");
		Long time = tuple.getLongByField("time");

		GeocoderRequest request = new GeocoderRequestBuilder().setAddress(address).setLanguage("en")
				.getGeocoderRequest();

		GeocodeResponse response;
		try {
			response = geocoder.geocode(request);
			GeocoderStatus status = response.getStatus();

			if (GeocoderStatus.OK.equals(status)) {
				GeocoderResult firstResult = response.getResults().get(0);
				LatLng latLng = firstResult.getGeometry().getLocation();
				System.out.println(latLng.getLat().toString());
				outputCollector.emit(new Values(time, latLng));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
