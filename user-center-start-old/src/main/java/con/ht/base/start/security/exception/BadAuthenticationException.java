package con.ht.base.start.security.exception;

import com.ht.base.common.ErrorResult;
import org.springframework.security.core.AuthenticationException;

/**
 * @author zhengyi
 * @date 11/23/18 2:47 PM
 **/
public class BadAuthenticationException extends AuthenticationException {

    private ErrorResult errorResult;

    public BadAuthenticationException(ErrorResult errorResult) {
        super(errorResult.getMessage());
        this.errorResult = errorResult;
    }

    public ErrorResult getErrorResult() {
        return errorResult;
    }
}