package eventos.com.br.eventos.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Calendar;

public class CalendarSerializer implements JsonSerializer<Calendar> {

    @Override
    public JsonElement serialize(Calendar data, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(data.getTimeInMillis());
    }

}
