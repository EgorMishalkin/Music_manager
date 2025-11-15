class BlankFieldError extends Exception {
    public BlankFieldError(String fieldName) {
        super("Ошибка: поле \"" + fieldName + "\" не заполнено или заполнено неверно!");
    }
}


public class Validator extends Exception{

    public static void checkNotEmpty(String input, String fieldName) throws BlankFieldError {
        if (input == null || input.trim().isEmpty()) {
            throw new BlankFieldError(fieldName);
        }
    }

    public static void checkIsNumber(String input, String fieldName) throws BlankFieldError {
        if (input != null || !input.trim().isEmpty()) {
            checkNotEmpty(input, fieldName);

            try {
                Integer.parseInt(input);
            } catch (Exception e) {
                throw new BlankFieldError(fieldName + " должно быть числом");
            }


        }
    }
}


