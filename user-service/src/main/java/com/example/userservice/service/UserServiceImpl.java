package com.example.userservice.service;

import com.example.userservice.entity.UserEntity;
import com.example.userservice.feign.AlbumServiceClient;
import com.example.userservice.model.AlbumResponseModel;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    private RestTemplate restTemplate;
    private Environment environment;
    private AlbumServiceClient albumServiceClient;

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,  RestTemplate restTemplate, Environment environment, AlbumServiceClient albumServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
        this.restTemplate=restTemplate;
        this.environment=environment;
        this.albumServiceClient=albumServiceClient;
    }


    @Override
    public UserDto createUser(UserDto user) {
        user.setUserId(UUID.randomUUID().toString());
        user.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));

        ModelMapper modelMapper=new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity=modelMapper.map(user,UserEntity.class);

        userRepository.save(userEntity);

        UserDto returnValue = modelMapper.map(userEntity,UserDto.class);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity=userRepository.findByEmail(username);

        if (userEntity==null){
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),true,true,true,true,new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity=userRepository.findByEmail(email);

        if (userEntity==null){
            throw new UsernameNotFoundException(email);
        }

        return new ModelMapper().map(userEntity,UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity=userRepository.findByUserId(userId);
        if (userEntity==null) throw new UsernameNotFoundException("User not found");

        UserDto userDto=new ModelMapper().map(userEntity,UserDto.class);

        // using REST template
//        String albumsUrl=String.format(environment.getProperty("albums.url"),userId);
//        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//        });
//        List<AlbumResponseModel> albumsList=albumsListResponse.getBody();

        // using Feign client
        logger.info("Before calling albums-ws");
        List<AlbumResponseModel> albumsList = albumServiceClient.getAlbums(userId);
        logger.info("After calling albums-ws");

        userDto.setAlbums(albumsList);

        return userDto;
    }

}
