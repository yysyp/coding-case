package com.poc.mpt.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Generic API Response Wrapper for standardized REST API responses.
 * Used to ensure consistent response structure across all endpoints in financial systems.
 *
 * @param <T> The type of data payload in the response
 */
@Schema(description = "Standardized API response wrapper")
public class GenericApiResponse<T> {

    @Schema(description = "Response status code", example = "200")
    private String code;

    @Schema(description = "Response message", example = "Success")
    private String message;

    @Schema(description = "Timestamp of response generation")
    private LocalDateTime timestamp;

    @Schema(description = "Business data payload")
    private T data;

    @Schema(description = "Request trace ID for observability and debugging")
    private String traceId;

    @Schema(description = "Indicates if the request was successful")
    private boolean success;

    // Private constructor to enforce builder pattern
    private GenericApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Creates a successful response with data
     *
     * @param data The business data
     * @param <T>  Type of data
     * @return GenericApiResponse instance
     */
    public static <T> GenericApiResponse<T> success(T data) {
        GenericApiResponse<T> response = new GenericApiResponse<>();
        response.code = "200";
        response.message = "Success";
        response.data = data;
        response.success = true;
        return response;
    }

    /**
     * Creates a successful response with custom message
     *
     * @param message Custom success message
     * @param <T>     Type of data
     * @return GenericApiResponse instance
     */
    public static <T> GenericApiResponse<T> success(String message) {
        GenericApiResponse<T> response = new GenericApiResponse<>();
        response.code = "200";
        response.message = message;
        response.success = true;
        return response;
    }

    /**
     * Creates a successful response with data and custom message
     *
     * @param data    The business data
     * @param message Custom success message
     * @param <T>     Type of data
     * @return GenericApiResponse instance
     */
    public static <T> GenericApiResponse<T> success(T data, String message) {
        GenericApiResponse<T> response = new GenericApiResponse<>();
        response.code = "200";
        response.message = message;
        response.data = data;
        response.success = true;
        return response;
    }

    /**
     * Creates an error response
     *
     * @param code    Error code
     * @param message Error message
     * @param <T>     Type of data
     * @return GenericApiResponse instance
     */
    public static <T> GenericApiResponse<T> error(String code, String message) {
        GenericApiResponse<T> response = new GenericApiResponse<>();
        response.code = code;
        response.message = message;
        response.success = false;
        return response;
    }

    /**
     * Creates an error response with default 500 code
     *
     * @param message Error message
     * @param <T>     Type of data
     * @return GenericApiResponse instance
     */
    public static <T> GenericApiResponse<T> error(String message) {
        return error("500", message);
    }

    // Getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
