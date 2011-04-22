package com.nmt.nmj.editor.exception;

public class NmjEditorException extends Exception {

    private static final long serialVersionUID = 1L;

    public NmjEditorException() {
        super();
    }

    public NmjEditorException(String message) {
        super(message);
    }

    public NmjEditorException(String message, Throwable t) {
        super(message, t);
    }

    public NmjEditorException(Throwable t) {
        super(t);
    }

}
