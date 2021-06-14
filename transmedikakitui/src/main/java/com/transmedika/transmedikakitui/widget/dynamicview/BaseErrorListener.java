package com.transmedika.transmedikakitui.widget.dynamicview;

public interface BaseErrorListener {
    void showError(String message);
    void validationError(String message);
    void hideError();
    void hideValidationError();
}
