package com.server.server.resource;

import com.server.server.model.Responses;
import com.server.server.model.Server;
import com.server.server.service.ServerService;
import com.server.server.service.ServerServiceIm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.Map;

import static com.server.server.enumaration.Status.SERVER_UP;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {

    private final ServerServiceIm serverService;

    @GetMapping("/list")
    public ResponseEntity<Responses> getServer(){
        return ResponseEntity.ok(
                Responses.builder()
                        .timeStamp(now())
                        .data(Map.of("server", serverService.list(30)))
                        .message("get server")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Responses> pingServer(@PathVariable ("ipAddress") String ipAddress) throws IOException {
        Server pinging = serverService.ping(ipAddress);
        return ResponseEntity.ok(
                Responses.builder()
                        .timeStamp(now())
                        .data(Map.of("server", pinging))
                        .message(pinging.getStatus() == SERVER_UP ? "ping success!" : "pinging failed!")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @PostMapping("/save")
    public ResponseEntity<Responses> saveServer(@RequestBody @Valid Server server){
        return ResponseEntity.ok(
                Responses.builder()
                        .timeStamp(now())
                        .data(Map.of("server", serverService.saving(server)))
                        .message("server saved")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Responses> gettingServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Responses.builder()
                        .timeStamp(now())
                        .data(Map.of("server", serverService.get(id)))
                        .message("Retrieve server")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Responses> deleteServer(@PathVariable("id") Long id){
        boolean delete = serverService.delete(id);
        return ResponseEntity.ok(
                Responses.builder()
                        .timeStamp(now())
                        .data(Map.of("deleted", delete))
                        .message("server deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Responses> gettingServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Responses.builder()
                        .timeStamp(now())
                        .data(Map.of("server", serverService.get(id)))
                        .message("Retrieve server")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
   }
