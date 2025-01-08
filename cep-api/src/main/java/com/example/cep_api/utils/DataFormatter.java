package com.example.cep_api.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataFormatter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
            "dd/MM/yyyy HH:mm:ss", Locale.forLanguageTag("pt-BR")
    );

    public static String formatarDataParaExibicao(LocalDateTime data) {
        return data.format(FORMATTER);
    }
}
