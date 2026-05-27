package view;

import javafx.scene.control.TextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class MaskUtils {

    public static void applyCpfMask(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            boolean isUpdating = false;

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (isUpdating) return;
                isUpdating = true;

                String clean = newValue == null ? "" : newValue.replaceAll("[^\\d]", "");
                if (clean.length() > 11) clean = clean.substring(0, 11);

                String formatted = formatCpf(clean);
                textField.setText(formatted);
                textField.positionCaret(formatted.length());

                isUpdating = false;
            }
        });
    }

    private static String formatCpf(String text) {
        StringBuilder sb = new StringBuilder(text);
        if (text.length() > 3) sb.insert(3, ".");
        if (text.length() > 6) sb.insert(7, ".");
        if (text.length() > 9) sb.insert(11, "-");
        return sb.toString();
    }

    public static void applyCnpjMask(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            boolean isUpdating = false;

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (isUpdating) return;
                isUpdating = true;

                String clean = newValue == null ? "" : newValue.replaceAll("[^\\d]", "");
                if (clean.length() > 14) clean = clean.substring(0, 14);

                String formatted = formatCnpj(clean);
                textField.setText(formatted);
                textField.positionCaret(formatted.length());

                isUpdating = false;
            }
        });
    }

    private static String formatCnpj(String text) {
        StringBuilder sb = new StringBuilder(text);
        if (text.length() > 2) sb.insert(2, ".");
        if (text.length() > 5) sb.insert(6, ".");
        if (text.length() > 8) sb.insert(10, "/");
        if (text.length() > 12) sb.insert(15, "-");
        return sb.toString();
    }

    public static void applyTelefoneMask(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            boolean isUpdating = false;

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (isUpdating) return;
                isUpdating = true;

                String clean = newValue == null ? "" : newValue.replaceAll("[^\\d]", "");
                if (clean.length() > 11) clean = clean.substring(0, 11);

                String formatted = formatTelefone(clean);
                textField.setText(formatted);
                textField.positionCaret(formatted.length());

                isUpdating = false;
            }
        });
    }

    private static String formatTelefone(String text) {
        StringBuilder sb = new StringBuilder(text);
        if (text.length() > 0) sb.insert(0, "(");
        if (text.length() > 2) sb.insert(3, ") ");
        if (text.length() > 6 && text.length() <= 10) {
            sb.insert(8, "-");
        } else if (text.length() > 10) {
            sb.insert(9, "-");
        }
        return sb.toString();
    }

    public static void applyCepMask(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            boolean isUpdating = false;

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (isUpdating) return;
                isUpdating = true;

                String clean = newValue == null ? "" : newValue.replaceAll("[^\\d]", "");
                if (clean.length() > 8) clean = clean.substring(0, 8);

                String formatted = formatCep(clean);
                textField.setText(formatted);
                textField.positionCaret(formatted.length());

                isUpdating = false;
            }
        });
    }

    private static String formatCep(String text) {
        StringBuilder sb = new StringBuilder(text);
        if (text.length() > 5) sb.insert(5, "-");
        return sb.toString();
    }
}
