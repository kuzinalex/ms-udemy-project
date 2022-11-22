package com.example.userservice.feign;

import com.example.userservice.model.AlbumResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "albums-ws")
public interface AlbumServiceClient {

    @GetMapping(path = "/users/{id}/albumss")
    public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}
