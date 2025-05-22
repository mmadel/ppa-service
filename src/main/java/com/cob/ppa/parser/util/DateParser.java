package com.cob.ppa.parser.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

public class DateParser {
    public static LocalDate parse(String raw) {
        if (raw == null || raw.isBlank()) return null;

        List<DateTimeFormatter> formatters = List.of(
                new DateTimeFormatterBuilder()
                        .appendPattern("M/d/")
                        .appendValueReduced(ChronoField.YEAR, 2, 4, 1950) // 00–49 → 2000–2049, 50–99 → 1950–1999
                        .toFormatter(Locale.US),
                DateTimeFormatter.ofPattern("M/d/yyyy", Locale.US),
                DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.US),
                DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.US)
        );

        for (DateTimeFormatter fmt : formatters) {
            try {
                return LocalDate.parse(raw, fmt);

            } catch (DateTimeParseException ignored) {}
        }

        System.err.println("Failed to parse : " + raw);
        return null;
    }
}
