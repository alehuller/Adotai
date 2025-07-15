package br.com.alevhvm.adotai.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullException extends RuntimeException{
        private static final long serialVersionUID = 1L;

        public RequiredObjectIsNullException(String msg) {
                super(msg);
        }

        public RequiredObjectIsNullException() {
                super("It is not allowed to pass a null object");
        }

}

