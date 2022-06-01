package com.zinkwork.Atm.controller;

import com.zinkwork.Atm.exception.AuthenticationException;
import com.zinkwork.Atm.service.AtmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "atm-rest-controller")
@RestController
@RequestMapping("/atm")
@Validated
@RequiredArgsConstructor
public class AtmController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AtmService atmservice;

    @Operation(tags = "atm-rest-controller", summary = "getBalance", description = "rest call to return the balance")
    @ApiResponses(value = @ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(mediaType = APPLICATION_JSON_VALUE)))

    @GetMapping(path = "/balance/{id}/{token}", produces = APPLICATION_JSON_VALUE)
    public String getAccount(@PathVariable String id, @PathVariable String token) {
        return "Account: " + id +
                "\nBalance: £" +(atmservice.getAccount(id, token)).getBalance();
    }

    @Operation(tags = "atm-rest-controller", summary = "withdraw", description = "rest call to withdraw funds")
    @ApiResponses(value = @ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(mediaType = APPLICATION_JSON_VALUE)))

    @GetMapping(path = "/withdraw/{id}/{token}/{amount}", produces = APPLICATION_JSON_VALUE)
    public String getFunds(@PathVariable String id, @PathVariable String token, @PathVariable Integer amount) {
        if (atmservice.validatePin(id, token)) {
            return atmservice.withdrawFunds(id, token, amount);
        } else {
            throw new AuthenticationException("test");
        }
    }

    @Operation(tags = "atm-rest-controller", summary = "overdraft", description = "rest call to get overdraft amount")
    @ApiResponses(value = @ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(mediaType = APPLICATION_JSON_VALUE)))

    @GetMapping(path = "/overdraft/{id}/{token}", produces = APPLICATION_JSON_VALUE)
    public String getOverdraft(@PathVariable String id, @PathVariable String token) {
        String overdraft = ((atmservice.getAccount(id, token)).getOverdraftLimit().toString()).substring(1);

        if(overdraft.length() == 0){
            overdraft = "0";
        }

        return "Overdraft: £" +  overdraft ;
    }

}
