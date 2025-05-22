package com.cob.ppa.service.batch.tokenizer;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.DefaultFieldSet;

import java.util.regex.Pattern;

import static org.springframework.cglib.core.ReflectUtils.getNames;

public class CustomDelimitedLineTokenizer extends DelimitedLineTokenizer {
    private static final Pattern COMMA_PATTERN = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    private String[] names;  // Store field names
    // Explicit setter for names
    public void setNames(String[] names) {
        this.names = names;
        super.setNames(names);  // Also set in parent class
    }

    @Override
    public FieldSet tokenize(String line) {
        String[] tokens = COMMA_PATTERN.split(line);

        // Clean each token
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim().replaceAll("^\"|\"$", "");
        }

        return new DefaultFieldSet(tokens, this.names);  // Use the stored names
    }

}
