package com.ecommerce.exception;

public class ResourceNotFoundException extends Throwable {
     String resourceName;
     String fieldName;
     Long fieldId;


    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldId) {
        super(String.format("%s Not Found For %s %d" , resourceName, fieldName, fieldId));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldId = fieldId;
    }
}
