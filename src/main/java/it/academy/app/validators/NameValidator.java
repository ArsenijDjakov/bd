package it.academy.app.validators;

//Room for improvement: Regex could be used for validation
public class NameValidator extends Validator<String> {
    @Override
    public void validate(String attribute) {

        attribute = attribute.trim();

        checkEveryNameInString(attribute);
    }

    private void checkEveryNameInString(String attribute) {
        String[] words = attribute.split(" ");
        for (String word : words) {
            doesStringContainLetters(word);
        }
    }
}
