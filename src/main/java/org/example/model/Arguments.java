package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Arguments {

    private final String sortField;

    private final String order;

    private final boolean statEnabled;

    private final String outputMode;

    private final String outputPath;
}
