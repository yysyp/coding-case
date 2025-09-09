package ps.demo.jpademo.controller;

import brave.Tracer;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.demo.jpademo.dto.BookDto;
import ps.demo.jpademo.dto.JasyptResponse;

import java.util.List;
import java.util.Scanner;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("/api/v1/jasypt")
@Tag(name = "Jasypt Encryption API", description = "API for text encryption and decryption using Jasypt")
public class JasyptController {

    @Value("${jasypt.encryptor.password}")
    private String jasyptEncrytorPass;

    @Value("${encpwdtest.test1}")
    private String testValue;

    @Autowired
    private Tracer tracer;

//    @io.swagger.v3.oas.annotations.Operation(
//            summary = "Encrypt text",
//            description = "Encrypt the given plain text",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "plain text",
//                    required = true,
//                    content = @io.swagger.v3.oas.annotations.media.Content(
//                            mediaType = "application/text",
//                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class)
//                    )
//            ),
//            responses = {
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
//                            responseCode = "200",
//                            description = "Encrypt successfully",
//                            content = @io.swagger.v3.oas.annotations.media.Content(
//                                    mediaType = "application/json",
//                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = JasyptResponse.class),
//                                    examples = {
//                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
//                                                    name = "Success",
//                                                    value = "{\"code\": \"200\", \"message\": \"success\", \"traceId\": \"...\", \"timestamp\": \"2020-01-01'T'10:11:12.345\"}"
//                                            )
//                                    }
//                            )
//                    )
//            }
//    )

    @Operation(
            summary = "Encrypt text",
            description = "Encrypts the provided text using configured Jasypt encryption algorithm",
            method = "GET"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Text encrypted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JasyptResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameter - text cannot be empty"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error - encryption failed"
            )
    })
    @GetMapping("/encrypt")
    ResponseEntity<JasyptResponse> encrypt(
            @Parameter(
                    name = "text",
                    description = "Plain text content to be encrypted",
                    required = true,
                    example = "1234",
                    schema = @Schema(type = "string", minLength = 1)
            )
            @RequestParam String text) {
        log.info("---testValue: {}", testValue);

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(jasyptEncrytorPass);
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setIvGenerator(new org.jasypt.iv.RandomIvGenerator());

        JasyptResponse jasyptResponse = new JasyptResponse();
        jasyptResponse.setData(encryptor.encrypt(text.trim()));
//        String tracerId  = tracer.currentSpan().context().traceIdString();
//        jasyptResponse.setTraceId(tracerId);
        jasyptResponse.initTracerId(tracer);
        return ResponseEntity.status(HttpStatus.OK).body(jasyptResponse);

    }


    @Operation(
            summary = "Decrypt encrypted text",
            description = "Decrypts Jasypt-encrypted text back to original plain text",
            operationId = "decryptText"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Text successfully decrypted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JasyptResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - encrypted text is invalid or malformed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during decryption process"
            )
    })
    @GetMapping("/decrypt")
    ResponseEntity<JasyptResponse> decrypt(
            @Parameter(
                    name = "text",
                    description = "Encrypted text to be decrypted (typically starts with ENC())",
                    required = true,
                    example = "ENC(Y4ERdCK9WpYN9TA3W+Jn5Oa9EOYEr+Z6PLbMKjv9R0Ub7rJOktCoeqjsCo/pDOKd)",
                    schema = @Schema(type = "string", minLength = 1)
            )
            @RequestParam String text) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(jasyptEncrytorPass);
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setIvGenerator(new org.jasypt.iv.RandomIvGenerator());
        text = text.trim();
        if (text.startsWith("ENC(")) {
            text = StringUtils.removeStart(text, "ENC(");
            text = StringUtils.removeEnd(text, ")");
        }

        JasyptResponse jasyptResponse = new JasyptResponse();
        jasyptResponse.setData(encryptor.decrypt(text));
        jasyptResponse.initTracerId(tracer);
        return ResponseEntity.status(HttpStatus.OK).body(jasyptResponse);
    }

}
