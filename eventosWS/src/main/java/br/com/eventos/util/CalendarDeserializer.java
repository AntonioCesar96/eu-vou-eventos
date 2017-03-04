package br.com.eventos.util;

import java.lang.reflect.Type;
import java.util.Calendar;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class CalendarDeserializer implements JsonDeserializer<Calendar> {

	@Override
	public Calendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		Calendar calendar = Calendar.getInstance();
		Long integer = Long.parseLong(json.getAsString());
		calendar.setTimeInMillis(integer);
		return calendar;
	}

}
