package com.zinkwork.Atm.controller;

import com.zinkwork.Atm.model.Account;
import com.zinkwork.Atm.service.AtmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.util.NoSuchElementException;

@Tag(name="atm-rest-controller")
@RestController
@RequestMapping("/atm")
@Validated
@RequiredArgsConstructor
public class AtmController {
    private final AtmService atmservice;

    @Operation(tags="atm-rest-controller",summary = "getBalance",description = "rest call to return the balance")
    @ApiResponses(value = @ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(mediaType = APPLICATION_JSON_VALUE)))

    @GetMapping(path = "/balance/{id}",produces = APPLICATION_JSON_VALUE)
    public Account getAccount(@PathVariable Integer accountId){
        try {
            return atmservice.getAccount(accountId);
        }catch (NoSuchElementException ex){
            throw new ResponseStatusException(NOT_FOUND,"Existing account id not provided",ex);
        }

    }

}
