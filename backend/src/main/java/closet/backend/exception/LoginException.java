package closet.backend.exception;

import lombok.Getter;

@Getter
public class LoginException extends RuntimeException{
    public LoginException() {
    }

    public LoginException(String message) {
        super(message);
    }
}
