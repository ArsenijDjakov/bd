package it.academy.app.validators;

public enum ErrorMessages {

    invalidUsernameFormat {
        @Override
        public String toString() {
            return "Invalid username format!";
        }
    },
    illegalCharactersUsed {
        @Override
        public String toString() {
            return "Field is empty";
        }
    },
    invalidPasswordFormat {
        @Override
        public String toString() {
            return "Invalid password format!";
        }
    },
    invalidEmailFormat {
        @Override
        public String toString() {
            return "Email format is invalid";
        }
    },
    noAdminRights {
        @Override
        public String toString() {
            return "User does not have rights to perform request!";
        }
    },
    invalidTelNumberFormat {
        @Override
        public String toString() {
            return "Telephone number format is invalid";
        }
    },
    usernameAlreadyExist {
        @Override
        public String toString() {
            return "User with selected username already exist!";
        }
    },
    emailAlreadyExist {
        @Override
        public String toString() {
            return "User with selected email already exist!";
        }
    },
    alreadyInFavorites {
        @Override
        public String toString() {
            return "Product already added to favorites!";
        }
    },
    invalidStatus {
        @Override
        public String toString() {
            return "Only 'IN PROGRESS' status can be changed";
        }
    };
}
