package com.example.userservice.feign;

import com.example.userservice.model.AlbumResponseModel;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumServiceClient {

    @GetMapping(path = "/users/{id}/albums")
    public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}
@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumServiceClient>{

    @Override
    public AlbumServiceClient create(Throwable cause) {
        return new AlbumServiceClientCallback(cause);
    }
}

class AlbumServiceClientCallback implements AlbumServiceClient{

    Logger logger= LoggerFactory.getLogger(this.getClass());

    private final Throwable cause;

    public AlbumServiceClientCallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public List<AlbumResponseModel> getAlbums(String id) {

        if (cause instanceof FeignException && ((FeignException) cause).status()==404){
            logger.error("404 error took place when getAlbums was called with userId: "
            + id + ". Error message: "
            + cause.getLocalizedMessage());
        }else {
            logger.error("Other error took place: " + cause.getLocalizedMessage());
        }

        return new ArrayList<>();
    }
}
