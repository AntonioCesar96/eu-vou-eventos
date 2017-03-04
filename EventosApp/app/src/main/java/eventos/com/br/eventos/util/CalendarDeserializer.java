package eventos.com.br.eventos.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Calendar;

public class CalendarDeserializer implements JsonDeserializer<Calendar> {

    @Override
    public Calendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        // Ele pega o TimeZone do celular GMT-0400
        Calendar calendar = Calendar.getInstance();
        Long integer = Long.parseLong(json.getAsString());
        calendar.setTimeInMillis(integer);

        return calendar;
    }
}
