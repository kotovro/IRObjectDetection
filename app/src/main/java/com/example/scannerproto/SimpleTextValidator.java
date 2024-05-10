package com.example.scannerproto;

public class SimpleTextValidator implements ITextValidator {
    @Override
    public boolean validate(String s) {
        return !s.isEmpty();
    };
}
