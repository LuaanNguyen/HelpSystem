//Old method for checking password requirements
//    // Method to check if passwords match and update the UI
//    private void checkPasswordsMatch(PasswordField passwordField, PasswordField confirmPasswordField, Label matchingErrorMessageLabel) {
//        if (!confirmPasswordField.getText().equals(passwordField.getText())) {
//            matchingErrorMessageLabel.setText("Passwords do not match");
//        } else {
//            matchingErrorMessageLabel.setText("");
//        }
//    }
//    private void checkPasswordsUpper(PasswordField passwordField, Label upperErrorMessageLabel) {
//        if (!passwordField.getText().matches(".*[A-Z].*")) {
//            upperErrorMessageLabel.setText("Password must contain at least 1 Upper Case letter");
//        } else {
//            upperErrorMessageLabel.setText("");
//        }
//    }
//    private void checkPasswordsLower(PasswordField passwordField, Label lowerErrorMessageLabel) {
//        if (!passwordField.getText().matches(".*[a-z].*")) {
//            lowerErrorMessageLabel.setText("Password must contain at least 1 Lower Case letter");
//        } else {
//            lowerErrorMessageLabel.setText("");
//        }
//    }
//    private void checkPasswordsSpecial(PasswordField passwordField, Label specialErrorMessageLabel) {
//        if (!passwordField.getText().matches(".*[!@#$%^&*].*")) {
//            specialErrorMessageLabel.setText("Password must contain at least 1 Special Character");
//        } else {
//            specialErrorMessageLabel.setText("");
//        }
//    }
