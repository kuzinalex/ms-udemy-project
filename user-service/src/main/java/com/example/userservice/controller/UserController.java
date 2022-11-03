package com.example.userservice.controller;

import com.example.userservice.model.CreateUserRequestModel;
import com.example.userservice.model.CreateUserResponseModel;
import com.example.userservice.model.LoginRequestModel;
import com.example.userservice.service.UserService;
import com.example.userservice.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private Environment environment;

    @Autowired
    private UserService userService;

    @GetMapping("/status/check")
    public String status() {
        return "user service working on port" + environment.getProperty("local.server.port")+", with token="+environment.getProperty("token.secret");
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userRequestModel) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(userRequestModel, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);

        CreateUserResponseModel responseModel = modelMapper.map(createdUser, CreateUserResponseModel.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseModel);
    }

//    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<CreateUserResponseModel> loginUser( @RequestBody LoginRequestModel loginRequestModel) {
//
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//        UserDto userDto = modelMapper.map(userRequestModel, UserDto.class);
//
//        UserDto createdUser = userService.createUser(userDto);
//
//        CreateUserResponseModel responseModel = modelMapper.map(createdUser, CreateUserResponseModel.class);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(responseModel);
//    }
}
