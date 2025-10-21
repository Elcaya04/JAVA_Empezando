package org.example.utilities;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>,JsonDeserializer<LocalDateTime> {
    // Formato: 2025-11-24T14:30:00 (ISO-8601)
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Formato alternativo sin hora: 2025-11-24
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public JsonElement serialize(LocalDateTime dateTime, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateTime.format(FORMATTER));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String dateString = json.getAsString();

        try {
            // Intentar parsear con formato completo (con hora)
            return LocalDateTime.parse(dateString, FORMATTER);
        } catch (Exception e) {
            try {
                // Si falla, intentar con solo fecha y agregar hora por defecto (00:00:00)
                return LocalDateTime.parse(dateString + "T00:00:00", FORMATTER);
            } catch (Exception ex) {
                throw new JsonParseException("Unable to parse date: " + dateString, ex);
            }
        }
    }
}
