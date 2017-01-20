package com.ypeksen.web.spring.service.exception;

public class ServiceException extends RuntimeException {
private static final long serialVersionUID = 7972496903360408970L;
private final int errorCode;

public ServiceException(int errorCode) {
this.errorCode = errorCode;
}

public ServiceException(int errorCode, String message) {
super(message);
this.errorCode = errorCode;
}

public int getErrorId(){
return errorCode;
}
public String getErrorCode() {
return "service." + errorCode;
}

@Override
public String toString() {
return "ServiceException errorCode:" + errorCode;
}
}
