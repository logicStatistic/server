package com.server.server.service;

import com.server.server.model.Server;
import com.server.server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.Random;


import static com.server.server.enumaration.Status.SERVER_DOWN;
import static com.server.server.enumaration.Status.SERVER_UP;
import static java.lang.Boolean.TRUE;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ServerServiceIm implements ServerService {

    private final ServerRepository serverRepository;

    @Override
    public Server saving(Server server) {
        log.info("Saving New Server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepository.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging the server ip: {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(1000) ? SERVER_UP : SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Available Servers");
        return serverRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("fetching server: {}", id);
        return serverRepository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("updating a server: {}", server.getName());
        return serverRepository.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Delete Server: {}", id);
        serverRepository.deleteById(id);
        return TRUE;
    }

    private String setServerImageUrl() {
        String[] images = {"server1.png", "server2.png", "server3.png", "server4.png"};
        String uriString = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/server/server_images/" + images(new Random().nextInt(4))).toUriString();
        return uriString;
    }

    private String images(int i) {
        return null;
    }

    private boolean isReachable(String ipAddress, int port, int timeOut) {
        try {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ipAddress, port), timeOut);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}