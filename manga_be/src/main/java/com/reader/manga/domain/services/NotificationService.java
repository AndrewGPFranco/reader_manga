package com.reader.manga.domain.services;

import com.reader.manga.domain.entities.notifications.Notification;
import com.reader.manga.ports.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Page<Notification> getAllNotifications(Integer paginaAtual) {
        Pageable pageable = PageRequest.of(paginaAtual, 4);
        return notificationRepository.findAllOrderByDataInDesc(pageable);
    }
}
